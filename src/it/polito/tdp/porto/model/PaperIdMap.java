package it.polito.tdp.porto.model;

import java.util.*;

public class PaperIdMap {
	private Map<Integer, Paper> map;
	
	public PaperIdMap(){
		map = new HashMap<Integer, Paper>();
	}
	
	public Paper get(Integer id){
		return map.get(id);
	}
	
	public Paper put(Paper value){
		Paper old = map.get(value);
		if(old == null){
			map.put(value.getEprintid(), value);
			return value;
		} else 
			return old;
	}
}
