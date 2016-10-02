package com.datasets.parsers;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

public abstract class Parser<T> {
	
	public abstract T read(InputStream file) throws Exception;
	
	public abstract T read(File file) throws Exception;
	
	public abstract T read(URI file);
	
}
