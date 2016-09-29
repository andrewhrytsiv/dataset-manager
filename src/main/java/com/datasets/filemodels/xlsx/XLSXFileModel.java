package com.datasets.filemodels.xlsx;

import java.util.Map;

import com.datasets.filemodels.DatasetFileModel;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

public class XLSXFileModel implements DatasetFileModel{
	
	private Map<String,String> metadataModel = Maps.newLinkedHashMap();
	private Table<Integer,String,String> dataModel =  HashBasedTable.create();
	private Table<String,String,String> dictionaryModel =  HashBasedTable.create();
	private Table<String,String,String> i18nModel =  HashBasedTable.create();

	public Map<String,String> getMetaData() {
		return metadataModel;
	}

	public void setMetadataModel(Map<String, String> metadataModel) {
		this.metadataModel = metadataModel;
	}

	public Table<Integer,String,String> getData() {
		return dataModel;
	}
	
	public void setDataModel(Table<Integer, String, String> dataModel) {
		this.dataModel = dataModel;
	}

	public Table<String,String,String> getDictionary() {
		return dictionaryModel;
	}
	
	public void setDictionaryModel(Table<String, String, String> dictionaryModel) {
		this.dictionaryModel = dictionaryModel;
	}
	
	public Table<String,String,String> getI18N() {
		return i18nModel;
	}
	
	public void setI18nModel(Table<String, String, String> i18nModel) {
		this.i18nModel = i18nModel;
	}

}
