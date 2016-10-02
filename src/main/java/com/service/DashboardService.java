package com.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datasets.filemodels.xlsx.XLSXFileModel;
import com.datasets.parsers.XLSXParser;
import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.resource.DashboardResource;

public class DashboardService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DashboardService.class);
	
	public boolean insertDataset(InputStream fileInStream, String type){
		XLSXParser parser = new XLSXParser();
		try (ByteArrayInputStream input = new ByteArrayInputStream(ByteStreams.toByteArray(fileInStream))){
//			 byte[] buffer = new byte[fileInStream.available()];
//			 fileInStream.read(buffer);
//			 File targetFile = new File("D:/AndrewWorkSpace/workspace_neon/dataset-manager/src/main/resources/"+"my_data."+type);
//			 Files.write(buffer, targetFile);
			 
			
			XLSXFileModel model = parser.read(input);
			System.out.println(model);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}finally {
			Closeables.closeQuietly(fileInStream);
		}
		return true;
	}
}
