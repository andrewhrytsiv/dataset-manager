package com.datasets.parsers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

public abstract class Parser<T> {
	
	public abstract String read(InputStream file) throws Exception;
	
	public abstract String read(File file) throws Exception;
	
	public abstract String read(URI file);
	
	public abstract List<String> getWarnings();
	
	public abstract String buildJson();
	
}
