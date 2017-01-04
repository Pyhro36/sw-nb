package searchEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class SearchEngine {

	static Map<String, List<String>> resultMap = new HashMap<String, List<String>>();

	public SearchEngine() {
	};

	public List<String> searchUrl(String search) {

		List<String> urls = new ArrayList<String>();

		if (resultMap.containsKey(search)) {
			urls = resultMap.get(search);
		} else {

			//urls.addAll(searchWikipedia(search));
			urls.addAll(searchGoogle(search));
		}

		resultMap.put(search, urls);
		return urls;
	}
	
	public String searchMarvelWikia(String search) {
		String result = null;
		
		search.replaceAll(" ", "%20");
		
		String query = "http://fr.marvel.wikia.com/wiki/Spécial:Recherche?query=" + search;
		
		try {
			
			Document doc = Jsoup.connect(query).get();
			Elements content = doc.select(".result-link");
			
			String line = content.get(0).toString();
			
			result = line.substring(line.indexOf("http"));
			result = result.substring(0,result.indexOf("\""));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private List<String> searchWikipedia(String search) {

		String query = "http://fr.wikipedia.org/w/api.php?action=opensearch&search=" + search
				+ " marvel&format=json&utf8=true";

		List<String> urls = new ArrayList<String>();

		try {
			String json = Jsoup.connect(query).ignoreContentType(true).execute().body();

			JSONParser parser = new JSONParser();
			JSONArray result = (JSONArray) parser.parse(json);

			JSONArray urlList = (JSONArray) result.get(3);

			for (int i = 0; i < urlList.size(); i++) {
				urls.add(urlList.get(i).toString());
			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urls;
	}

	private List<String> searchGoogle(String search) {

		// Clé Victor
		String key = "AIzaSyCjlPDtLAvMEIDBlkNis3K1p2qyqcN-iuM";
		// Id Custom Search
		String cx = "013217534318604423727:bswaria7qe4";

		String query = "https://www.googleapis.com/customsearch/v1?key=" + key + "&cx=" + cx + "&q=" + search;

		List<String> urls = new ArrayList<String>();

		try {
			String json = Jsoup.connect(query).ignoreContentType(true).execute().body();
			JSONParser parser = new JSONParser();
			JSONObject result = (JSONObject) parser.parse(json);

			JSONArray items = (JSONArray) result.get("items");

			for (int i = 0; i < items.size(); i++) {
				JSONObject item = (JSONObject) items.get(i);
				urls.add((String) item.get("link"));
			}

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return urls;
	}
	
	public String getText(String url){
		String filtre=null;
		
		if(url.contains("wikipedia")){
			filtre=".mw-content-ltr p, .mw-content-ltr li";
		}else if(url.contains("imdb")){
			filtre="div[itemprop = description]";
		}else if(url.contains("wikia")){
			filtre="#mw-content-text";
		}else{
			filtre="body";
		}
		
		String textContent = null;
		Elements content=null;
		try {
			Document doc = Jsoup.connect(url).get();
			content = doc.select(filtre);
			textContent=content.text();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return textContent;
	}

}
