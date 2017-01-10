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
import java.util.List;
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
        List<Model> models = GraphsModelsConstructor.createExampleModels();
        
        DissimilariteMatrice dm = new DissimilariteMatrice(models, DissimilariteMatrice.TypeSimilarite.PAR_TRIPLETS);
        ClusteringAlgorithm alg = new DefaultClusteringAlgorithm();
        String[] names = new String[]{"model1", "model2", "model3", "model4"};
        Cluster cluster = alg.performClustering(dm.getSimilarites(), names, new AverageLinkageStrategy());
        cluster.toConsole(0);
        return cluster;
    }
}
