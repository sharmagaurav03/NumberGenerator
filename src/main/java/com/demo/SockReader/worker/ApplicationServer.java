package com.demo.SockReader.worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.demo.SockReader.lifecycle.Disposable;
import com.demo.SockReader.lifecycle.Initializable;
import com.demo.SockReader.socket.task.ClientSocketListenerTask;

/**
 * This class represent the Application server. This is where the server start
 * listening for client socket connections. If the client sends the terminate
 * signal, this application server will be initiated to be shutdown by
 * ClientSocketListenerTask. The ApplicationServer will shutdown all of its
 * dependencies as part of its clean shutdown.
 * 
 * @author gsharma
 *
 */
public class ApplicationServer implements Initializable, Disposable {

	private static final int MAX_CLIENT_CONNECTION = 5;
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
			clientSocketExecutor = Executors.newFixedThreadPool(MAX_CLIENT_CONNECTION);

			dataHandler = new DataHandler();
			dataHandler.init();

			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientSocketExecutor.execute(new ClientSocketListenerTask(clientSocket, dataHandler, this));
			}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void dispose() {
		dataHandler.dispose();
		clientSocketExecutor.shutdown();
		try {
			clientSocketExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		clientSocketExecutor.shutdownNow();
		System.exit(0);
	}
}