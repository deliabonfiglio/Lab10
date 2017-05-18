package it.polito.tdp.porto.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.AuthorIdMap;
import it.polito.tdp.porto.model.Paper;
import it.polito.tdp.porto.model.PaperIdMap;

public class PortoDAO {
	
	public List<Author> listAuthors(AuthorIdMap authorMap) {
		
		final String sql = "SELECT * FROM author ORDER BY lastname";
		List<Author> authors = new ArrayList<Author>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Author autore=authorMap.get(rs.getInt("id"));
				
				if( autore ==null){
					autore = new Author(rs.getString("lastname"), rs.getString("firstname"),rs.getInt("id"));
					autore = authorMap.put(autore);
				}
				
				authors.add(autore);
			}
			conn.close();
			return authors;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	public List<Author> getCoauthors(Author autore, AuthorIdMap authorMap) {

		final String sql = "SELECT DISTINCT c2.authorid "+
							"FROM creator as c1, creator as c2 "+
							"WHERE c1.eprintid=c2.eprintid and c1.authorid=? and c2.authorid<> c1.authorid ";
		List<Author> coauthors = new ArrayList<Author>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, autore.getId());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				int id= rs.getInt("authorid");
				Author autor = authorMap.get(id);
						
				coauthors.add(autor);
			}
			conn.close();
			return coauthors;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public void getArticolsOfAuthor(Author a, PaperIdMap paperIdMap) {
		final String sql = "select p.* "+
							"from paper as p, creator as c "+
							"where p.eprintid=c.eprintid and c.authorid= ? ";
			
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, a.getId());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Paper ptemp = paperIdMap.get(rs.getInt("eprintid"));
				
				if(ptemp ==null){
				ptemp = new Paper(rs.getInt("eprintid"), rs.getString("title"), rs.getString("issn"), rs.getString("publication"),
						rs.getString("type"),rs.getString("types"));
				ptemp = paperIdMap.put(ptemp);
				}
				
				a.getArticles().add(ptemp);

			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}
	
	
	
}