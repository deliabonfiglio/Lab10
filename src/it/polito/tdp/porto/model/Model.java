package it.polito.tdp.porto.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.*;
import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	private Graph<Author, DefaultEdge> graph ;
	private List<Author> authors= new ArrayList<Author>();
	private DijkstraShortestPath<Author, DefaultEdge> dj;
	private AuthorIdMap authorMap =new AuthorIdMap();
	private PaperIdMap paperIdMap = new PaperIdMap();

	public List<Author> getAuthors() {
		authors.clear();		
		if(authors.isEmpty()){
			PortoDAO dao = new PortoDAO();
			authors.addAll(dao.listAuthors(authorMap));
				for(Author a: authors){
					dao.getArticolsOfAuthor(a, paperIdMap);
				}
			}
		return authors;
	}

	
	public void createGraph(){
		if(graph==null){
			graph = new SimpleGraph<Author, DefaultEdge>(DefaultEdge.class);
		}
		
		PortoDAO dao = new PortoDAO();
		
		//aggiungo tutti gli autori come vertici
		Graphs.addAllVertices(graph, this.getAuthors());
		
		//aggiungo come archi tutti i coautori dell'autore
		for(Author autore: graph.vertexSet()){
				for(Author coautore: dao.getCoauthors(autore, authorMap))
					graph.addEdge(autore, coautore);			
		}
	}
	
	public List<Author> getNeighbours(Author autore){
		return Graphs.neighborListOf(graph, autore);
	}

	public List<Author> getAuthorsNotCoauthors(Author author) {
		List<Author> notCoauthor = new ArrayList<Author>(this.getAuthors());
		PortoDAO dao = new PortoDAO();
		
//mi creo una lista di non coautori OGNI VOLTA CHE CHIAMO IL METODO!
//Partendo da quella di tutti gli autori e rimuovendo i coautori dell'autore selezionato
			notCoauthor.removeAll(dao.getCoauthors(author, authorMap));
			notCoauthor.remove(author);
		
		return notCoauthor;
	}
	
	public Set<Paper> getShortestPath(Author a1, Author a2){
		
		dj= new DijkstraShortestPath<Author, DefaultEdge>(graph, a1, a2);	
//faccio un set cosi non devo controllare i duplicati, poichè si basa sull'equal se il paper già è stato trovato non lo prende
		Set<Paper> papers = new HashSet<Paper>();

		if(dj.getPathEdgeList()!=null){
			List<DefaultEdge> lis = new ArrayList<DefaultEdge>(dj.getPathEdgeList());
				
			for(DefaultEdge dfe: lis){		
				//papers.add(dao.getArticolo(graph.getEdgeSource(dfe), graph.getEdgeTarget(dfe)));
				for(Paper pa1: graph.getEdgeSource(dfe).getArticles()){
						for(Paper pa2: graph.getEdgeTarget(dfe).getArticles()){							
							if(pa1.equals(pa2)){
								papers.add(pa1);
							}	
						}	
					}
				}
		}
		
		return papers;
	}
}