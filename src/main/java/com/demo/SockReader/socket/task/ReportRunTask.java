package com.demo.SockReader.socket.task;

import java.util.Collections;
import java.util.Set;

import com.demo.SockReader.socket.cache.DataCacheHandler;
import com.google.common.collect.Sets;

/**
 * This task is generating the report and printing the report to the console.
 * 
 * @author gsharma
 *
 */
public class ReportRunTask implements Runnable {

	private Set<String> previousNumbers;
	private DataCacheHandler dataCacheHandler;

	public ReportRunTask(DataCacheHandler dataCacheHandler) {
		super();
		this.previousNumbers = Collections.emptySet();
		this.dataCacheHandler = dataCacheHandler;
	}

	@Override
	public void run() {

		Set<String> numbers = this.dataCacheHandler.getNumberSet();

		int duplicateValues = Sets.intersection(numbers, previousNumbers).size();
		int uniqueValues = Sets.difference(numbers, previousNumbers).size();
		int totalUnique = Sets.union(numbers, previousNumbers).size();

		this.previousNumbers = Sets.union(numbers, previousNumbers);

		String report = String.format("Received %d unique numbers, %d duplicates. Unique total: %d", uniqueValues,
				duplicateValues, totalUnique);

		System.out.println(report);

	}

}
