package com.malbolge.httpserver;

import com.malbolge.httpserver.config.Configuration;
import com.malbolge.httpserver.config.ConfigurationManager;

/**
 * Driver class for the Http Server
 */
public class HttpServer {
	public static void main(String[] args) {
		System.out.println("Server starting...");
		ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
		Configuration c = ConfigurationManager.getInstance().getCurrentConfiguration();
		System.out.println(c.getPort());
		System.out.println(c.getWebroot());
	}
}
