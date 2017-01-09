package serviceMetier;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.jena.rdf.model.Resource;

import grapher.Populator;
import grapher.SpotlightEntity;
import grapher.SpotlightWrapper;
import searchEngine.SearchEngine;
import similarite.URLModel;

public class ServiceMetier {
	
	public List<String> getSimilariteDeRequete(String search, int n) throws IOException {
		
		SearchEngine searchEngine = new SearchEngine();
		SpotlightWrapper spotlightWrapper = new SpotlightWrapper();
		Populator populator = new Populator();
		
		List<String> pagesURL = searchEngine.searchUrl(search);
		
		List<URLModel> allResources = new LinkedList<>();
		URLModel referenceURLModel = null;
		
		for (String pageURL : pagesURL) {
			
			String textPage = searchEngine.getText(pageURL);
			SpotlightEntity[] spotlightEntities = spotlightWrapper.entityResult(textPage);
			List<Resource> resources = new LinkedList<>(); 
			
			for (SpotlightEntity spotlightEntity : spotlightEntities) {
				
				Resource resource = spotlightEntity.getJenaResource();
				populator.populate(resource);
				resources.add(resource);
			}
			
			allResources.add(new URLModel(resources, pageURL));
		}
		
		String referenceTextPage = searchEngine.getText(referenceURL);
		SpotlightEntity[] referenceSpotlightEntities = spotlightWrapper.entityResult(referenceTextPage);
		List<Resource> referencesResources = new LinkedList<>();
		
		for (SpotlightEntity spotlightEntity : referenceSpotlightEntities) {
			
			Resource resource = spotlightEntity.getJenaResource();
			populator.populate(resource);
			referencesResources.add(resource);
		}
		
		referenceURLModel = new URLModel(referencesResources, referenceURL);		
		
		return referenceURLModel.getPlusSimilairesURL(allResources, n);
	}
}
