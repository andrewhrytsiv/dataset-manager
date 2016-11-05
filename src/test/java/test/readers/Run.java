package test.readers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.datasets.parsers.XLSXParser;
import com.datasets.query.RowData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Run {

	public static void main(String[] args) throws Exception {
		File file  = new File("C:/Users/Andrew/Desktop/wdc_lib.sparsers.wdc-xlsx/data_min.xlsx");
		XLSXParser parser  = new XLSXParser();
		String message = parser.read(file);
		String json = parser.buildJson();
		System.out.println(json);
        
	}

}

