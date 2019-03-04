package com.demo.SockReader.worker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.demo.SockReader.lifecycle.Disposable;
import com.demo.SockReader.lifecycle.Initializable;
import com.demo.SockReader.socket.cache.DataCacheHandler;
import com.demo.SockReader.socket.task.ReportRunTask;

/**
 * This class is responsible for triggering the report generation. This will do
 * whatever it takes to generate the report every 10 seconds.
 * 
 * @author gsharma
 *
 */
public class DataReporter implements Initializable, Disposable {

	private static final int INITIAL_REPORT_DELAY = 10;

	private static final int REPORT_INTERVAL = 10;

	private DataCacheHandler dataCacheHandler;
	private ScheduledExecutorService scheduledExecutorService;

	public DataReporter(DataCacheHandler dataCacheHandler) {
		this.dataCacheHandler = dataCacheHandler;
	}

	@Override
	public void init() {

		ReportRunTask reportRunTask = new ReportRunTask(dataCacheHandler);
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(reportRunTask, INITIAL_REPORT_DELAY, REPORT_INTERVAL,
				TimeUnit.SECONDS);

	}

	@Override
	public void dispose() {
		scheduledExecutorService.shutdownNow();
	}

}
