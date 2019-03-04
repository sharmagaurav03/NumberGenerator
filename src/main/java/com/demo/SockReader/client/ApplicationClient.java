package com.demo.SockReader.client;


import java.io.*;
import java.net.*;

/**
 * This is a sample socket client.
 * @author gsharma
 *
 */
public class ApplicationClient {
	public static void main(String[] args) throws IOException {

		String hostName = "127.0.0.1";
		int portNumber = 4000;

		try (Socket clientSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {

			{
				String numbers = "314159265\n777777777\n007007009\n456000000\n600000078\n890000000\nterminate";

				out.println(numbers);
				out.flush();
				try {
					Thread.sleep(5000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}
}