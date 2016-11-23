package test.readers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.datasets.parsers.XLSXParser;
import com.datasets.query.RowData;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Run {

	public static void main(String[] args) throws Exception {
//		File file  = new File("C:\\Users\\Andrew\\Downloads\\1f9aac20-8e35-11e6-9d7b-0d2802a5672e_2016_10(undefined).xlsx");
//		XLSXParser parser  = new XLSXParser();
//		String message = parser.read(file);
//		String json = parser.buildJson();
		String json = "{\"value\":\"My text with 'name' my!\"}";
		System.out.println(getPrettyJsonWithEscapedCharacters(json));
        
	}
	
	
	public static String getPrettyJsonWithEscapedCharacters(String jsonString) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			JsonParser parser = new JsonParser();
			JsonElement jelemen = parser.parse(jsonString);
			String prettyJsonString = gson.toJson(jelemen);
			Files.write(prettyJsonString, new File("C:\\tmp.json"), Charset.forName("UTF-8"));
			return prettyJsonString;
		} catch (Exception ex) {
			return null;
		}
	} 

}

