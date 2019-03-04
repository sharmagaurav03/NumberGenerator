package com.demo.SockReader;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DataHandler implements Initializable, Disposable {

	private static final String LOG_FILE_NAME = "numbers.log";
	
	private LinkedBlockingQueue<String> fileWriteQueue;
	private ConcurrentHashMap<String, AtomicLong> dataCache;
	
	private File dataFile;
	
	private Thread dataWriter;
	private FileWriteTask fileWriteTask;
	
	private DataCacheHandler dataCacheHandler;

	public DataHandler() {
		fileWriteQueue = new LinkedBlockingQueue<String>();
		dataCache = new ConcurrentHashMap<String, AtomicLong>();
		init();
	}

	public void init() {
		try {
			dataFile = new File(LOG_FILE_NAME);
			dataFile.createNewFile();
			dataFile.deleteOnExit();
		} catch (IOException e) {
			System.out.println("Initialization of DataHandler failed!");
			e.printStackTrace();
		}
		fileWriteTask = new FileWriteTask(fileWriteQueue, dataFile);
		dataWriter = new Thread(fileWriteTask);
		dataWriter.start();
		
		dataCacheHandler = new DataCacheHandler(dataCache, fileWriteQueue);
	}
	
	public void handleData(String data) {
		dataCacheHandler.cacheData(data);
	}


	@Override
	public void dispose() {
		if (dataWriter != null) {
			if (fileWriteTask.isAllDataWrittenToFile()) {
				dataWriter.interrupt();
			}
		}
	}

}
