package grapher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @brief Configuration par le package Grapher
 * 
 * Cette classe contient des constantes statiques permettant de centraliser la configuration des classes du package grapher.
 * 
 * Ces constantes sont chargées dynamiquement au lancement du programme, à l'aide de la classe @c Properties (<tt>java.util</tt>).
 * Leurs valeurs sont situées dans un fichier XML, dont l'emplacement est défini par @c GrapherConfig.CONFIG_FILE_LOCATION.
 * Les valeurs par défaut indiquées pour les paramètres sont celles qui sont utilisées si le paramètre est absent du fichier de configuration.
 * 
 * @note Même s'il est techniquement possible de créer une instance de cette classe, cela est à la fois inutile et déconseillé.
 * 
 * @author Thibaut FERNANDEZ
 */
public class GrapherConfig {
	
	/**
	 * @brief L'URL par défaut du service SPARQL à utiliser pour peupler les entités RDF
	 * 
	 * Valeur par défaut: <tt>http://dbpedia.org/sparql/</tt>
	 */
	public static final String DEFAULT_SPARQL_ENDPOINT;
	
	/**
	 * @brief La requête SPARQL qui sert de base pour peupler une ressource RDF
	 * 
	 * Cette requête est paramétrée: elle doit contenir des variables dont les noms sont spécifiques.
	 * Sans quoi, la mémthode <tt>Populator.populate()</tt> ne pourra pas formuler la requête finie, ni récupérer les données.
	 * 
	 * Valeur par défaut:
	 * @code{.unparsed}
SELECT ?property ?value
WHERE {
	?resource ?property ?value .
}
	 * @endcode
	 * 
	 * @param ?resource est remplacé par le nom de la ressource à peupler, lors de l'exécution de la requête
	 * @param ?property Le type de propiété, est utilisé pour lier <tt>?value</tt> à la ressource
	 * @param ?value La valeur de la propriété identifiée
	 */
	public static final String BASE_SPARQL_QUERY_STRING;
	
	/**
	 * @brief Indice de confiance utilisé par Spotlight
	 * 
	 * Valeur par défaut: 0.35
	 */
	public static final double SPOTLIGHT_CONFIDENCE;
	
	/**
	 * @brief URL du serveur Spotlight par défaut
	 * 
	 * Il est important que ce serveur soit accessible publiquement, indépendant et toujours disponible.
	 * 
	 * Valeur par défaut: <tt>http://spotlight.sztaki.hu:2222/rest/annotate</tt>
	 */
	public static final String DEFAULT_SPOTLIGHT_URL;
	
	/**
	 * @brief Le chemin du fichier de configuration utilisé pour obtenir les valeurs des paramètres
	 * 
	 * Ce fichier est un fichier XML généré par la classe Properties de Java.
	 * 
	 * Ce fichier est régénéré à chaque exécution du programme
	 */
	public static final String CONFIG_FILE_LOCATION = "config/grapher.xml";
	
	static {
		Properties props = new Properties();
		try {
			props.loadFromXML(new FileInputStream(CONFIG_FILE_LOCATION));
		} catch (IOException e) { /* do nothing */ }
		
		DEFAULT_SPARQL_ENDPOINT = props.getProperty("defaultSparqlEndpoint", "http://dbpedia.org/sparql/");
		props.setProperty("defaultSparqlEndpoint", DEFAULT_SPARQL_ENDPOINT);
		
		BASE_SPARQL_QUERY_STRING = props.getProperty("baseQueryString", "SELECT ?property ?value WHERE { ?resource ?property ?value . }");
		props.setProperty("baseQueryString", BASE_SPARQL_QUERY_STRING);
		
		SPOTLIGHT_CONFIDENCE = Double.valueOf(props.getProperty("spotlightConfidence", "0.35"));
		props.setProperty("spotlightConfidence", String.valueOf(SPOTLIGHT_CONFIDENCE));
		
		DEFAULT_SPOTLIGHT_URL = props.getProperty("defaultSpotlightUrl", "http://spotlight.sztaki.hu:2222/rest/annotate");
		props.setProperty("defaultSpotlightUrl", DEFAULT_SPOTLIGHT_URL);
		
		try {
			props.storeToXML(new FileOutputStream(CONFIG_FILE_LOCATION), "IF4H4203 - Semantic Web Marvel Project\nGrapher Configuration file");
		} catch (IOException e) {
			//System.err.println("Unable to write to config file");
		}
	}
}
