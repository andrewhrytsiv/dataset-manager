package com.datasets.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.datasets.filemodels.JSONFileModel;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.util.Pair;

public class JSONParser extends Parser<JSONFileModel>{
	
	private JSONFileModel fileModel;

	@Override
	public String read(InputStream fileStream) throws Exception {
		String message = null;
		String jsonFile = CharStreams.toString(new InputStreamReader(fileStream, Charsets.UTF_8));
		JsonParser jsonParser = new JsonParser();
		JsonArray datasetArray = jsonParser.parse(jsonFile).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("list");
		fileModel = new JSONFileModel(datasetArray);
		return message;
	}

	@Override
	public String read(File file) throws Exception {
		String message = null;
		String jsonFile = CharStreams.toString(new InputStreamReader( new FileInputStream(file), Charsets.UTF_8));
		JsonParser jsonParser = new JsonParser();
		JsonArray datasetArray = jsonParser.parse(jsonFile).getAsJsonObject().get("data").getAsJsonObject().getAsJsonArray("list");
		fileModel = new JSONFileModel(datasetArray);
		return message;
	}

	@Override
	public String read(URI file) {
		// TODO Auto-generated method stub
		return null;
	}

	public JSONFileModel getFileModel() {
		return fileModel;
	}
	
	public List<Pair<String, LinkedHashMap<String, String>>> parseDatasets() throws ScriptException, NoSuchMethodException, IOException{
		List<Pair<String, LinkedHashMap<String, String>>> datasetsList = Lists.newArrayList();
		Resource resource = new ClassPathResource("wdc-flat.js");
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader(resource.getFile()));
		Invocable invocable = (Invocable) engine;
		JsonArray array = fileModel.getDatasetList();
		for(JsonElement element : array){
			JsonElement metadataJson = element.getAsJsonObject().get("metadata");
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> metadata = (LinkedHashMap<String, String>) invocable.invokeFunction("json2flat", metadataJson.toString());
			datasetsList.add(new Pair<>(element.toString(), metadata));
		}
		return datasetsList;
	} 
}
