package com.datasets.filemodels;

import com.google.gson.JsonArray;

public class DictionaryJsonFileModel {
	
	private final JsonArray dictionaryList;
	
	public DictionaryJsonFileModel(JsonArray dictionaryList) {
		this.dictionaryList = dictionaryList;
	}

	public JsonArray getDictionaryList() {
		return dictionaryList;
	}
}
