package com.malbolge.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Handle HTTP Connections created by the {@link ServerListenerThread} class.
 * <p>
 * This class handles the communication through the socket created for a specific connection request.
 */
public class HttpConnectionWorkerThread extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
	private static final String CRLF = "\n\r";

	private final Socket mSocket;

	public HttpConnectionWorkerThread(Socket socket) {
		this.mSocket = socket;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			inputStream = this.mSocket.getInputStream();
			outputStream = this.mSocket.getOutputStream();

			final String html = "<html><head><title>Simple Java HTTP Server</title><body><h1>" +
					"This is a Test Page</h1></body></head></html>";

			final String res =
					"HTTP/1.1 200 OK" + CRLF + // status line : HTTP_VERSION RESPONSE_CODE RESPONSE_MSG;
							"Content-Length: " + html.getBytes().length + CRLF + // HEADER
							CRLF +
							html +
							CRLF + CRLF;

			outputStream.write(res.getBytes());
			LOGGER.info("Connection processing finished.");
		} catch (IOException e) {
			LOGGER.error("Problem with communication: ", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// ignore
				}
			}

			if (this.mSocket != null) {
				try {
					this.mSocket.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}
