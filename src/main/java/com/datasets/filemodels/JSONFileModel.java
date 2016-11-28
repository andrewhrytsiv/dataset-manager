package com.datasets.filemodels;

import com.google.gson.JsonArray;

public class JSONFileModel {
	private final JsonArray datasetList;

	public JSONFileModel(JsonArray datasetList) {
		this.datasetList = datasetList;
	}

	public JsonArray getDatasetList() {
		return datasetList;
	}

}
