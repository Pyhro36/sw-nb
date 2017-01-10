package grapher;

import java.io.IOException;
import org.apache.jena.rdf.model.Model;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;

/**
 * @brief Class de test du package grapher
 * 
 * Cette classe permet de procéder aux tests fonctionnels du package grapher
 * 
 * @author Thibaut FERNANDEZ
 *
 */
class GrapherTester {
	
	public static final String TEST_STRING = "President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance.";
	public static final String TEST_RESOURCE = "http://dbpedia.org/resource/Barack_Obama";
	
	public static void main(String[] args) {
		SpotlightWrapper wrapper = new SpotlightWrapper();
		
		System.out.println("Chaîne de test:");
		System.out.println(TEST_STRING);
		System.out.println();
		
		System.out.println("Test: résultats en HTML");
		try {
			String HTMLtest = wrapper.htmlResult(TEST_STRING);
			System.out.println(HTMLtest);
		} catch (IOException e) {
			System.out.println("Échec du test");
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println("Test: résultats en JSON");
		try {
			String HTMLtest = wrapper.jsonResult(TEST_STRING);
			System.out.println(HTMLtest);
		} catch (IOException e) {
			System.out.println("Échec du test");
			e.printStackTrace();
		}
		System.out.println();
		

		
		System.out.println("Test: résultats en SpotlightEntity");
		try {
			SpotlightEntity[] result = wrapper.entityResult(TEST_STRING);
			for (SpotlightEntity e : result) {
				System.out.println(e);
				System.out.println(e.getJenaResource());
			}
		} catch (IOException e) {
			System.out.println("Échec du test");
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println("Tests de peuplage avec la ressource:");
		System.out.println(TEST_RESOURCE);
		System.out.println();
		
		Model  model =  ModelFactory.createDefaultModel();
                Resource testResource = model.createResource(TEST_RESOURCE);
		Populator p = new Populator();
		testResource = p.populate(testResource, model);
		for (Statement s : testResource.listProperties().toList()) {
			System.out.println(s);
		}
	}
}
