package com.demo.SockReader.worker;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.demo.SockReader.lifecycle.Disposable;
import com.demo.SockReader.lifecycle.Initializable;
import com.demo.SockReader.socket.cache.DataCacheHandler;
import com.demo.SockReader.socket.task.FileWriteTask;

/**
 * This class is responsible for handling everything related to Data coming from
 * Client Socket connections.
 * 
 * @author gsharma
 *
 */
public class DataHandler implements Initializable, Disposable {

	private static final String LOG_FILE_NAME = "numbers.log";

	private ExecutorService dataWriterExecutorService;
	private DataCacheHandler dataCacheHandler;
	private DataReporter dataReporter;

	public void init() {
		try {

			// Create log file and register to the JVM shutdown hook.
			File dataFile = new File(LOG_FILE_NAME);
			dataFile.createNewFile();
			dataFile.deleteOnExit();

			// Queue between the sockets that are sending the numbers and the thread that
			// write unique numbers to the file.
			LinkedBlockingQueue<String> fileWriteQueue = new LinkedBlockingQueue<String>();

			// dataWriter is a separate thread that write to the file.
			FileWriteTask fileWriteTask = new FileWriteTask(fileWriteQueue, dataFile);
			dataWriterExecutorService = Executors.newSingleThreadExecutor();
			dataWriterExecutorService.execute(fileWriteTask);

			// Every number client socket writes are being cached on server side.
			// Only unique number is sent to be written to log file via queue.
			dataCacheHandler = new DataCacheHandler(fileWriteQueue);

			// Start the Data report background thread for reporting.
			dataReporter = new DataReporter(dataCacheHandler);
			dataReporter.init();

		} catch (IOException e) {
			System.out.println("Initialization of DataHandler failed!");
			e.printStackTrace();
		}

	}

	public void handleData(String data) {
		dataCacheHandler.cacheData(data);
	}

	@Override
	public void dispose() {
		if (dataReporter != null) {
			dataReporter.dispose();
		}
		if (dataWriterExecutorService != null) {
			// As data writer is blocked for queue. It will throw Interrupted exception.
			// Below exception is expected.
			// at
			// java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.reportInterruptAfterWait(AbstractQueuedSynchronizer.java:2014)
			dataWriterExecutorService.shutdownNow();
		}
	}

}
