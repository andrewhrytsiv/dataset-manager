package com.dao.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.dao.DatasetDAO;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;

@Repository
public class DatasetDAOSql implements DatasetDAO{
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
	public Dataset find(UUID datasetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exist(UUID datasetId) {
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
}
