package com.demo.SockReader.socket.cache;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * All the numbers coming from socket are being cached here.
 * This file need to be optimized as mentioned in fix me.
 * @author gsharma
 *
 */

//FIXME: This class is storing double cache. This need to be fixed.
//Double caching is kept to avoid duplicate entries to log file.
public class DataCacheHandler {

	private ConcurrentHashMap<String, Object> newNumbers;
	private ConcurrentHashMap<String, Object> allNumbers;
	private LinkedBlockingQueue<String> fileWriteQueue;

	private final Object value = new Object();

	public DataCacheHandler(LinkedBlockingQueue<String> fileWriteQueue) {
		super();
		this.fileWriteQueue = fileWriteQueue;
		this.newNumbers = new ConcurrentHashMap<String, Object>();
		this.allNumbers = new ConcurrentHashMap<String, Object>();
	}

	public void cacheData(String data) {
		// set the count as 0 for new number
		Object inputMappedValue = newNumbers.putIfAbsent(data, value);
		

		// enqueue the new number to be written in log file
		if (inputMappedValue == null && !allNumbers.containsKey(data)) {
			try {
				fileWriteQueue.put(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Set<String> getNumberSet() {

		ConcurrentHashMap<String, Object> tempNumbers = newNumbers;
		newNumbers = new ConcurrentHashMap<String, Object>();
		allNumbers.putAll(tempNumbers);
		return tempNumbers.keySet();
	}

}
