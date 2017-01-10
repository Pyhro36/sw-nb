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

/**
 * Classe abstraite de construction d'exemples de graphes
 * @author Pierre-Louis Lefebvre
 */
public abstract class GraphsModelsConstructor {
    
    /**
     * 
     * @return une liste d'exemples de graphes RDF
     */
    public static List<Model> createExampleModels() {
        List<Model> models = new LinkedList<>();
        
        Model model1 = ModelFactory.createDefaultModel(); // graphe de reference
                                                          // qui a le plus d'elts
        
        Resource alice = ResourceFactory.createResource("http://example.org/alice");    	
        Resource bob = ResourceFactory.createResource("http://example.org/bob");
        Resource jena = ResourceFactory.createResource("http://example.org/jena");
        
        model1.add(alice, RDF.type, FOAF.Person);
        model1.add(alice, FOAF.name, "Alice");
        model1.add(alice, FOAF.mbox, ResourceFactory.createResource("mailto:alice@example.org"));
        model1.add(alice, FOAF.knows, bob);

        model1.add(bob, RDF.type, FOAF.Person);
        model1.add(bob, FOAF.name, "Bob");
        model1.add(bob, FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org"));
        model1.add(bob, FOAF.knows, alice);
        
        model1.add(jena, RDF.type, FOAF.Person);
        model1.add(jena, FOAF.name, "Jena");
        
        models.add(model1);
        
        model1.write(System.out, "TURTLE");
        
        Model model2 = ModelFactory.createDefaultModel();
        
        // instance differentes des resources de model1 pour bien verfier l'egalite par l'URI
        Resource alice2 = ResourceFactory.createResource("http://example.org/alice");    	
        Resource bob2 = ResourceFactory.createResource("http://example.org/bob");
        Resource jena2 = ResourceFactory.createResource("http://example.org/jena");
        
        model2.add(alice2, RDF.type, FOAF.Person);
        model2.add(alice2, FOAF.name, "Alice");
        model2.add(alice, FOAF.knows, bob);

        model2.add(bob2, RDF.type, FOAF.Person);
        model2.add(bob2, FOAF.name, "Bob");
        model2.add(bob2, FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org"));
        
        model2.add(jena2, RDF.type, FOAF.Person);
        
        models.add(model2);
               
        model2.write(System.out, "TURTLE");
        
        Model model3 = ModelFactory.createDefaultModel();
        
        Resource alice3 = ResourceFactory.createResource("http://example.org/alice");    	
        Resource bob3 = ResourceFactory.createResource("http://example.org/bob");
        Resource paul3 = ResourceFactory.createResource("http://example.org/paul");
        
        model3.add(alice3, RDF.type, FOAF.Person);
        model3.add(alice3, FOAF.name, "Alice");
        model3.add(alice3, FOAF.mbox, ResourceFactory.createResource("mailto:alice@example.org"));
        model3.add(alice3, FOAF.knows, bob);

        model3.add(bob3, RDF.type, FOAF.Person);
        model3.add(bob3, FOAF.name, "Bob");
        model3.add(bob3, FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org"));
        model3.add(bob3, FOAF.knows, alice);
        
        model3.add(paul3, RDF.type, FOAF.Person);
        
        models.add(model3);
        
        model3.write(System.out, "TURTLE");
                
        Model model4 = ModelFactory.createDefaultModel(); 
        
        Resource alice4 = ResourceFactory.createResource("http://example.org/alice");    	
        Resource bob4 = ResourceFactory.createResource("http://example.org/bob");
        
        model4.add(alice4, RDF.type, FOAF.Person);
        model4.add(alice4, FOAF.name, "Alice");
        model4.add(alice4, FOAF.knows, bob);

        model4.add(bob4, RDF.type, FOAF.Person);
        model4.add(bob4, FOAF.name, "Bob");
        model4.add(bob4, FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org"));
        
        model4.write(System.out, "TURTLE");
        
        models.add(model4);

       return models; 
    }   
}
