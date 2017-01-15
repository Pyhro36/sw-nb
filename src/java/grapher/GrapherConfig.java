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
         * Cette URL doit pointer sur un endpoint SPARQL disposant de suffisamment
         * de données pour permettre au programme de fonctionner.
	 */
	public static final String DEFAULT_SPARQL_ENDPOINT = "http://dbpedia.org/sparql/";
	
	/**
	 * @brief La requête SPARQL qui sert de base pour peupler une ressource RDF
	 * 
	 * Cette requête est paramétrée: elle doit contenir des variables dont les noms sont spécifiques.
	 * Sans quoi, la mémthode <tt>Populator.populate()</tt> ne pourra pas formuler la requête finie, ni récupérer les données.
         * 
	 * Valeur avec un meilleur formattage:
         * 
         * @code
PREFIX owl: <http://www.w3.org/2002/07/owl#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX onto: <http://dbpedia.org/ontology/>
SELECT ?property ?value
WHERE {
    FILTER (
        ?property != rdf:type &&
        ?property != rdfs:label &&
        ?property != rdfs:comment &&
        ?property != rdfs:seeAlso &&
        ?property != owl:sameAs &&
        ?property != onto:abstract
    )
    ?resource ?property ?value .
}
         * @endcode
	 * 
	 * @param ?resource est remplacé par le nom de la ressource à peupler, lors de l'exécution de la requête
	 * @param ?property Le type de propiété, est utilisé pour lier <tt>?value</tt> à la ressource
	 * @param ?value La valeur de la propriété identifiée
	 */
	public static final String BASE_SPARQL_QUERY_STRING = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX onto: <http://dbpedia.org/ontology/> SELECT ?property ?value WHERE { FILTER (?property != rdf:type && ?property != rdfs:label && ?property != rdfs:comment && ?property != rdfs:seeAlso && ?property != owl:sameAs && ?property != onto:abstract ) ?resource ?property ?value . }";
	
	/**
	 * @brief Indice de confiance utilisé par Spotlight
	 * 
	 * Plus cette valeur est élevé, plus Spotlight sera strict sur la correspondance entre un terme et une ressource.
	 */
	public static final double SPOTLIGHT_CONFIDENCE = 0.5;
	
	/**
	 * @brief URL du serveur Spotlight par défaut
	 * 
	 * Il est important que ce serveur soit accessible publiquement, indépendant et toujours disponible.
	 */
	public static final String DEFAULT_SPOTLIGHT_URL = "http://www.dbpedia-spotlight.com/fr/annotate";
}
