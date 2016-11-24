package com.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import com.entity.Dataset;

public class DatasetRowMapper implements RowMapper<Dataset>{

	@Override
	public Dataset mapRow(ResultSet rs, int rowNum) throws SQLException {
		Dataset dataset = new Dataset();
		dataset.setUuid(rs.getString("dataset_id"));
		dataset.setJsonData(rs.getString("json_data"));
		dataset.setUrl(rs.getString("url"));
		dataset.setPersonal(rs.getBoolean("personal"));
		dataset.setSnapshotDate(rs.getTimestamp("snapshot_date").toLocalDateTime());
		dataset.setOwnerId(rs.getInt("owner"));
		return dataset;
	}

}
