package grapher;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.json.JsonObject;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

/**
 * @brief Représentation d'une entité retournée par Spotlight
 * 
 * Cette classe a pour vocation de stocker et représenter les données d'une entité retournée par DBPedia Spotlight.
 * Cette classe représente @b une entité. L'ensemble des entités retorunées par une requête Spotlight doit être représenté par une liste d'instances de cette classe.
 * 
 * Cette classe n'est pas liée à l'API Jena et n'est pas du RDF.
 * Ce choix a été fait afin de conserver les métadonnées supplémentaires fournies par SPotlight.
 * 
 * @author Thibaut FERNANDEZ
 *
 */
public class SpotlightEntity {

	///@brief L'URI DBPedia de l'entité
	public String URI;
	
	///@brief La valeur de "support" retournée par Spotlight
	public int support;
	
	///@brief Les types de l'entité DBPedia
	public String[] types;
	
	///@brief Le texte orignal correspondant à l'entité
	public String text;
	
	///@brief La position dans le texte original à laquelle débute le texte reconnu comme correspondant à l'entité
	public int offset;
	
	///@brief Le score de similarité retourné par Spotlight
	public double similarity;
	
	///@brief La valeur de "percentageOfSecondRank" retorunée par Spotlight
	public double percentageOfSecondRank;
	
	
	/**
	 * @brief Constructeur de base
	 * 
	 * Ce constructeur prend en paramètre un JsonObject, de l'API Javax Json, obtenu en parsant le code JSON d'un résultat Spotlight.
	 * 
	 * Cet objet JSON doit correspondre à l'un des objets de la liste "Resources" renvoyée par Spotlight dans un résultat au format JSON.
	 * 
	 * @param JSONrepr L'objet JSON définissant l'entité à créer
	 */
	public SpotlightEntity(JsonObject JSONrepr) {
		this.URI = JSONrepr.getString("@URI", "");
		this.types = JSONrepr.getString("@types", "").split(",");
		this.text = JSONrepr.getString("@surfaceForm", "");
		this.support = Integer.parseInt(JSONrepr.getString("@support"));
		this.offset = Integer.parseInt(JSONrepr.getString("@offset"));
		this.similarity = Double.parseDouble(JSONrepr.getString("@similarityScore"));
		this.percentageOfSecondRank = Double.parseDouble(JSONrepr.getString("@percentageOfSecondRank"));
	}
	
	///@brief Constructeur nul
	private SpotlightEntity() {
		this.URI = null;
		this.types = null;
		this.text = null;
		this.support = 0;
		this.offset = 0;
		this.similarity = 0;
		this.percentageOfSecondRank = 0;
	}
	
	/**
	 * @brief Génération de la ressource RDF au format JENA
	 * 
	 * Cette méthode permet de générer un objet Resource Jena et de l'associer à l'URI fourni par Spotlight.
	 * 
	 * @note Cette méthode ne peuple pas la ressource et ne lui ajoute pas les propriétés qui lui sont liées dans DBPedia.
	 * 
	 * @return Un objet Resource de l'API Jena dont l'URI est celle de la ressource courante
	 */
	public Resource getJenaResource() {
		return ModelFactory.createDefaultModel().createResource(this.URI);
	}
	
	@Override
	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append('"');
		b.append(this.text);
		b.append('"');
		b.append('<');
		b.append(this.URI);
		b.append('>');
		b.append('[');
		for (String t : this.types) {
			b.append(t);
			b.append(',');
		}
		b.append(']');
		b.append('(');
		b.append(String.valueOf(this.similarity));
		b.append(')');
		return new String(b);
	}
	
	/**
	 * 
	 * @brief Agrégation de plusieurs entités Spotlight
	 * 
	 * Cette méthode fusionne les entités Spotlight identiques d'une liste pour ne garder que des entités d'URI différentes
	 * 
	 * L'agrégation est effectuée de la manière suivante:
	 * @li L'URI est la même pour toutes les entités agrégées entre elles. Si deux entités ont une URI différente, elles ne sont pas agrégées.
	 * @li Le texte retenu est celui de la première entité trouvée.
	 * @li Les types de l'entité résultante sont une union des types de chacune des entités.
	 * @li Les variables numériques de l'entité résultante sont la moyenne arithmétique des variables numériques des entités de départ.
	 * 
	 * @param list Une liste d'entités Spotlight, contenant des doublons
	 * @return La liste d'entités agrégées
	 */
	public static SpotlightEntity[] agregate(SpotlightEntity[] list) {
		Vector<SpotlightEntity> result = new Vector<SpotlightEntity>();
		Vector<SpotlightEntity> lst = new Vector<SpotlightEntity>();
		Set<String> types;
		String URI, txt;
		SpotlightEntity tmpEnt;
		double similarity, posr;
		int number, offset, support;
		
		for (SpotlightEntity e : list) lst.add(e);
		
		for (int i = 0; i < lst.size(); i++) {
			// Initialisation
			URI = lst.get(i).URI;
			txt = lst.get(i).text;
			offset = 0;
			support = 0;
			similarity = 0;
			posr = 0;
			number = 0;
			types = new HashSet<String>();
			
			// Recherche des entités identiques
			for (int j = i; j < lst.size(); j++) {
				if (lst.get(j).URI.equals(URI)) {
					for (String t : lst.get(j).types) types.add(t);
					offset += lst.get(j).offset;
					support += lst.get(j).support;
					similarity += lst.get(j).similarity;
					posr += lst.get(j).percentageOfSecondRank;
					number++;
					i--;
					j--;
					lst.remove(j);
				}
			}
			
			// Génération de l'entité agrégée
			tmpEnt = new SpotlightEntity();
			tmpEnt.URI = URI;
			tmpEnt.text = txt;
			tmpEnt.offset = (int)(((double)offset) / number);
			tmpEnt.support = (int)(((double)support) / number);
			tmpEnt.similarity = similarity / number;
			tmpEnt.percentageOfSecondRank = posr / number;
			tmpEnt.types = types.toArray(tmpEnt.types);
			result.add(tmpEnt);
		}
		
		return result.toArray(new SpotlightEntity[result.size()]);
		
	}
}
