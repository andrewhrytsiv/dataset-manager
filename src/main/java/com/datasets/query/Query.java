package com.datasets.query;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;

public abstract class Query<T extends Comparable<T>> {
	
	protected Table<Integer,String,String> table;
	protected List<T> result = Lists.newArrayList();
	
	public Query<T> from(Table<Integer,String,String> fromTable){
		table = fromTable;
		return this;
	}
	
	public abstract Query<T> map(List<String> columns);
	
	public Query<T> filter(Predicate<T> filter){
		List<T> target = result.stream().filter(filter).collect(Collectors.toList());
		result = target;
		return this;
	} 
	
	public Query<T> distinct(){
		result = Lists.newArrayList(new LinkedHashSet<T>(result));
		return this;
	}
	
	public Query<T> orderBy(boolean ascending){
		if(ascending){
			Collections.sort(result);
		}else{
			Collections.sort(result, Collections.reverseOrder());
		}
		return this;
	}
	
	public List<T> get(){
		return result;
	}
}
