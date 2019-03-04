package com.demo.SockReader.socket.task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class represents the FileWrite Task. As multiple threads(Default 5) are
 * reading the incoming unique numbers from the client, the writing to the
 * underlying file is done only by one thread. The LinkedBlockingQueue is acting
 * as a data channel between the thread generating the number and the thread
 * writing to the log file.
 * 
 * @author gsharma
 *
 */
public class FileWriteTask implements Runnable {

	private LinkedBlockingQueue<String> fileWriteQueue;
	private File dataFile;

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
				writer.write(str + "\n");
				writer.flush();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
