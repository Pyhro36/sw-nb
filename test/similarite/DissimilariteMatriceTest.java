/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarite;

import java.util.LinkedList;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pierre-Louis Lefebvre
 */
public class DissimilariteMatriceTest {
    /**
     * Test of getSimilarites method, of class DissimilariteMatrice.
     */
    @Test
    public void testGetSimilarites() { 
        System.out.println("getSimilarites");
        
        List<Model> models = GraphsModelsConstructor.createExampleModels();
                  
        DissimilariteMatrice instanceTriplets = new DissimilariteMatrice(models, DissimilariteMatrice.TypeSimilarite.PAR_TRIPLETS);
        DissimilariteMatrice instanceSujets = new DissimilariteMatrice(models, DissimilariteMatrice.TypeSimilarite.PAR_SUJETS);
        
        double[][] expResultTriplets = new double[][]{ // (intersection / union)
            {0.0, (1.0 - (7.0 / 10.0)), (1.0 - (8.0 / 11.0)), (1.0 - (6.0 / 10.0))},
            {(1.0 - (7.0 / 10.0)), 0.0, (1.0 - (6.0 / 10.0)), (1.0 - (6.0 / 7.0))},
            {(1.0 - (8.0 / 11.0)), (1.0 - (6.0 / 10.0)), 0.0, (1.0 - (6.0 / 9.0))},
            {(1.0 - (6.0 / 10.0)), (1.0 - (6.0 / 7.0)), (1.0 - (6.0 / 9.0)), 0.0}
        };
        
        double[][] expResultSujets = new double[][]{ // (intersection / union)
            {0.0, 0.0, 0.5, 1.0 - (2.0 / 3.0)}, // compenser les approximations de calcul
            {0.0, 0.0, 0.5, 1.0 - (2.0 / 3.0)},
            {0.5, 0.5, 0.0, 1.0 - (2.0 / 3.0)},
            {1.0 - (2.0 / 3.0), 1.0 - (2.0 / 3.0), 1.0 - (2.0 / 3.0), 0.0}
        };
        
        double[][] resultTriplets = instanceTriplets.getSimilarites();
        double[][] resultSujets = instanceSujets.getSimilarites();
        
        assertArrayEquals(expResultTriplets, resultTriplets);
        assertArrayEquals(expResultSujets, resultSujets);

    }
}
