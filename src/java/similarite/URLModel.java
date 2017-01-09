package similarite;

import grapher.Populator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

public class URLModel {
	
	/* Model = set of triplets
	 * 	Statement = single triplet
	 * 		Resource = subject
	 * 		RDFNode = object
	 * 		Property = predicate
	 * */

	private Model model;
	private String url;

	public URLModel(List<Resource> resources, String url) {
		this.url = url;
		createModelFromResource(resources);
	}

	public List<Resource> getResources() {
		return model.listSubjects().toList();
	}

	public String getURL() {
		return url;
	}

	/**
	 * 
	 * @brief donne les n modeles les plus similaires par les triplets au model actif parmi une liste de modeles
	 * 
	 * @param urlModels la liste de modeles
	 * @param n
	 * @return la liste des n model les plus similaires par les triplets
	 */
	
	public List<String> getPlusSimilairesURL(List<URLModel> urlModels, int n) {
		List<String> urlSimilaires = new LinkedList<String>();
		
		List<Long> similarite = new LinkedList<Long>();
		
		for (URLModel urlModel : urlModels) {			
			int indiceToAdd = 0;
			long similariteCourante = this.similairiteParTriplets(urlModel);
			
			while(urlSimilaires.size() > indiceToAdd && similariteCourante < similarite.get(indiceToAdd)) {
				indiceToAdd++;
			}
			
			urlSimilaires.add(indiceToAdd, urlModel.getURL());
			similarite.add(indiceToAdd, similariteCourante);
			
			if (urlSimilaires.size() > n) {
				urlSimilaires.remove(urlSimilaires.size() - 1);
				similarite.remove(similarite.size() - 1);
			}
		}

		return urlSimilaires;
	}

	/**
	 * 
	 * @brief calcule l'indice de similarite par les sujets avec un autre
	 *        URLModel
	 * 
	 * @param model2
	 *            l'autre URLModel
	 * @return l'indice de similarite calule selon l'indexe de Jaccard 
	 */
	public long similairiteParSujets(URLModel model2) {
		long similarite = 0;
		ResIterator model1Sujets = model.listSubjects();

		while (model1Sujets.hasNext()) {

			ResIterator model2Sujets = model2.model.listSubjects();
                        Resource a = model1Sujets.next();

			while (model2Sujets.hasNext()) {
				Resource b = model2Sujets.next();
                                
				if (a.equals(b)) {
					similarite++;
				}
			}
		}
		// Selon l'indexe de Jaccard J(A,B) = |A^B|/|AvB|
		return similarite / (model.size() + model2.model.size() - similarite);
	}

	/**
	 * 
	 * @brief calcule l'indice de similarité par les triplets avec un autre
	 *        URLModel
	 * 
	 * @param model2
	 *            l'autre URLModel
	 * @return l'indice de similarite calule selon l'indexe de Jaccard 
	 */
	public long similairiteParTriplets(URLModel model2) {

		return intersectionTripletsNorme(model, model2.model)
				/ (model.size() + model2.model.size() - intersectionTripletsNorme(model, model2.model));
	}

	private long intersectionTripletsNorme(Model model1, Model model2) {

		Model intersection = model1.intersection(model2);

		return intersection.size();
	}

	private void createModelFromResource(List<Resource> resources) {

		model = ModelFactory.createDefaultModel();
                Populator populator = new Populator();

		for (Resource resource : resources) {
			
                        model.createResource(resource);
                        populator.populate(resource);
		}
	}
}
