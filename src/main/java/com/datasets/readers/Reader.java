package com.datasets.readers;

import java.io.File;
import java.net.URI;

public abstract class Reader<T> {
	
	public abstract T read(File file) throws Exception;
	
	public abstract T read(URI file);
	
}
