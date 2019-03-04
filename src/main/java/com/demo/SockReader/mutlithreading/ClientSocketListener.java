package com.demo.SockReader.mutlithreading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.demo.SockReader.DataHandler;
import com.demo.SockReader.Disposable;

public class ClientSocketListener implements Runnable {

	private static final String TERMINATE = "terminate";
	private Socket clientSocket;
	private DataHandler dataHandler;
	private Disposable application;

	public ClientSocketListener(Socket clientSocket, DataHandler dataHandler, Disposable application) {
		super();
		this.clientSocket = clientSocket;
		this.dataHandler = dataHandler;
		this.application = application;
	}

	@Override
	public void run() {


			try (
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
				String inputLine = null;
				while(TERMINATE.equalsIgnoreCase(inputLine = in.readLine()))
				
				String inputLine = in.readLine();
				if (TERMINATE.equalsIgnoreCase(inputLine))
					application.dispose();
				else
					dataHandler.handleData(inputLine);

			} catch (IOException e) {
				System.out.println("Exception caught when trying to close server socket.");
				System.out.println(e.getStackTrace());
			}

	}

}
