package com.datasets.filemodels;

import com.google.gson.JsonArray;

public class DictionaryJsonFileModel {
	
	public static String DICTIONARY_DOWNLOAD_API = "/api/export/dictionary/";
	
	private final JsonArray dictionaryList;
	
	public DictionaryJsonFileModel(JsonArray dictionaryList) {
		this.dictionaryList = dictionaryList;
	}

	public JsonArray getDictionaryList() {
		return dictionaryList;
	}
}
