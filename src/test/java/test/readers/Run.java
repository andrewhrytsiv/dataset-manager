package test.readers;

import java.io.File;

import com.datasets.readers.XLSXReader;

public class Run {

	public static void main(String[] args) throws Exception {
		File file  = new File("C:/Users/Andrew/Desktop/wdc_lib.sparsers.wdc-xlsx/data.xlsx");
		XLSXReader reader  = new XLSXReader();
		reader.read(file);
	}

}
