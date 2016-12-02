package com.datasets.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasets.filemodels.XLSXFileModel;
import com.datasets.json.JSNode;
import com.datasets.json.JSObject;
import com.datasets.query.Query;
import com.datasets.query.RowData;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

public class XLSXParser extends Parser<XLSXFileModel>{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(XLSXParser.class);
	
	private static final String DATASET_ID = "dataset.id";
	private static final String DATASET_COMMIT_NOTE = "dataset.commit.note";
	private static final String DATASET_LABEL = "dataset.label";
	private static final String DATASET_NOTE = "dataset.note";
	private static final String DATASET_SOURCE = "dataset.source";
	private static final String DATASET_TOPICS = "dataset.topics[]";
	private static final String DIMENSION = "dimension";
	private static final String LABEL = "label";
	private static final String ROLE = "role";
	private static final String LAYOUT = "layout";
	private static final String ID = "id";
	private static final String VALUE = "value";
	
	private static List<String> ALL_COLUMNS = ImmutableList.of();
	
	public static String DATA_SHEET = "data";
	public static String METADATA_SHEET = "metadata";
	public static String DICTIONARY_SHEET = "dictionary";
	public static String I18N_SHEET = "i18n";
	
	private XLSXFileModel fileModel = new XLSXFileModel();
	private String datasetId;
	private List<String> warningsList;
	private JSObject product = new JSObject();
	
	public XLSXFileModel getFileModel(){
		return fileModel;
	}
	
	@Override
	public String read(InputStream xlsxFile) throws Exception {
		String errorMessage = null;
		warningsList = Lists.newArrayList();
		try (XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile)) {
			readMetadata(workbook.getSheet(METADATA_SHEET));
			readData(workbook.getSheet(DATA_SHEET));
			readDictionary(workbook.getSheet(DICTIONARY_SHEET));
			readI18N(workbook.getSheet(I18N_SHEET));
			validate();
		}catch(ParseValidationError error){
			errorMessage = error.getMessage();
		}finally {
			xlsxFile.close();
		}
		return errorMessage;
	}
	
	@Override
	public String read(File file) throws Exception {
		String errorMessage = null;
		warningsList = Lists.newArrayList();
		try (FileInputStream xlsxFile = new FileInputStream(file); XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile)) {
			readMetadata(workbook.getSheet(METADATA_SHEET));
			readData(workbook.getSheet(DATA_SHEET));
			readDictionary(workbook.getSheet(DICTIONARY_SHEET));
			readI18N(workbook.getSheet(I18N_SHEET));
			validate();
		}catch(ParseValidationError error){
			errorMessage = error.getMessage();
		}
		return errorMessage;
	}
	
	public List<String> getWarnings(){
		return warningsList;
	}
	
	private void readMetadata(XSSFSheet metadataSheet) throws ParseValidationError{
		if(metadataSheet == null){
			throw new ParseValidationError("Cannot find metadata. Metadata sheet must be named 'metadata'");
		}
		Map<String,String> metadataModel = fileModel.getMetaData();
		Iterator<Row> rowIter = metadataSheet.rowIterator();
		Row headerRow = rowIter.next();		
		int keyColumn = headerRow.getFirstCellNum();
		int valueColumn = headerRow.getLastCellNum() - 1;
		while(rowIter.hasNext()){
			Row row = (Row) rowIter.next();			
			metadataModel.put(row.getCell(keyColumn).toString(), Objects.toString(row.getCell(valueColumn), ""));
		}
	}

	private void readData(XSSFSheet dataSheet){
		readFromTable(dataSheet, fileModel.getData());
	}
	
	private void readDictionary(XSSFSheet dictionarySheet){
		readFromTable(dictionarySheet, fileModel.getDictionary());
	}
	
	private void readI18N(XSSFSheet i18nSheet){
		readFromTable(i18nSheet, fileModel.getI18N());
	}

	private void readFromTable(XSSFSheet sourceTable, Table<Integer, String, String> modelTable) {
		Iterator<Row> rowIter = sourceTable.rowIterator();
		Row headerRow = rowIter.next();
		int start = headerRow.getFirstCellNum();
		int end = headerRow.getLastCellNum() - 1;
		Map<Integer,String> columIndexNameMap = getHeaderColumnIndexNameMap(headerRow, start, end);
		while(rowIter.hasNext()){
			Row row = (Row) rowIter.next();
			for(int colIndex=start; colIndex<=end; colIndex++){
				modelTable.put(row.getRowNum(), columIndexNameMap.get(colIndex), JSNode.convertDoubleQuoteToSingleIfExist(Objects.toString(row.getCell(colIndex), "")));
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

	private void validate() throws ParseValidationError{
		List<String> warnings = Lists.newArrayList();
		Map<String,String> metaData = fileModel.getMetaData();
		if(keyNotExistOrValueEmpty(metaData, DATASET_ID)){
			ParseValidationError error = new ParseValidationError("Cannot find metadata.dataset.id. Create and download new dataset with dataset manager'");
			LOGGER.error(error.getMessage());
			throw error;
		}else{
			datasetId = metaData.get(DATASET_ID);
		}
		if(keyNotExistOrValueEmpty(metaData, DATASET_COMMIT_NOTE)){
			warnings.add("Metadata.dataset.commit.note is empty");
		}
		if(keyNotExistOrValueEmpty(metaData, DATASET_LABEL)){
			warnings.add("Dataset.label is empty");
		}
		if(keyNotExistOrValueEmpty(metaData, DATASET_NOTE)){
			warnings.add("Dataset.note is empty");
		}
		if(keyNotExistOrValueEmpty(metaData, DATASET_SOURCE)){
			warnings.add("Dataset.source is empty");
		}
		if(keyNotExistOrValueEmpty(metaData, DATASET_TOPICS)){
			warnings.add("List of topics is empty");
		}
		JSObject metadataJO = new JSObject(metaData);
		JSNode dimensionNode = metadataJO.getNodeByPath(DIMENSION);
		if(dimensionNode == null){
			ParseValidationError error = new ParseValidationError("Cannot find dataset dimension.");
			LOGGER.error(error.getMessage());
			throw error;
		}
		for(JSNode dimChild : dimensionNode.childNodes()){
			if(!dimChild.getKeyValueMap().containsKey(LABEL) || dimChild.getKeyValueMap().get(LABEL).isEmpty()){
				ParseValidationError error = new ParseValidationError("Cannot find dimension."+ dimChild.key() +".label");
				LOGGER.error(error.getMessage());
				throw error;
			}
			if(!dimChild.getKeyValueMap().containsKey(ROLE) || dimChild.getKeyValueMap().get(ROLE).isEmpty()){
				warnings.add("Cannot find dimension."+ dimChild.key() +".role");
			}
		}
		JSNode layoutNode = metadataJO.getNodeByPath(LAYOUT);
		if(layoutNode == null){
			throw new ParseValidationError("Cannot find metadata.layout");
		}
		//B.A. implementation : metadata.layout.sheet contains data sheet name
		//In this case data sheet name should be "data"
		List<String> dimKeyList = dimensionNode.childNodes().stream().map(node -> node.key()).collect(Collectors.toList());
		for(String dimKey : dimKeyList){
			try{
				if(!layoutNode.getNode(dimKey).getKeyValueMap().containsKey(LABEL) || layoutNode.getNode(dimKey).getKeyValueMap().get(LABEL).isEmpty()){
					throw new Exception();
				}
			}catch(Exception ex){
				ParseValidationError error = new ParseValidationError("Cannot find layout."+ dimKey +".label. It must be refered to data sheet column");
				LOGGER.error(error.getMessage());
				throw error;
			}
			try{
				if(!layoutNode.getNode(dimKey).getKeyValueMap().containsKey(ID) || layoutNode.getNode(dimKey).getKeyValueMap().get(ID).isEmpty()){
					throw new Exception();
				}
			}catch(Exception ex){
				warnings.add("Cannot find layout."+ dimKey +".id. It must be refered to data sheet column");
			}
		}
		
		if(keyNotExistOrValueEmpty(layoutNode.getKeyValueMap(), VALUE)){
			ParseValidationError error = new ParseValidationError("Cannot find layout.value. It must be refered to data sheet column");
			LOGGER.error(error.getMessage());
			throw error;
		}
		//B.A. validate hear undefined values(I do when read file)
		Table<Integer,String,String> data = fileModel.getData();
		for(String dimKey : dimKeyList){
			String labelColumnName = layoutNode.getNode(dimKey).getKeyValueMap().get(LABEL);
			String idColumnName = layoutNode.getNode(dimKey).getKeyValueMap().get(ID);
			for(Map.Entry<Integer, Map<String, String>> row  : data.rowMap().entrySet()){
				Integer rowIndex = row.getKey();
				Map<String, String> rowValues = row.getValue();
				if(Strings.isNullOrEmpty(rowValues.get(labelColumnName))){
					ParseValidationError error = new ParseValidationError("Sheet data row "+rowIndex+" has undefined value in column '"+labelColumnName+"'");
					LOGGER.error(error.getMessage());
					throw error;
				}
				if(Strings.isNullOrEmpty(rowValues.get(idColumnName))){
					ParseValidationError error = new ParseValidationError("Sheet data row "+rowIndex+" has undefined value in column '"+idColumnName+"'");
					LOGGER.error(error.getMessage());
					throw error;
				}
			}
		}
		String valueColumnName = layoutNode.getKeyValueMap().get(VALUE);
		for(Map.Entry<Integer, Map<String, String>> row  : data.rowMap().entrySet()){
			Integer rowIndex = row.getKey();
			Map<String, String> rowValues = row.getValue();
			if(Strings.isNullOrEmpty(rowValues.get(valueColumnName))){
				warnings.add("Sheet data row "+rowIndex + " has undefined value in column '"+valueColumnName+"'");
			}
		}
		Table<Integer,String,String> dictionary = fileModel.getDictionary();
		for(Map.Entry<Integer, Map<String, String>> row  : dictionary.rowMap().entrySet()){
			Integer rowIndex = row.getKey();
			Map<String, String> rowValues = row.getValue();
			if(Strings.isNullOrEmpty(rowValues.get("key"))){
				ParseValidationError error = new ParseValidationError("Sheet 'dictionary' row " + rowIndex +" has undefined value in column 'key'");
				LOGGER.error(error.getMessage());
				throw error;
			}
			// if(!dict[i]["value.label"])
		    //   return {error:"Sheet 'dictionary' row "+i +" has undefined value in column 'value.label'"} 
		}
		Table<Integer,String,String> i18n = fileModel.getI18N();
		for(Map.Entry<Integer, Map<String, String>> row  : i18n.rowMap().entrySet()){
			Integer rowIndex = row.getKey();
			Map<String, String> rowValues = row.getValue();
			if(Strings.isNullOrEmpty(rowValues.get("key"))){
				ParseValidationError error = new ParseValidationError("Sheet 'i18n' row " + rowIndex +" has undefined value in column 'key'");
				LOGGER.error(error.getMessage());
				throw error;
			}
		}
		
		warnings.addAll(warningsList);
		warningsList =  warnings;
	}
	
	private boolean keyNotExistOrValueEmpty(Map<String,String> metaData, Object key){
		return !metaData.containsKey(key) || Strings.isNullOrEmpty(metaData.get(key));
	}
	
	public String buildJson(){
		 List<String> warns = warningsList.stream().map(warnMessge -> JSNode.wrap(warnMessge)).collect(Collectors.toList());
		 product.addPath("validation"+JSObject.ARRAY_WRAPPED,Joiner.on(",").join(warns));
		 product.addPath("metadata"+JSObject.OBJECT, metadata());
		 product.addPath("data"+JSObject.ARRAY_WRAPPED, data());
		 product.addPath("dictionary"+JSObject.ARRAY_WRAPPED, dictionary());
		 return product.toString();
	}
	
	public String getDatasetId(){
		return datasetId;
	}
	
	private String metadata(){
		Map<String,String> metaData = fileModel.getMetaData();
		JSObject metadataJO = new JSObject(metaData);
		JSNode dimensionNode = metadataJO.getNodeByPath(DIMENSION);
		JSNode layoutNode = metadataJO.getNodeByPath(LAYOUT);
		Table<Integer,String,String> dataModel = fileModel.getData();
		for(JSNode dimChild : dimensionNode.childNodes()){
			String idColumnName = layoutNode.getNode(dimChild.key()).getKeyValueMap().get(ID);
			String labelColumnName = layoutNode.getNode(dimChild.key()).getKeyValueMap().get(LABEL);
			List<RowData> result = new Query<RowData>() {
				
				public Query<RowData> map(List<String> columns) {
					String idName = columns.get(0);
					String labelName = columns.get(1);
					table.rowMap().entrySet().stream().forEach(row -> {
						Map<String, String> rowValues = row.getValue();
						RowData mapedValue = new RowData().add(ID, rowValues.get(idName)).add(LABEL, rowValues.get(labelName));
						result.add(mapedValue);
					});
					return this;
				}
			}
			.from(dataModel)
			.map(Lists.newArrayList(idColumnName, labelColumnName))
			.distinct()
			.orderBy(true)
			.get();
//			Ask Boldak if we need add values in metadata search
//			metaData.put(DIMENSION+"."+dimChild+"."+"values[]", Joiner.on(",").join(result));
			dimChild.addPath("values"+JSObject.ARRAY_WRAPPED, Joiner.on(",").join(result));
		}
		return metadataJO.toString();
	}
	
	private String data(){
		JSObject metadataJO = new JSObject(fileModel.getMetaData());
		JSNode dimensionNode = metadataJO.getNodeByPath(DIMENSION);
		JSNode layoutNode = metadataJO.getNodeByPath(LAYOUT);
		Table<Integer,String,String> dataModel = fileModel.getData();
		List<RowData> result = new Query<RowData>() {
			
			public Query<RowData> map(List<String> columns) {
				table.rowMap().entrySet().stream().forEach(row -> {
					Map<String, String> rowValues = row.getValue();
					String valueColumnName = layoutNode.getKeyValueMap().get(VALUE);
					RowData mapedValue = new RowData();
					for(JSNode dimChild : dimensionNode.childNodes()){
						String labelColumnName = layoutNode.getNode(dimChild.key()).getKeyValueMap().get(LABEL);
						String idColumnName = layoutNode.getNode(dimChild.key()).getKeyValueMap().get(ID);
						mapedValue.add(dimChild.key(), rowValues.get(labelColumnName));
						mapedValue.add("#"+dimChild.key(), rowValues.get(idColumnName));
					}
					mapedValue.add("#"+VALUE, rowValues.get(valueColumnName));
					result.add(mapedValue);
				});
				return this;
			}
		}
		.from(dataModel)
		.map(ALL_COLUMNS)
		.get();
		String dictionaryJsonArrayValues = Joiner.on(",").join(result);
		return dictionaryJsonArrayValues;
	}
	
	private String dictionary(){
		Table<Integer,String,String> dictionary = fileModel.getDictionary();
		List<String> result = new Query<String>() {
			
			public Query<String> map(List<String> columns) {
				table.rowMap().entrySet().stream().forEach(row -> {
					JSObject pathJson = new JSObject();
					row.getValue().entrySet().stream().forEach( column -> {
						pathJson.addPath(column.getKey(), column.getValue());
					});
					result.add(pathJson.toString());
				});
				return this;
			}
		}
		.from(dictionary)
		.map(ALL_COLUMNS)
		.get();
		result.addAll(i18n());
		String dictionaryJsonArrayValues = Joiner.on(",").join(result);
		return dictionaryJsonArrayValues;
	}
	
	private List<String> i18n(){
		Table<Integer,String,String> i18n = fileModel.getI18N();
		List<String> result = new Query<String>() {
			
			public Query<String> map(List<String> columns) {
				table.rowMap().entrySet().stream().forEach(row -> {
					JSObject pathJson = new JSObject();
					row.getValue().entrySet().stream().forEach( column -> {
						pathJson.addPath(column.getKey(), column.getValue());
					});
					result.add(pathJson.toString());
				});
				return this;
			}
		}
		.from(i18n)
		.map(ALL_COLUMNS)
		.get();
		return result;
	}
}
