package com.demo.SockReader;

import com.demo.SockReader.worker.ApplicationServer;

/**
 * This is the class that bootstrap the server.
 *
 */
public class ApplicationServerStarter {
	public static void main(String[] args) {
		ApplicationServer applicationServer = new ApplicationServer(4000);
		applicationServer.init();
	}
}
