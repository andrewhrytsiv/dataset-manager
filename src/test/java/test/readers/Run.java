package test.readers;

import java.io.File;

import com.datasets.parsers.XLSXParser;

public class Run {

	public static void main(String[] args) throws Exception {
		File file  = new File("C:\\Users\\user\\Desktop\\wdc_lib.sparsers.wdc-xlsx\\data.xlsx");
		XLSXParser reader  = new XLSXParser();
		reader.read(file);
	}

}
