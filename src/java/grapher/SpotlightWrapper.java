package grapher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * @brief Wrapper de DBPedia Spotlight
 * 
 * Cette classe contient des méthodes permettant à Java de communiquer avec le moteur DBPedia Spotlight.
 * 
 * @author Thibaut FERNANDEZ
 *
 */
public class SpotlightWrapper {
	
	///@brief Serveur utilisé par cette instance
	private final String url;

	/**
	 * @brief Génère un tableau d'entités Spotlight
	 * 
	 * Cette méthode génère les entités Spotlight à partir de la réponse du serveur.
	 * Elle s'occupe de récupérer le tableau d'entités à partir de l'objet JSON racine.
	 * 
	 * @param json L'objet JSON racine retourné par Spotlight
	 * @return Le tableau d'entité Spotlight calculé à partir du champ "Resources" de l'objet json fourni en paramètre
	 */
	public static SpotlightEntity[] getEntityList(JsonObject json){
		JsonArray list = json.getJsonArray("Resources");
		SpotlightEntity[] r = new SpotlightEntity[list.size()];
		for (int i = 0; i < list.size(); i++) {
			r[i] = new SpotlightEntity(list.getJsonObject(i));
		}
		return r;
	}
	
	/**
	 * @brief Constructeur par défaut
	 * 
	 * Ce constructeur utilise l'URL par défaut
	 * 
	 * @see GrapherConfig.DEFAULT_SPOTLIGHT_URL
	 */
	public SpotlightWrapper() {
		this(GrapherConfig.DEFAULT_SPOTLIGHT_URL);
	}
	
	/**
	 * @brief Constructeur de base
	 * 
	 * Ce constructeur permet de spécifier l'URL à utiliser pour contacter le serveur
	 * 
	 * @param url Le serveur Spotlight à utiliser
	 */
	public SpotlightWrapper(String url) {
		this.url = url;
	}
	
	/**
	 * @brief Requête Spotlight avec résultats sous forme de chaîne HTML
	 * 
	 * Cette méthode envoie une requête au serveur Spotlight, en transmettant le texte fourni en paramètre.
	 * Le résultat est récupéré au format HTML sous la forme d'une chaîne de caractères.
	 * 
	 * Le résultat n'est pas parsé, la chaîne est renvoyée telle quelle.
	 * 
	 * @param text Le texte que Spotlight doit analyser
	 * @return La réponse du serveur, au format HTML
	 * @throws IOException En cas d'échec de la connection, une exception est lancée.
	 */
	public String htmlResult(String text) throws IOException {
		String query;
		InputStream response;
		URLConnection connection;
		int status;
		StringBuffer result = new StringBuffer();
		
		query = "confidence=" +
				URLEncoder.encode(String.valueOf(GrapherConfig.SPOTLIGHT_CONFIDENCE), "UTF-8") +
				"&text=" +
				URLEncoder.encode(text, "UTF-8");
				
		connection = new URL(this.url).openConnection();
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		connection.setRequestProperty("Accept", "text/html");
		connection.setDoOutput(true);
		
		try (OutputStream output = connection.getOutputStream()) {
		    output.write(query.getBytes("UTF-8"));
		}
		
		response = connection.getInputStream();
		status = ((HttpURLConnection)connection).getResponseCode();
		
		if (status != 200 && status != 304) {
			throw new IOException("Bad HTTP Status code:" + String.valueOf(status));
		}
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(response))) {
	        for (String line; (line = reader.readLine()) != null;) {
	            result.append(line);
	        }
	    }
		
		return new String(result);
	}
	
	/**
	 * @brief Requête Spotlight avec résultats sous forme de chaîne JSON
	 * 
	 * Cette méthode envoie une requête au serveur Spotlight, en transmettant le texte fourni en paramètre.
	 * Le résultat est récupéré au format JSON sous la forme d'une chaîne de caractères.
	 * 
	 * Le résultat n'est pas parsé, la chaîne est renvoyée telle quelle.
	 * 
	 * @param text Le texte que Spotlight doit analyser
	 * @return La réponse du serveur, au format JSON
	 * @throws IOException En cas d'échec de la connection, une exception est lancée.
	 */
	public String jsonResult(String text) throws IOException {
		String query;
		InputStream response;
		URLConnection connection;
		int status;
		StringBuffer result = new StringBuffer();
		
		query = "confidence=" +
				URLEncoder.encode(String.valueOf(GrapherConfig.SPOTLIGHT_CONFIDENCE), "UTF-8") +
				"&text=" +
				URLEncoder.encode(text, "UTF-8");
		
		connection = new URL(this.url).openConnection();
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);
		
		try (OutputStream output = connection.getOutputStream()) {
		    output.write(query.getBytes("UTF-8"));
		}
		
		response = connection.getInputStream();
		status = ((HttpURLConnection)connection).getResponseCode();
		
		if (status != 200 && status != 304) {
			throw new IOException("Bad HTTP Status code:" + String.valueOf(status));
		}
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(response))) {
	        for (String line; (line = reader.readLine()) != null;) {
	            result.append(line);
	        }
	    }
		
		return new String(result);
	}

	/**
	 * @brief Requête Spotlight avec résultats sous forme de tableau d'entités Spotlight
	 * 
	 * Cette méthode envoie une requête au serveur Spotlight, en transmettant le texte fourni en paramètre.
	 * Le résultat est récupéré au format JSON sous la forme d'une chaîne de caractères.
	 * 
	 * Le résultat est parsé et les entités sont récupérées.
	 * Elles sont rassemblés dans un tableau qui est retourné par cette méthode.
	 * 
	 * @param text Le texte que Spotlight doit analyser
	 * @return Les entités qui ont été trouvées par Spotlight
	 * @throws IOException En cas d'échec de la connection, une exception est lancée.
	 */
	public SpotlightEntity[] entityResult(String text) throws IOException {
		String query;
		InputStream response;
		URLConnection connection;
		int status;
		
		query = "confidence=" +
				URLEncoder.encode(String.valueOf(GrapherConfig.SPOTLIGHT_CONFIDENCE), "UTF-8") +
				"&text=" +
				URLEncoder.encode(text, "UTF-8");
		
		connection = new URL(this.url).openConnection();
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);
		
		try (OutputStream output = connection.getOutputStream()) {
		    output.write(query.getBytes("UTF-8"));
		}
		
		response = connection.getInputStream();
		status = ((HttpURLConnection)connection).getResponseCode();
		
		if (status != 200 && status != 304) {
			throw new IOException("Bad HTTP Status code:" + String.valueOf(status));
		}
		
		return getEntityList(Json.createReader(response).readObject());
	}
	
}
