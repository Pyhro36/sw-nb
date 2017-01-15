/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serviceMetier;

import com.apporiented.algorithm.clustering.AverageLinkageStrategy;
import com.apporiented.algorithm.clustering.Cluster;
import com.apporiented.algorithm.clustering.ClusteringAlgorithm;
import com.apporiented.algorithm.clustering.DefaultClusteringAlgorithm;
import com.apporiented.algorithm.clustering.WeightedLinkageStrategy;
import com.apporiented.algorithm.clustering.visualization.DendrogramPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.apache.jena.rdf.model.Model;
import similarite.DissimilariteMatrice;
import similarite.GraphsModelsConstructor;

/**
 *
 * @author PL
 */
public class DissimilariteDendrogramItergationTest {
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(400, 300);
        frame.setLocation(400, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel content = new JPanel();
        DendrogramPanel dp = new DendrogramPanel();

        frame.setContentPane(content);
        content.setBackground(Color.red);
        content.setLayout(new BorderLayout());
        content.add(dp, BorderLayout.CENTER);
        dp.setBackground(Color.WHITE);
        dp.setLineColor(Color.BLACK);
        dp.setScaleValueDecimals(0);
        dp.setScaleValueInterval(1);
        dp.setShowDistances(false);

        Cluster cluster = createSampleClusterWithGraphs();
        dp.setModel(cluster);
        frame.setVisible(true);
    }
    
    private static Cluster createSampleClusterWithGraphs() {
        ServiceMetier sm = new ServiceMetier();
        
        List<Model> recourcesModels = GraphsModelsConstructor.createExampleModels();
        String[] names = new String[]{"model1", "model2", "model3", "model4"};
        Map<String, Model> urlToGraph = new HashMap<>();
        
        for (int i = 0; i < names.length; i++) {
            urlToGraph.put(names[i], recourcesModels.get(i));
        }
        
        DissimilariteMatrice dm = new DissimilariteMatrice(recourcesModels, DissimilariteMatrice.TypeSimilarite.PAR_TRIPLETS);
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        Cluster cluster = alg.performClustering(dm.getSimilarites(), names, new AverageLinkageStrategy());
        cluster.toConsole(0);
        
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

           List<String> mainResources = sm.getMainResources(models);

           for(int i = 0; i < urls.size(); i++) {

               String url = urls.remove(i);

               for(String uri : mainResources) {
                   url = url.concat("  "+uri);
               } 

               urls.add(i, url);
           }
       }       
        
        return cluster;
    }
}
