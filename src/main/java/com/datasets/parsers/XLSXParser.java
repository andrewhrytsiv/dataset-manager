package com.datasets.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

public class XLSXParser extends Parser<XLSXFileModel>{
	
	public static int DATA_SHEET_INDEX = 0;
	public static int METADATA_SHEET_INDEX = 1;
	public static int DICTIONARY_SHEET_INDEX = 2;
	public static int I18N_SHEET_INDEX = 3;
	
	private XLSXFileModel fileModel = new XLSXFileModel();
	
	@Override
	public XLSXFileModel read(File file) throws Exception {
		try (FileInputStream xlsxFile = new FileInputStream(file); XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile)) {
			readMetadata(workbook.getSheetAt(METADATA_SHEET_INDEX));
			readData(workbook.getSheetAt(DATA_SHEET_INDEX));
			readDictionary(workbook.getSheetAt(DICTIONARY_SHEET_INDEX));
			readI18N(workbook.getSheetAt(I18N_SHEET_INDEX));
		}
		return fileModel;
	}

	@Override
	public XLSXFileModel read(URI file) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void readMetadata(XSSFSheet metadataSheet){
		Map<String,String> metadataModel = fileModel.getMetaData();
		Iterator<Row> rowIter = metadataSheet.rowIterator();
		Row headerRow = rowIter.next();		
		int keyColumn = headerRow.getFirstCellNum();
		int valueColumn = headerRow.getLastCellNum() - 1;
		while(rowIter.hasNext()){
			Row row = (Row) rowIter.next();			
			metadataModel.put(row.getCell(keyColumn).toString(), row.getCell(valueColumn).toString());
		}
	}
	
	private void readData(XSSFSheet dataSheet){
		Table<Integer,String,String> dataModel = fileModel.getData();
		Iterator<Row> rowIter = dataSheet.rowIterator();
		Row headerRow = rowIter.next();		
		int start = headerRow.getFirstCellNum();
		int end = headerRow.getLastCellNum() - 1;
		Map<Integer,String> columIndexNameMap = getHeaderColumnIndexNameMap(headerRow, start, end);
		while(rowIter.hasNext()){
			Row row = (Row) rowIter.next();
			for(int colIndex=start; colIndex<=end; colIndex++){
				dataModel.put(row.getRowNum(), columIndexNameMap.get(colIndex), Objects.toString(row.getCell(colIndex), ""));
			}
		}
	}
	
	private void readDictionary(XSSFSheet dictionarySheet){
		Table<String,String,String> dictionaryModel = fileModel.getDictionary();
		Iterator<Row> rowIter = dictionarySheet.rowIterator();
		Row headerRow = rowIter.next();		
		int start = headerRow.getFirstCellNum();
		int end = headerRow.getLastCellNum() - 1;
		Map<Integer,String> columIndexNameMap = getHeaderColumnIndexNameMap(headerRow, start, end);
		while(rowIter.hasNext()){
			Row row = (Row) rowIter.next();
			String keyValue = row.getCell(start).toString();
			for(int colIndex=start+1; colIndex<=end; colIndex++){
				dictionaryModel.put(keyValue, columIndexNameMap.get(colIndex), Objects.toString(row.getCell(colIndex), ""));
			}
		}
	}
	
	private void readI18N(XSSFSheet i18nSheet){
		Table<String,String,String> i18nModel = fileModel.getI18N();
		Iterator<Row> rowIter = i18nSheet.rowIterator();
		Row headerRow = rowIter.next();
		int start = headerRow.getFirstCellNum();
		int end = headerRow.getLastCellNum() - 1;
		Map<Integer,String> columIndexNameMap = getHeaderColumnIndexNameMap(headerRow, start, end);
		while(rowIter.hasNext()){
			Row row = (Row) rowIter.next();
			String keyValue = row.getCell(start).toString();
			for(int colIndex=start+1; colIndex<=end; colIndex++){
				i18nModel.put(keyValue, columIndexNameMap.get(colIndex), Objects.toString(row.getCell(colIndex), ""));
			}
		}
	} 

	private Map<Integer, String> getHeaderColumnIndexNameMap(Row headerRow, int startColumn, int endColumn) {
		Map<Integer,String> columIndexNameMap = Maps.newLinkedHashMap();
		for(int column=startColumn; column<=endColumn;  column++){
			columIndexNameMap.put(column, headerRow.getCell(column).toString());
		}
		return columIndexNameMap;
	}

}
