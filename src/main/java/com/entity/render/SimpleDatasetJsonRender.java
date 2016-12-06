package com.entity.render;

import com.util.Period;

public class SimpleDatasetJsonRender {
	private String id;
	private String url;
	private Boolean personal;
	private String snapshotDate;
	private Period period;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getPersonal() {
		return personal;
	}
	public void setPersonal(Boolean personal) {
		this.personal = personal;
	}
	public String getSnapshotDate() {
		return snapshotDate;
	}
	public void setSnapshotDate(String snapshotDate) {
		this.snapshotDate = snapshotDate;
	}
	public Period getPeriod() {
		return period;
	}
	public void setPeriod(Period period) {
		this.period = period;
	}
}
