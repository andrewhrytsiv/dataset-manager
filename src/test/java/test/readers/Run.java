package test.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.datasets.parsers.JSONParser;
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
		File file  = new File("C:\\Users\\Andrew\\Downloads\\dj-dps.herokuapp.com_export_Dataset_2016_11_23_11_23_27.json");
		JSONParser parser  = new JSONParser();
		parser.read(new FileInputStream(file));
		parser.parseDatasets();
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

