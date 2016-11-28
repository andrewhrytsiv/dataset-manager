package com.entity;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.postgresql.util.PGobject;

public class Dataset {

	public static String TABLE = "datasets";

	private String uuid;
	private String jsonData;
	private String url;
	private boolean personal;
	private LocalDateTime snapshotDate;
	private Integer ownerId;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getJsonData() {
		return jsonData;
	}
	
	public PGobject getPGJson() throws SQLException {
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(jsonData);
		return jsonObject;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public boolean isPersonal() {
		return personal;
	}

	public void setPersonal(boolean personal) {
		this.personal = personal;
	}

	public LocalDateTime getSnapshotDate() {
		return snapshotDate;
	}

	public void setSnapshotDate(LocalDateTime snapshotDate) {
		this.snapshotDate = snapshotDate;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
