package grapher;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * @brief Classe servant à peupler les ressources RDF avec leurs paramètres
 * 
 * Cette classe permet d'interroger DBPedia (ou un autre servcie) pour retoruver les paramètres d'une ressource RDF.
 * Cette classe utilise l'API JENA pour représenter les données et pour effectuers les requêtes SPARQL.
 * 
 * 
 * @author Thibaut FERNANDEZ
 *
 */
public class Populator {
	
	///@brief L'URL du service SPARQL utilisé par l'instance courante
	public final String endpoint;
	
	/**
	 * @brief Constructeur avec les valeurs par défaut
	 * 
	 * Ce constructeur utilise les valeurs par défaut définies dans les constantes de classe.
	 * 
	 * @see GrapherConfig.DEFAULT_SPARQL_ENDPOINT
	 */
	public Populator() {
		this(GrapherConfig.DEFAULT_SPARQL_ENDPOINT);
	}
	
	/**
	 * @brief Constructeur principal
	 * 
	 * Ce constructeur permet de choisir les paramètres de l'objet
	 * 
	 * @param endpoint L'URL du service SPARQL utilisé pour les requêtes
	 */
	public Populator(String endpoint) {
		this.endpoint = endpoint;
	}
	

	/**
	 * @brief Peuple une ressource RDF
	 * 
	 * Cette méthode reçoit une ressource RDF, et l'utilise pour effectuer une requête SPARQL visant à récupérer toutes ses propriétés.
	 * Les propriétés ainsi récupérées sont ajoutées à la ressource, qui est retournée.
	 * 
	 * Le service SPARQL utilisé est celui fourni lors de la création de l'instance courante.
	 * 
	 * Cette version utilise le modèle de requête par défaut défini dans GrapherConfig.
	 * 
	 * @see GrapherConfig.BASE_SPARQL_QUERY_STRING
	 * 
	 * @param resource La ressource à peupler
         * @param model Le modèle JENA auquel appartient la ressource à peupler
	 * @return La ressource, une fois peuplée
	 */
	public Resource populate(Resource resource, Model model) {
		return populate(resource, model, GrapherConfig.BASE_SPARQL_QUERY_STRING);
	}
	
	/**
	 * @brief Peuple une ressource RDF
	 * 
	 * Cette méthode reçoit une ressource RDF, et l'utilise pour effectuer une requête SPARQL visant à récupérer toutes ses propriétés.
	 * Les propriétés ainsi récupérées sont ajoutées à la ressource, qui est retorunée.
	 * 
	 * Le service SPARQL utilisé est celui fourni lors de la création de l'instance courante.
	 * 
	 * La modèle de requête doit contenir trois variables prédéfinies. Plus d'informations dans la classe GrapherConfig.
	 * 
	 * @see GrapherConfig.BASE_SPARQL_QUERY_STRING
	 * 
	 * @param resource La ressource à peupler
         * @param model Le modèle JENA auquel appartient la ressource à peupler
	 * @param queryString Le modèle de requête à utiliser
	 * @return La ressource, une fois peuplée
	 */
	public Resource populate(Resource resource, Model model, String queryString) {
		QuerySolutionMap initialBinding = new QuerySolutionMap();
		initialBinding.add("resource", resource);
		Query query = (new ParameterizedSparqlString(queryString, initialBinding)).asQuery();
		try (QueryExecution qexec = QueryExecutionFactory.sparqlService(this.endpoint, query)) {
			ResultSet results = qexec.execSelect() ;
			for ( ; results.hasNext() ; ) {
				QuerySolution soln = results.nextSolution() ;
				if (soln == null) continue;
				if (soln.get("property") == null) continue;
				if (!soln.get("property").isURIResource()) continue;
				Property p = ResourceFactory.createProperty(soln.getResource("property").getURI());
				RDFNode v = soln.get("value");
				model.add(resource, p, v);
			}
		}
		return resource;
	}
}
