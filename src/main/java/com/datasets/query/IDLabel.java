package com.datasets.query;

import com.google.common.collect.ComparisonChain;

public class IDLabel implements Comparable<IDLabel>{
	String id;
	String label;
	
	public IDLabel(String id, String label) {
		this.id = id;
		this.label = label;
	}
	
	@Override
	public String toString(){
		return "{ id : '"+id+"',label : '"+label+"' }";
	}

	@Override
	public int compareTo(IDLabel otherObject) {
		return ComparisonChain.start()
	      .compare(id, otherObject.id)
	      .compare(label, otherObject.label)
	      .result();
	}
	
}
