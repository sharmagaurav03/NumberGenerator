package com.demo.SockReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class FileWriteTask implements Runnable {

	private LinkedBlockingQueue<String> fileWriteQueue;
	private File dataFile;
	private volatile boolean isAllDataWrittenToFile = true;

	public FileWriteTask(LinkedBlockingQueue<String> fileWriteQueue, File dataFile) {
		super();
		this.fileWriteQueue = fileWriteQueue;
		this.dataFile = dataFile;
	}

	@Override
	public void run() {
		try (FileWriter writer = new FileWriter(dataFile);) {
			while (true) {
				String str = fileWriteQueue.take();
				writer.write(str);
				if (fileWriteQueue.isEmpty()) {
					isAllDataWrittenToFile = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public boolean isAllDataWrittenToFile() {
		return isAllDataWrittenToFile;
	}

}
