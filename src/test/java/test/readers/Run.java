package test.readers;

import java.io.File;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.datasets.parsers.XLSXParser;

public class Run {

	public static void main(String[] args) throws Exception {
		File file  = new File("C:/Users/Andrew/Desktop/wdc_lib.sparsers.wdc-xlsx/data_min.xlsx");
		XLSXParser parser  = new XLSXParser();
		String message = parser.read(file);
		parser.buildJson();
		
	}

}
