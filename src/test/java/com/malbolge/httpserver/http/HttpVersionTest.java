package com.malbolge.httpserver.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpVersionTest {

	@Test
	void getBestCompatibleVersionExactMatch() {
		HttpVersion version = null;
		try {
			version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
		} catch (BadHttpVersionException e) {
			e.printStackTrace();
			fail();
		}

		assertNotNull(version);
		assertEquals(version, HttpVersion.HTTP_1_1);
	}

	@Test
	void getBestCompatibleVersionBadFormat() {
		try {
			HttpVersion.getBestCompatibleVersion("httP/1.1");
			fail();
		} catch (BadHttpVersionException ignore) {}
	}

	@Test
	void getBestCompatibleVersionHigherVersion() {
		HttpVersion version = null;
		try {
			version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
			assertNotNull(version);
			assertEquals(version, HttpVersion.HTTP_1_1);
		} catch (BadHttpVersionException ignore) {
			fail();
		}
	}
}
