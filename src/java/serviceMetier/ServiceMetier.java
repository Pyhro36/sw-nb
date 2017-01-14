package serviceMetier;

import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.PDistClusteringAlgorithm;
import com.apporiented.algorithm.clustering.WeightedLinkageStrategy;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.jena.rdf.model.Resource;

import grapher.Populator;
import grapher.SpotlightEntity;
import grapher.SpotlightWrapper;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
		
		for (String pageURL : pagesURL) {
			String textPage = searchEngine.getText(pageURL);
			SpotlightEntity[] spotlightEntities = spotlightWrapper.entityResult(textPage);
                        Model model = ModelFactory.createDefaultModel();
                        
			for (SpotlightEntity spotlightEntity : spotlightEntities) {
				Resource resource = spotlightEntity.getJenaResource();
                                populator.populate(resource, model);
			}
			
                        allResources.add(model);
		}
                
                DissimilariteMatrice similariteMatrice = new DissimilariteMatrice(allResources, DissimilariteMatrice.TypeSimilarite.PAR_TRIPLETS);
                ClusteringAlgorithm alg = new PDistClusteringAlgorithm();
                Cluster cluster = alg.performClustering(similariteMatrice.getSimilarites(), (String[]) pagesURL.toArray(), new WeightedLinkageStrategy());
                
                return cluster;
        }
        
        private List<String> getMainMainResources(List<Model> models) {
            
            List<String> mainResources = null;
            
            return mainResources;
        }
}
