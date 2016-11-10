package com.entity;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.postgresql.util.PGobject;

public class Dataset {

	public static String TABLE = "datasets";

	private UUID uuid;
	private String jsonData;
	private boolean personal;
	private LocalDateTime snapshotDate;
	private String owner;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
