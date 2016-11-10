package com.dao.sql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dao.DatasetDAO;
import com.entity.Dataset;
import com.entity.MetadataKeyValue;

@Repository 
public class DatasetDAOSql implements DatasetDAO{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	
	@Autowired 
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	@Transactional
	public void insert(Dataset dataset, MetadataKeyValue metadata) throws DataAccessException, SQLException {	
		insert(dataset);
		insert(metadata);
	}
	
	private void insert(Dataset dataset) throws DataAccessException, SQLException{
		String sql = "INSERT INTO datasets (dataset_id, json_data, personal, snapshot_date, owner) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { dataset.getUuid(), dataset.getPGJson(), dataset.isPersonal(),
				dataset.getSnapshotDate(), dataset.getOwner() });
	}
	
	private void insert(MetadataKeyValue metadata){
		String sql = "INSERT INTO metadata_key_value (key, value, dset_id, table_name) VALUES (?, ?, ?, ?)";
		List<Object[]> parameters = new ArrayList<Object[]>();
		metadata.getKeyValue().entrySet().forEach(entry -> {
			Object[] row = new Object[4];
			row[0] = entry.getKey();
			row[1] = entry.getValue();
			row[2] = metadata.getUuid();
			row[3] = metadata.getTable();
			parameters.add(row);
		});
		jdbcTemplate.batchUpdate(sql, parameters);
	}

	@Override
	public Dataset find(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}
}
