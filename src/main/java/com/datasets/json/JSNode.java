package com.datasets.json;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Joiner;


public class JSNode {
	JSNode parent;
	Map<String,JSNode> childNodeMap = new LinkedHashMap<>();
	Map<String, String> keyValueMap = new LinkedHashMap<>();
	
	public JSNode(JSNode parent){
		this.parent = parent; 
	}
	
	public void addPath(LinkedList<String> keyPath, String value){
		if(onlyOne(keyPath)){
			keyValueMap.put(keyPath.removeFirst(), value);
		}else{
			JSNode childNode = new JSNode(this);
			String childNodeName = keyPath.removeFirst();
			if(childNodeMap.containsKey(childNodeName)){
				childNodeMap.get(childNodeName).addPath(keyPath, value);
			}else{
				childNodeMap.put(childNodeName, childNode);
				childNode.addPath(keyPath, value);
			}
			
		}
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
