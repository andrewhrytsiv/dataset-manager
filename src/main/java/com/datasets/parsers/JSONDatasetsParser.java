package com.datasets.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.datasets.filemodels.DatasetsJsonFileModel;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.util.Pair;

public class JSONDatasetsParser extends Parser<DatasetsJsonFileModel>{
	
	private DatasetsJsonFileModel fileModel;

	@Override
	public String read(InputStream fileStream) throws Exception {
		String message = null;
		String jsonFile = CharStreams.toString(new InputStreamReader(fileStream, Charsets.UTF_8));
		JsonParser jsonParser = new JsonParser();
		JsonObject dataObject = jsonParser.parse(jsonFile).getAsJsonObject().get("data").getAsJsonObject();
		String sourceHost = dataObject.get("source").getAsString();
		JsonArray datasetArray = dataObject.getAsJsonArray("list");
		fileModel = new DatasetsJsonFileModel(sourceHost, datasetArray);
		return message;
	}

	@Override
	public String read(File file) throws Exception {
		String message = null;
		String jsonFile = CharStreams.toString(new InputStreamReader( new FileInputStream(file), Charsets.UTF_8));
		JsonParser jsonParser = new JsonParser();
		JsonObject dataObject = jsonParser.parse(jsonFile).getAsJsonObject().get("data").getAsJsonObject();
		String sourceHost = dataObject.get("source").getAsString();
		JsonArray datasetArray = dataObject.getAsJsonArray("list");
		fileModel = new DatasetsJsonFileModel(sourceHost, datasetArray);
		return message;
	}

	public DatasetsJsonFileModel getFileModel() {
		return fileModel;
	}
	
	public List<Pair<String, LinkedHashMap<String, String>>> parseDatasetsWithMetadata() throws ScriptException, NoSuchMethodException, IOException{
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
	
	@SuppressWarnings("unused")
	public String parseDataset() throws ScriptException, NoSuchMethodException, IOException{
		List<Pair<String, LinkedHashMap<String, String>>> datasetsList = Lists.newArrayList();
		Resource resource = new ClassPathResource("wdc-flat.js");
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader(resource.getFile()));
		Invocable invocable = (Invocable) engine;
		JsonArray array = fileModel.getDatasetList();
		return array.get(0).toString();
	}
}
