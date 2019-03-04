package com.demo.SockReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.demo.SockReader.mutlithreading.ClientSocketListener;

public class ApplicationServer implements Initializable, Disposable {
	public static void main(String[] args) throws IOException {

		ApplicationServer applicationServer = new ApplicationServer(4000);
		applicationServer.init();

	}

	private int portNumber;
	private ExecutorService clientSocketExecutor;
	private DataHandler dataHandler;

	public ApplicationServer(int port) {
		super();
		this.portNumber = port;
	}

	public void init() {

		try (ServerSocket serverSocket = new ServerSocket(portNumber);

		) {
			dataHandler = new DataHandler();
			dataHandler.init();
			clientSocketExecutor = Executors.newFixedThreadPool(4);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientSocketExecutor.execute(new ClientSocketListener(clientSocket, dataHandler, this));
			}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void dispose() {
		clientSocketExecutor.shutdown();
		try {
			clientSocketExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clientSocketExecutor.shutdownNow();
		dataHandler.dispose();

	}
}