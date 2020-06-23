package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	private FoodDao dao;
	private Map<Integer,Food> alimenti;
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
public Model() {
	dao= new FoodDao();
	alimenti=dao.listAllFoods();
}	
public void creaMappa(int n) {
	grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	List<Food> vertici=dao.listFoodbyPortion(n, alimenti);
	System.out.println("Lista dimensione: "+alimenti.size());
	Graphs.addAllVertices(this.grafo, vertici);
	System.out.println("Vertici dimensione: "+vertici.size());
	List<Arco> archi = dao.listArchi(alimenti);
	System.out.println("Lista archi dimensione: "+archi.size());
	for(Arco a:archi) {
		Food a1=a.getAlimento1();
		Food a2=a.getAlimento2();
		//controllo esistenza vertici
		if(grafo.containsVertex(a1)&&grafo.containsVertex(a2)) {
		//Controllo esistenza arco
		if(!grafo.containsEdge(a1, a2)) {
			
			double peso=dao.pesoArco(a.getAlimento1(), a.getAlimento2());
			System.out.println("Alimento1: "+a1.getDisplay_name());
			System.out.println("Alimento2: "+a2.getDisplay_name());
			System.out.println("peso arco: "+peso);
			//Aggiungo arco
		Graphs.addEdge(grafo,a1, a2,peso );
		
		}}
	}
	System.out.println("Lista archi grafo: "+grafo.edgeSet().size());
	
}
public List<Food> getFood(){
	List<Food> result =new ArrayList<>();
	for(Food f:this.grafo.vertexSet())result.add(f);
	Collections.sort(result);
	
	return result;
}
public List<CalorieLista> calorieMassime(Food origine){
	List<CalorieLista> result =new ArrayList<>();
	if(Graphs.neighborListOf(grafo, origine).size()==0)return null;
	for(Food f: Graphs.neighborListOf(grafo, origine)) {
		double peso = grafo.getEdgeWeight(this.grafo.getEdge(origine, f));
		result.add(new CalorieLista(f,peso));
	}
	Collections.sort(result);
	List<CalorieLista> resultfiltered= result.subList(0, 5);
	return resultfiltered;
	
}

}
