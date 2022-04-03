package com.malbolge.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Handles and serves a SocketServer in a thread that create sockets for each connection request it receives.
 */
public class ServerListenerThread extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

	private final int mPort;
	private final String mWebroot;
	private final ServerSocket mServerSocket;

	public ServerListenerThread(int port, String webroot) throws IOException {
		this.mPort = port;
		this.mWebroot = webroot;
		this.mServerSocket = new ServerSocket(port);
	}

	@Override
	public void run() {
		try {
			while (this.mServerSocket.isBound() && !this.mServerSocket.isClosed()) {
				Socket sk = this.mServerSocket.accept();
				LOGGER.info("Connection accepted: " + sk.getInetAddress());
				HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(sk);
				httpConnectionWorkerThread.start();
			}
		} catch (IOException e) {
			LOGGER.error("Problem with setting socket: ", e);
		} finally {
			try {
				this.mServerSocket.close();
			} catch (IOException e) {
				// ignore
			}
		}
	}
}
