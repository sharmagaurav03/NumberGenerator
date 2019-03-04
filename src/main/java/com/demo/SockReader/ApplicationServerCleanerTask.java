package com.demo.SockReader;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ApplicationServerCleanerTask implements Runnable {

	private Thread applicationServerProcessor;
	private CompletionService<Boolean> completionService;
	private ExecutorService clientSocketExecutor;

	public ApplicationServerCleanerTask(Thread applicationServerProcessor, CompletionService<Boolean> completionService,
			ExecutorService clientSocketExecutor) {
		super();
		this.applicationServerProcessor = applicationServerProcessor;
		this.completionService = completionService;
		this.clientSocketExecutor = clientSocketExecutor;
	}

	@Override
	public void run() {
		try {
			Future<Boolean> resultFuture = completionService.take();
			boolean isTerminatedCommand = resultFuture.get();
			if (isTerminatedCommand) {
				applicationServerProcessor.interrupt();
				clientSocketExecutor.shutdownNow();
				// TODO: Add the retry and self interruption login if executor does not ends
				// after a certain number of retry.
				while (!clientSocketExecutor.awaitTermination(10, TimeUnit.MILLISECONDS))
					;
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
