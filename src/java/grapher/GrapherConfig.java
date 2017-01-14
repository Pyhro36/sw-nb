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
	public static final String DEFAULT_SPARQL_ENDPOINT = "http://dbpedia.org/sparql/";
	
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
	public static final String BASE_SPARQL_QUERY_STRING = "PREFIX onto: <http://dbpedia.org/ontology/> SELECT ?property ?value WHERE { FILTER (?property != rdf:type AND ?property != rdfs:label AND ?property != rdfs:comment AND ?property != rdfs:seeAlso AND ?property != owl:sameAs AND ?property != onto:abstract ) <http://dbpedia.org/resource/Barack_Obama> ?property ?value . }";
	
	/**
	 * @brief Indice de confiance utilisé par Spotlight
	 * 
	 * Valeur par défaut: 0.35
	 */
	public static final double SPOTLIGHT_CONFIDENCE = 0.5;
	
	/**
	 * @brief URL du serveur Spotlight par défaut
	 * 
	 * Il est important que ce serveur soit accessible publiquement, indépendant et toujours disponible.
	 * 
	 * Valeur par défaut: <tt>http://spotlight.sztaki.hu:2222/rest/annotate</tt>
	 */
	public static final String DEFAULT_SPOTLIGHT_URL = "http://www.dbpedia-spotlight.com/fr/annotate";
}
