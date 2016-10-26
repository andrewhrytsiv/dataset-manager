package com.datasets.json;

import java.util.LinkedList;
import java.util.Map;

import com.google.common.base.Splitter;

public class JSObject {
	JSNode root;
	
	public JSObject(Map<String, String> keyPathValueMap){
		this.root = new JSNode(null);
		this.root.setRoot(true);
		keyPathValueMap.entrySet().forEach( entry -> {
			addPath(entry.getKey(), entry.getValue());
		});
	}
	
	public void addPath(String keyPath, String value){
		LinkedList<String> keyPathList = new LinkedList<>( Splitter.on(".").splitToList(keyPath));
		root.addPath(keyPathList, value);
	}
	
	public String getAttributeValueByPath(String path){
		return root.getValue(new LinkedList<>(Splitter.on(".").splitToList(path)));
	}
	
	public JSNode getNodeByPath(String keyPath){
		LinkedList<String> keyPathList = new LinkedList<>( Splitter.on(".").splitToList(keyPath));
		return root.getNode(keyPathList);
	}
	
	public String toString(){
		return root.toString();
	}
}
