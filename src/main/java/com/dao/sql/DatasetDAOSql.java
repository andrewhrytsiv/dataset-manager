package com.dao.sql;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dao.DatasetDAO;
import com.entity.Dataset;
import com.entity.Dictionary;
import com.entity.MetadataKeyValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
/**
 *  DatasetDAO works with dataset,metadata_key_value,dictionary tables.
 *  Yeah, I know it breaks pattern. But dataset abstraction is complex data definitions based on metadata.
 *  And we use few tables to search  json parts quickly. 
 */
@Repository
public class DatasetDAOSql implements DatasetDAO{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DatasetDAOSql.class);
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
	private PlatformTransactionManager platformTransactionManager;
	
	@Autowired 
    public void setDataSource(DataSource source) {
		this.dataSource = source;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public Dataset find(String datasetId) {
		String sql = "SELECT * FROM datasets WHERE dataset_id = ? ";
		Dataset dataset = (Dataset)jdbcTemplate.queryForObject(sql, new Object[] {datasetId}, new DatasetRowMapper());
		return dataset;
	}
	
	@Override
	public List<Dataset> findByUser(Integer userId) {
		List<Dataset> datasetList = Lists.newArrayList();
		try{
			String sql = "SELECT dataset_id, url, personal,snapshot_date FROM datasets ORDER BY snapshot_date DESC";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			for (Map<String, Object> row : rows) {
				Dataset dataset = new Dataset();
				dataset.setUuid((String)(row.get("dataset_id")));
				dataset.setUrl((String)row.get("url"));
				dataset.setPersonal((Boolean)row.get("personal"));
				Timestamp date = (Timestamp)row.get("snapshot_date");
				dataset.setSnapshotDate(date.toLocalDateTime());
				datasetList.add(dataset);
			}
		}catch(Exception ex){
			LOGGER.error("findByUser retrive failed : "+ex.getMessage(), ex);
		}
		return datasetList;
	}

	@Override
	public boolean exist(String datasetId) {
		String sql = "SELECT COUNT(dataset_id) FROM datasets WHERE dataset_id = ?";
		Integer count = (Integer) jdbcTemplate.queryForObject(sql, new Object[] { datasetId }, Integer.class);
		return count > 0 ? true : false;
	}
	
	@Override
	public void insert(Dataset dataset, MetadataKeyValue metadata) throws DataAccessException, SQLException {	
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status=platformTransactionManager.getTransaction(paramTransactionDefinition );
		try{
			insert(dataset);
			insert(metadata);
		  platformTransactionManager.commit(status);
		}catch (Exception e) {
		  platformTransactionManager.rollback(status);
		  LOGGER.error("Transaction issue: ", e);
		}
	}
	
	private void insert(Dataset dataset) throws DataAccessException, SQLException{
		String sql = "INSERT INTO datasets (dataset_id, json_data, url, personal, snapshot_date, owner) VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] {dataset.getUuid(), dataset.getPGJson(), dataset.getUrl(), dataset.isPersonal(), dataset.getSnapshotDate(), dataset.getOwnerId()});
	}
	
	private void insert(MetadataKeyValue metadata){
		String sql = "INSERT INTO metadata_key_value (key, value, dset_id, table_name) VALUES (?, ?, ?, ?)";
		List<Object[]> parameters = metadata.getKeyValue().entrySet().stream()
				.map(e -> new Object[] {e.getKey(), e.getValue(), metadata.getUuid(), metadata.getTable()})
				.collect(Collectors.toList());
		jdbcTemplate.batchUpdate(sql, parameters);
	}

	@Override
	public void update(Dataset dataset) throws DataAccessException, SQLException {
		String sql = "UPDATE datasets SET  json_data = ?, url = ?, snapshot_date = ?, owner = ? WHERE dataset_id = ?";
		jdbcTemplate.update(sql, new Object[] {dataset.getPGJson(), dataset.getUrl(), dataset.getSnapshotDate(), dataset.getOwnerId(), dataset.getUuid()});
	}

	@Override
	public MetadataKeyValue findMetadata(String datasetId) {
		String sql = "SELECT key, value FROM metadata_key_value WHERE table_name = 'datasets' AND dset_id = ?";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, datasetId);
		MetadataKeyValue metadata = new MetadataKeyValue();
		metadata.setUuid(datasetId);
		for (Map<String, Object> row : rows) {
			String key = (String) row.get("key");
			String value = (String) row.get("value");
			metadata.getKeyValue().put(key, value);
		}
		return metadata;
	}

	@Override
	public List<MetadataKeyValue> findAllMetadata() {
		String sql = "SELECT dset_id, key, value FROM metadata_key_value WHERE table_name = 'datasets' ORDER BY dset_id";
		Map<String, MetadataKeyValue> resultMap = Maps.newLinkedHashMap();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {
			String datasetId = (String) row.get("dset_id");
			String key = (String) row.get("key");
			String value = (String) row.get("value");
			if(resultMap.containsKey(datasetId)){
				MetadataKeyValue metadata = resultMap.get(datasetId);
				metadata.getKeyValue().put(key, value);
			}else{
				MetadataKeyValue metadata = new MetadataKeyValue();
				metadata.setUuid(datasetId);
				metadata.getKeyValue().put(key, value);
				resultMap.put(datasetId, metadata);
			}
		}
		return Lists.newArrayList(resultMap.values());
	}
	
	@Override
	public Dictionary findDictionary(String key){
		String sql = "SELECT * FROM dictionary WHERE key = ? ";
		Dictionary dictionary = (Dictionary)jdbcTemplate.queryForObject(sql, new Object[] {key}, new DictionaryRowMapper());
		return dictionary;
	}
	
	@Override
	public List<Dictionary> findDictionaries(String type){
		String sql = "SELECT * FROM dictionary WHERE type = ? ";
		List<Dictionary> dictionaryList = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, new Object[] {type});
		for (Map<String, Object> row : rows) {
			Dictionary dictionary = new Dictionary();
			dictionary.setKey((String)row.get("key"));
			dictionary.setType((String)row.get("type"));
			dictionary.setDictionaryJson((String)row.get("dictionary_json"));
			dictionaryList.add(dictionary);
		}
		return dictionaryList;
	}
	
	@Override
	public void insert(Dictionary dictionary){
		
	}
	
	@Override
	public void insert(List<Dictionary> dictionaryList){
		
	}
}
