package com.malbolge.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.malbolge.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

	private static ConfigurationManager sInstance;
	private static Configuration mCurrentConfig;

	private ConfigurationManager() { /* Should not be instantiated */}

	public static ConfigurationManager getInstance() {
		if (sInstance == null)
			sInstance = new ConfigurationManager();
		return sInstance;
	}

	/**
	 * Used to load a configuration file by the path provided
	 *
	 * @param filePath the path of the configuration file.
	 */
	public void loadConfigurationFile(String filePath) {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			throw new HttpConfigurationException(e);
		}

		StringBuilder sb = new StringBuilder();

		int i = -1;
		while (true) {
			try {
				if ((i = fileReader.read()) == -1) break;
			} catch (IOException e) {
				throw new HttpConfigurationException(e);
			}
			sb.append((char) i);
		}

		JsonNode config = null;
		try {
			config = Json.parse(sb.toString());
		} catch (JsonProcessingException e) {
			throw new HttpConfigurationException("Error parsing the config Configuration File", e);
		}
		try {
			mCurrentConfig = Json.fromJson(config, Configuration.class);
		} catch (JsonProcessingException e) {
			throw new HttpConfigurationException("Error parsing the Configuration File, internal", e);
		}
	}

	/**
	 * Returns the current loaded configuration.
	 */
	public Configuration getCurrentConfiguration() {
		if (mCurrentConfig == null)
			throw new HttpConfigurationException("No current configuration set.");

		return mCurrentConfig;
	}
}
