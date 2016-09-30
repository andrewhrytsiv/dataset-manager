package com.datasets.readers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.datasets.filemodels.xlsx.XLSXFileModel;

public class XLSXReader extends Reader<XLSXFileModel>{
	public static int DATA_SHEET_INDEX = 0;
	public static int METADATA_SHEET_INDEX = 1;
	public static int DICTIONARY_SHEET_INDEX = 2;
	public static int I18N_SHEET_INDEX = 3;
	
	@Override
	public XLSXFileModel read(File file) throws Exception {
		FileInputStream xlsxFile = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook (xlsxFile);
		XSSFSheet dataSheet = workbook.getSheetAt(DATA_SHEET_INDEX);
		XSSFSheet metadataSheet = workbook.getSheetAt(METADATA_SHEET_INDEX);
		XSSFSheet dictionarySheet = workbook.getSheetAt(DICTIONARY_SHEET_INDEX);
		XSSFSheet i18nSheet = workbook.getSheetAt(I18N_SHEET_INDEX);
		return new XLSXFileModel();
	}

	@Override
	public XLSXFileModel read(URI file) {
		// TODO Auto-generated method stub
		return null;
	}

}
