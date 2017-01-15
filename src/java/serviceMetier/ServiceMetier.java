package serviceMetier;

import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.PDistClusteringAlgorithm;
import com.apporiented.algorithm.clustering.WeightedLinkageStrategy;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.jena.rdf.model.Resource;

import grapher.Populator;
import grapher.SpotlightEntity;
import grapher.SpotlightWrapper;
import java.util.Map;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import searchEngine.SearchEngine;
import similarite.DissimilariteMatrice;

/**
 * 
 * @author Pierre-Louis Lefebvre
 * 
 */
public class ServiceMetier {

	public Cluster getSimilariteDeRequete(String search, int n) throws IOException {		
            SearchEngine searchEngine = new SearchEngine();
            SpotlightWrapper spotlightWrapper = new SpotlightWrapper();
            Populator populator = new Populator();

            List<String> pagesURL = searchEngine.searchUrl(search);

            List<Model> allResources = new LinkedList<>();
            Map<String, Model> urlToGraph = new HashMap<>();

            for (String pageURL : pagesURL) {
                    String textPage = searchEngine.getText(pageURL);
                    SpotlightEntity[] spotlightEntities = spotlightWrapper.entityResult(textPage);
                    Model model = ModelFactory.createDefaultModel();

                    for (SpotlightEntity spotlightEntity : spotlightEntities) {
                            Resource resource = spotlightEntity.getJenaResource();
                            populator.populate(resource, model);
                    }

                    allResources.add(model);
                    urlToGraph.put(pageURL, model);
            }

            DissimilariteMatrice similariteMatrice = new DissimilariteMatrice(allResources, DissimilariteMatrice.TypeSimilarite.PAR_TRIPLETS);
            ClusteringAlgorithm alg = new PDistClusteringAlgorithm();
            Cluster cluster = alg.performClustering(similariteMatrice.getSimilarites(), (String[]) pagesURL.toArray(), new WeightedLinkageStrategy());

            // get 4 mains clusters
            List<Cluster> clusters = new LinkedList<>();

            clusters.add(cluster.getChildren().get(0).getChildren().get(0));
            clusters.add(cluster.getChildren().get(0).getChildren().get(1));
            clusters.add(cluster.getChildren().get(1).getChildren().get(0));
            clusters.add(cluster.getChildren().get(1).getChildren().get(1));

            for(Cluster c : clusters) {                   
                List<Model> models = new LinkedList<>();
                List<String> urls = c.getLeafNames();

                for(String url : urls) {
                    models.add(urlToGraph.get(url));
                }

                List<String> mainResources = getMainResources(models);

                for(int i = 0; i < urls.size(); i++) {

                    String url = urls.remove(i);

                    for(String uri : mainResources) {
                        url = url.concat("\n" + uri);
                    } 

                    urls.add(i, url);
                }
            }

            return cluster;
        }

	public List<String> getMainResources(List<Model> models) {
            List<String> mainResources = null;
            HashMap <String, Integer> resourcesMap = new HashMap<>();
            int[] intPlusPresentes = new int[] {0,0,0};
            String[] stringPlusPresentes = new String[3];
            int total;

            for(int i=0; i < models.size(); i++) {
                Model x = models.get(i);
                ResIterator iterator = x.listSubjects();

                while(iterator.hasNext()) {
                    Resource res = iterator.next();

                    if(resourcesMap.containsKey(res.getURI())) {
                        total = resourcesMap.get(res.getURI());
                        total++;
                        resourcesMap.put(res.getURI(), total);

                    } else {
                        total = 1;
                        resourcesMap.put(res.getURI(), total);
                    }

                    if(total > intPlusPresentes[2]) {
                        intPlusPresentes[0] = intPlusPresentes[1];
                        stringPlusPresentes[0] = stringPlusPresentes[1]; 
                        intPlusPresentes[1] = intPlusPresentes[2];
                        stringPlusPresentes[1] = stringPlusPresentes[2];
                        intPlusPresentes[2] = total;
                        stringPlusPresentes[2] = res.getURI();

                    } else if(total > intPlusPresentes[1]) {
                        intPlusPresentes[0] = intPlusPresentes[1];
                        stringPlusPresentes[0] = stringPlusPresentes[1];
                        intPlusPresentes[1] = total;
                        stringPlusPresentes[1] = res.getURI();

                    } else if(total > intPlusPresentes[0]) {
                        intPlusPresentes[0] = total;
                        stringPlusPresentes[0] = res.getURI();
                    }
                }			
            }

            for(int i = 0; i > 3; i++) {
                    mainResources.add(stringPlusPresentes[i]);
            }

            return mainResources;
	}
 }
