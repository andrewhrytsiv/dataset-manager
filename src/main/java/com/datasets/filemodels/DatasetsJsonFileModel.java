package com.datasets.filemodels;

import com.google.gson.JsonArray;

public class DatasetsJsonFileModel {
	
	public static String DATASET_DOWNLOAD_API = "/api/export/datasets/";
	
	
	private final JsonArray datasetList;
	
	private final String sourceHost;

	public DatasetsJsonFileModel(String sourceHost, JsonArray datasetList) {
		this.sourceHost = sourceHost;
		this.datasetList = datasetList;
	}

	public JsonArray getDatasetList() {
		return datasetList;
	}

	public String getSourceHost() {
		return sourceHost;
	}
	
	public String getSourceHostDownloadLink(){
		return sourceHost + DATASET_DOWNLOAD_API;
	}

}
