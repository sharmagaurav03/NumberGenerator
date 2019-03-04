package com.demo.SockReader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class DataCacheHandler {

	private ConcurrentHashMap<String, AtomicLong> numbers;
	private LinkedBlockingQueue<String> fileWriteQueue;

	public DataCacheHandler(ConcurrentHashMap<String, AtomicLong> numbers, LinkedBlockingQueue<String> fileWriteQueue) {
		super();
		this.numbers = numbers;
		this.fileWriteQueue = fileWriteQueue;
	}

	public void cacheData(String data) {
		System.out.println("Data to be cached "+ data);

		//set the count as 0 for new number
		AtomicLong inputMappedValue = numbers.computeIfAbsent(data, (input) -> new AtomicLong(0));

		//enqueue the new number to be written in log file
		if (inputMappedValue.longValue() == 0) {
			synchronized (fileWriteQueue) {
				long tempInputMappedValue = numbers.get(data).longValue();
				if (tempInputMappedValue == 0) {
					try {
						fileWriteQueue.put(data);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}
		//increment the encounter count of the number
		numbers.computeIfPresent(data, (key, value) -> new AtomicLong(value.incrementAndGet()));
	}
}
