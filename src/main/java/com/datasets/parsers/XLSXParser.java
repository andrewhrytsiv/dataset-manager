package com.datasets.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.datasets.json.JSNode;
import com.datasets.json.JSObject;
import com.datasets.query.IDLabel;
import com.datasets.query.Query;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;

public class XLSXParser extends Parser<XLSXFileModel>{
	
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
	
	public static String DATA_SHEET = "data";
	public static String METADATA_SHEET = "metadata";
	public static String DICTIONARY_SHEET = "dictionary";
	public static String I18N_SHEET = "i18n";
	
	private XLSXFileModel fileModel = new XLSXFileModel();
	private List<String> warningsList;
	private JSObject metadataJO;
	
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

	@Override
	public String read(URI file) {
		
		return null;
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

	private void validate() throws ParseValidationError{
		List<String> warnings = Lists.newArrayList();
		Map<String,String> metaData = fileModel.getMetaData();
		if(keyNotExistOrValueEmpty(metaData, DATASET_ID)){
			throw new ParseValidationError("Cannot find metadata.dataset.id. Create and download new dataset with dataset manager'");
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
		metadataJO = new JSObject(metaData);
		JSNode dimensionNode = metadataJO.getNodeByPath(DIMENSION);
		if(dimensionNode == null){
			throw new ParseValidationError("Cannot find dataset dimension.");
		}
		for(JSNode dimChild : dimensionNode.childNodes()){
			if(!dimChild.getKeyValueMap().containsKey(LABEL) || dimChild.getKeyValueMap().get(LABEL).isEmpty()){
				throw new ParseValidationError("Cannot find dimension."+ dimChild.key() +".label");
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
				throw new ParseValidationError("Cannot find layout."+ dimKey +".label. It must be refered to data sheet column");
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
			throw new ParseValidationError("Cannot find layout.value. It must be refered to data sheet column");
		}
		//B.A. validate hear undefined values(I do when read file)
		warnings.addAll(warningsList);
		warningsList =  warnings;
	}
	
	private boolean keyNotExistOrValueEmpty(Map<String,String> metaData, Object key){
		return !metaData.containsKey(key) || Strings.isNullOrEmpty(metaData.get(key));
	}
	
	public void buildJson(){
		 metadata();
	}
	
	public void metadata(){
		JSNode dimensionNode = metadataJO.getNodeByPath(DIMENSION);
		JSNode layoutNode = metadataJO.getNodeByPath(LAYOUT);
		Table<Integer,String,String> dataModel = fileModel.getData();
		for(JSNode dimChild : dimensionNode.childNodes()){
			String idColumnName = layoutNode.getNode(dimChild.key()).getKeyValueMap().get(ID);
			String labelColumnName = layoutNode.getNode(dimChild.key()).getKeyValueMap().get(LABEL);
			List<IDLabel> result = new Query<IDLabel>() {
				public Query<IDLabel> map(List<String> columns) {
					String idName = columns.get(0);
					String labelName = columns.get(1);
					table.rowMap().entrySet().stream().forEach(row -> {
						Map<String, String> rowValues = row.getValue();
						IDLabel mapedValue = new IDLabel(rowValues.get(idName), rowValues.get(labelName));
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
			result.forEach(System.out::println);
		}
	}

}
