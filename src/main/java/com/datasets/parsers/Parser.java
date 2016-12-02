package com.datasets.parsers;

import java.io.File;
import java.io.InputStream;

public abstract class Parser<T> {
	
	public abstract String read(InputStream file) throws Exception;
	
	public abstract String read(File file) throws Exception;
	
}
