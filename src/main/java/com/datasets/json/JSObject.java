package com.datasets.json;

import java.util.LinkedList;

import com.google.common.base.Splitter;

public class JSObject {
	JSNode root = new JSNode(null);
	
	public void addPath(String keyPath, String value){
		LinkedList<String> pathList = new LinkedList<>( Splitter.on(".").splitToList(keyPath));
		root.addPath(pathList, value);
	}
	
	public String getAttributeValueByPath(String path){
		return root.getValue(new LinkedList<>(Splitter.on(".").splitToList(path)));
	}
	
	public String toString(){
		return root.toString();
	}
}
