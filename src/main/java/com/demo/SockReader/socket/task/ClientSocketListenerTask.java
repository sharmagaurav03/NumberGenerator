package com.demo.SockReader.socket.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.demo.SockReader.lifecycle.Disposable;
import com.demo.SockReader.worker.DataHandler;

/**
 * This task represent the process of reading the data from client socket. If
 * the client sends the terminate signals, this task will initiate the gracefull
 * application shutdown process.
 * 
 * @author gsharma
 *
 */
public class ClientSocketListenerTask implements Runnable {

	private static final String TERMINATE = "terminate";
	private Socket clientSocket;
	private DataHandler dataHandler;
	private Disposable application;

	public ClientSocketListenerTask(Socket clientSocket, DataHandler dataHandler, Disposable application) {
		super();
		this.clientSocket = clientSocket;
		this.dataHandler = dataHandler;
		this.application = application;
	}

	@Override
	public void run() {
		boolean isBadData = false;

		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
			String inputLine = null;
			while (!TERMINATE.equalsIgnoreCase(inputLine = in.readLine())) {
				if (inputLine.length() != 9) {
					clientSocket.close();
					isBadData = true;
					break;
				}
				dataHandler.handleData(inputLine);
			}
			if (!isBadData) {
				application.dispose();
			}

		} catch (IOException e) {
			System.out.println("Exception caught when trying to close server socket.");
			System.out.println(e.getStackTrace());
		}

	}

}
