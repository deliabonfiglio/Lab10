package it.polito.tdp.porto.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.*;
import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	private Graph<Author, DefaultEdge> graph ;
	private List<Author> authors= new ArrayList<Author>();

	public List<Author> getAuthors() {
		authors.clear();
		if(authors.isEmpty()){
			PortoDAO dao = new PortoDAO();
			authors.addAll(dao.getAllAuthors().values());
		}
		return authors;
	}

	public List<Author> getCoauthors(Author autore){
		PortoDAO dao = new PortoDAO();
		
		return dao.getCoauthors(autore);
	}
	
	public void createGraph(){
		if(graph==null){
			graph = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		}
		
		PortoDAO dao = new PortoDAO();
		
		//aggiungo tutti gli autori come vertici
		Graphs.addAllVertices(graph, this.authors);
		
		//aggiungo come archi tutti i coautori dell'autore
		for(Author autore: graph.vertexSet()){
				for(Author coautore: dao.getCoauthors(autore))
					graph.addEdge(autore, coautore);
			
		}
		
		System.out.println(graph.toString());
	}
	
	public List<Author> getNeighbours(Author autore){
		return Graphs.neighborListOf(graph, autore);
	}
}


