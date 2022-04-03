package com.malbolge.httpserver;

import com.malbolge.httpserver.config.Configuration;
import com.malbolge.httpserver.config.ConfigurationManager;
import com.malbolge.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Driver class for the Http Server
 */
public class HttpServer {

	private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

	public static void main(String[] args) {
		LOGGER.info("Server starting...");
		ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
		Configuration c = ConfigurationManager.getInstance().getCurrentConfiguration();
		LOGGER.info("Using port: " + c.getPort());
		LOGGER.info("Using webroot: " + c.getWebroot());

		ServerListenerThread slt = null;
		try {
			slt = new ServerListenerThread(c.getPort(), c.getWebroot());
			slt.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
