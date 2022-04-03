package com.malbolge.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {

	private static final ObjectMapper mObjectMapper = defaultObjectMapper();

	private static ObjectMapper defaultObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return om;
	}

	public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
		return mObjectMapper.readTree(jsonSrc);
	}

	public static <T> T fromJson(JsonNode node, Class<T> clazz) throws JsonProcessingException {
		return mObjectMapper.treeToValue(node, clazz);
	}

	public static JsonNode toJson(Object obj) {
		return mObjectMapper.valueToTree(obj);
	}

	public static String stringify(JsonNode node) throws JsonProcessingException {
		return generateJson(node, false);
	}

	public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
		return generateJson(node, true);
	}

	private static String generateJson(Object o, boolean pretty) throws JsonProcessingException {
		ObjectWriter objectWriter = mObjectMapper.writer();
		if (pretty)
			objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
		return objectWriter.writeValueAsString(o);
	}

}
