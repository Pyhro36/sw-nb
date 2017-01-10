/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package similarite;

import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author Pierre-Louis
 */
public class DissimilariteMatrice {
    private double[][] dissimilarites;
    private List<Model> models;
    
    public enum TypeSimilarite {
        PAR_TRIPLETS,
        PAR_SUJETS;
    }
    
    public DissimilariteMatrice (List<Model> Models, TypeSimilarite type) {
        this.models = Models;
        dissimilarites = new double[models.size()][models.size()];
        
        for (int i = 0; i < models.size(); i++) {
            
            for (int j = 0; j < models.size(); j++) {
                
                switch(type) {
                    case PAR_SUJETS :
                        dissimilarites[i][j] = (1 - similariteParSujets(i, j));            
                        break;
                    case PAR_TRIPLETS :
                    default :
                        dissimilarites[i][j] = (1 - similariteParTriplets(i, j));            
                        break;
                }
            }
        }
    }

    public double[][] getSimilarites() {
        return dissimilarites;
    }
    	
    private double similariteParSujets(int indiceModel1, int indiceModel2) {
		double similarite = 0;
                Model model1 = models.get(indiceModel1);
                Model model2 = models.get(indiceModel2);     
                
		ResIterator model1Sujets = model1.listSubjects();

		while (model1Sujets.hasNext()) {

			ResIterator model2Sujets = model2.listSubjects();
                        Resource a = model1Sujets.next();

			while (model2Sujets.hasNext()) {
				Resource b = model2Sujets.next();
                                
				if (a.equals(b)) {
					similarite++;
				}
			}
		}
		// Selon l'indexe de Jaccard J(A,B) = |A^B|/|AvB|
		return similarite / (model1.listSubjects().toList().size() + model2.listSubjects().toList().size() - similarite);
    }

    /**
     * 
     * @brief calcule l'indice de similarité par les triplets entre deux
     * graphes RDF de la matrice
     * @param indiceModel1 l'indice du 1er graphe RDF
     * @param indiceModel2 l'indice du deuxieme graphe RDF
     * @return l'indice de similarite calule selon l'indexe de Jaccard 
     */
    private double similariteParTriplets(int indiceModel1, int indiceModel2) {
        Model model1 = models.get(indiceModel1);
        Model model2 = models.get(indiceModel2); 

        long intersectionNorme = intersectionTripletsNorme(model1, model2);

        double ret = (double) (intersectionNorme / (model1.size() + model2.size() - intersectionNorme));           
        return ret;
    }

    private long intersectionTripletsNorme(Model model1, Model model2) {
            Model intersection = model1.intersection(model2);
            return intersection.size();
    }
}
