package com.datasets.filemodels;

import com.google.gson.JsonArray;

public class DatasetsJsonFileModel {
	private final JsonArray datasetList;

	public DatasetsJsonFileModel(JsonArray datasetList) {
		this.datasetList = datasetList;
	}

	public JsonArray getDatasetList() {
		return datasetList;
	}

}
