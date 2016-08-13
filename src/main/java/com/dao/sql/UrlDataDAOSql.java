package com.dao.sql;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import com.dao.UrlDataDAO;
import com.entity.UrlData;
import com.google.common.collect.Lists;

public class UrlDataDAOSql implements UrlDataDAO{
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert insertUrlData;
	
	@Autowired 
    public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertUrlData = new SimpleJdbcInsert(dataSource).withTableName("url_data").usingGeneratedKeyColumns("id");
    } 
	@Override
	public void insert(UrlData data, byte[] bytes) {
		jdbcTemplate.update("INSERT INTO url_data (url, file, file_type) VALUES (?, ?, ?)",
				new Object[] { data.getUrl(),new SqlLobValue(bytes), data.getType() },
				new int[] { Types.VARCHAR, Types.BLOB, Types.VARCHAR });
	}
	@Override
	public List<UrlData> findAll() {
		List<UrlData> list = Lists.newArrayList();
		List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT id,url,file_type FROM url_data");
		for (Map<String, Object> row : rows) {
			UrlData urlData = new UrlData();
			urlData.setId(((Number)(row.get("id"))).intValue());
			urlData.setUrl((String)row.get("url"));
			urlData.setType((String)row.get("file_type"));
			list.add(urlData);
		}
		return list;
	}
}
