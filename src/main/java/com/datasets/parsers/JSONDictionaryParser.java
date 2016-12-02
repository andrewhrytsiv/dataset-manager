package com.datasets.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasets.filemodels.DictionaryJsonFileModel;
import com.entity.Dictionary;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParser;

public class JSONDictionaryParser extends Parser<DictionaryJsonFileModel>{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(JSONDictionaryParser.class);
	
	private DictionaryJsonFileModel fileModel;
	
	@Override
	public String read(InputStream fileStream) throws Exception {
		String message = null;
		String jsonFile = CharStreams.toString(new InputStreamReader(fileStream, Charsets.UTF_8));
		JsonParser jsonParser = new JsonParser();
		JsonArray dictionaryArray = jsonParser.parse(jsonFile).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("list");
		fileModel = new DictionaryJsonFileModel(dictionaryArray);
		return message;
	}

	@Override
	public String read(File file) throws Exception {
		String message = null;
		String jsonFile = CharStreams.toString(new InputStreamReader( new FileInputStream(file), Charsets.UTF_8));
		JsonParser jsonParser = new JsonParser();
		JsonArray datasetArray = jsonParser.parse(jsonFile).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("list");
		fileModel = new DictionaryJsonFileModel(datasetArray);
		return message;
	}
	
	public List<Dictionary> parseDictionary() {
		List<Dictionary> dictionaryList = Lists.newArrayList();
		for(JsonElement element : fileModel.getDictionaryList()){
			try{
				String key = element.getAsJsonObject().get("key").getAsString();
				JsonElement typeElement= element.getAsJsonObject().get("type");
				String type = (typeElement instanceof JsonNull)? null : typeElement.getAsString();
				Dictionary dictionary = new Dictionary();
				dictionary.setKey(key);
				dictionary.setType(type);
				dictionary.setDictionaryJson(element.toString());
				dictionaryList.add(dictionary);
			}catch(Exception ex){
				LOGGER.error("Dictionary parse error:", ex);
			}
		}
		return dictionaryList;
	}

}
