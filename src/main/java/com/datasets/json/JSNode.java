package com.datasets.json;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.jdbc.datasource.lookup.IsolationLevelDataSourceRouter;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;


public class JSNode {
	private JSNode parent;
	private String currentNodeKey;
	private boolean root;
	
	private Map<String,JSNode> childNodeMap = new LinkedHashMap<>();
	private Map<String, String> keyValueMap = new LinkedHashMap<>();
	
	public JSNode(JSNode parent){
		this.parent = parent; 
	}
	
	public boolean isRoot(){
		return root;
	}
	
	public void setRoot(boolean isRoot){
		root = isRoot; 
	}
	
	public void addPath(LinkedList<String> keyPath, String value){
		if(onlyOne(keyPath)){
			keyValueMap.put(keyPath.removeFirst(), value);
		}else{
			String childNodeName = keyPath.removeFirst();
			JSNode childNode = new JSNode(this);
			if(childNodeMap.containsKey(childNodeName)){
				childNodeMap.get(childNodeName).addPath(keyPath, value);
			}else{
				childNode.currentNodeKey = childNodeName;
				childNodeMap.put(childNodeName, childNode);
				childNode.addPath(keyPath, value);
			}
		}
	}
	
	public JSNode getNode(LinkedList<String> keyPath){
		if(onlyOne(keyPath)){
			if(childNodeMap.containsKey(keyPath.getFirst())){
				return childNodeMap.get(keyPath.getFirst());
			}else{
				return null;
			}
		}else{
			String childNodeName = keyPath.removeFirst();
			if(childNodeMap.containsKey(childNodeName)){
				return childNodeMap.get(childNodeName).getNode(keyPath);
			}else{
				return null;
			}
		}
	}
	
	public JSNode getNode(String keyPath){
		LinkedList<String> keyPathList = new LinkedList<>( Splitter.on(".").splitToList(keyPath));
		return getNode(keyPathList);
	}
	
	public String key(){
		return currentNodeKey;
	}
	
	public Collection<JSNode> childNodes(){
		return childNodeMap.values();
	}
	
	
	public String getValue(LinkedList<String> keyPath){
		if(onlyOne(keyPath)){
			return keyValueMap.get(keyPath.removeFirst());
		}else{
			String nodeName = keyPath.removeFirst();
			JSNode childNode = childNodeMap.get(nodeName);
			return childNode != null ? childNode.getValue(keyPath) : null;
		}
	}
	
	public boolean containsKey(String key){
		return keyValueMap.containsKey(key);
	}
	
	public Map<String, String> getKeyValueMap(){
		return keyValueMap;
	}
	
	@Override
	public String toString(){
		List<String> atrributeList = keyValueMap.entrySet().stream().map(entry -> entry.getKey()+":'"+entry.getValue()+"'").collect(Collectors.toList());
		List<String> nodeAttrList = childNodeMap.entrySet().stream().map(entry -> entry.getKey()+":{"+entry.getValue().toString()+"}").collect(Collectors.toList());
		atrributeList.addAll(nodeAttrList);
		return Joiner.on(",").join(atrributeList);
	}
	
	public static <T> boolean onlyOne(Collection<T> list){
		return list.size() == 1 ? true : false;
	}
}
