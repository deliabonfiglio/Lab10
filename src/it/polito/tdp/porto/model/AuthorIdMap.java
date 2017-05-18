package it.polito.tdp.porto.model;

import java.util.*;

public class AuthorIdMap {
	private Map<Integer, Author> map;

	public AuthorIdMap() {
		map = new HashMap<Integer, Author>();
	}
	
	public Author get(Integer id){
		return map.get(id);
	}
	
	public Author put(Author value){
		Author old = map.get(value);
		if(old == null){
			map.put(value.getId(), value);
			return value;
		} else 
			return old;
	}
	
	
}
