package com.malbolge.httpserver.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
	private HttpParser httpParser;

	@BeforeAll
	public void beforeClass() {
		httpParser = new HttpParser();
	}

	@Test
	void parseHttpRequest() {
		HttpRequest request = null;
		try {
			request = httpParser.parseHttpRequest(generateValidGETTestCase());
		} catch (HttpParsingException e) {
			fail(e);
		}
		assertNotNull(request);
		assertEquals(request.getMethod(), HttpMethod.GET);
		assertEquals(request.getRequestTarget(), "/");
		assertEquals(request.getHttpVersion(), HttpVersion.HTTP_1_1);
	}

	@Test
	void parseHttpRequestBadMethod01() {
		try {
			httpParser.parseHttpRequest(generateBadTestCaseMethodName01());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
		}
	}

	@Test
	void parseHttpRequestBadMethod02() {
		try {
			httpParser.parseHttpRequest(generateBadTestCaseMethodName02());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
		}
	}

	@Test
	void parseHttpRequestInvalidRequestLineNumOfItems01() {
		try {
			httpParser.parseHttpRequest(generateBadTestCaseInvalidRequestLineNumOfItems01());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}

	@Test
	void parseHttpRequestInvalidRequestLineNumOfItems02() {
		try {
			httpParser.parseHttpRequest(generateBadTestCaseInvalidRequestLineNumOfItems02());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}

	@Test
	void parseHttpRequestNoLineFeed() {
		try {
			httpParser.parseHttpRequest(generateBadTestCaseNoLineFeed());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}

	@Test
	void parseHttpRequestBadHttpVersion() {
		try {
			httpParser.parseHttpRequest(generateBadHttpVersionRequest());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
		}
	}

	@Test
	void parseHttpRequestUnsupportedHttpVersion() {
		try {
			httpParser.parseHttpRequest(generateUnsupportedHttpVersion());
			fail();
		} catch (HttpParsingException e) {
			assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
		}
	}

	@Test
	void parseHttpRequestSupportedHttpVersion() {
		HttpRequest request = null;
		try {
			request = httpParser.parseHttpRequest(generateSupportedHttpVersion());
			assertNotNull(request);
			assertEquals(request.getHttpVersion(), HttpVersion.HTTP_1_1);
		} catch (HttpParsingException e) {
			fail();
		}
	}

	private InputStream generateValidGETTestCase() {
		String rawData = "GET / HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n" +
				"sec-ch-ua: \" Not A;Brand\";v=\"99\", \"Chromium\";v=\"100\", \"Google Chrome\";v=\"100\"\r\n" +
				"sec-ch-ua-mobile: ?0\r\n" +
				"sec-ch-ua-platform: \"Linux\"\r\n" +
				"DNT: 1\r\n" +
				"Upgrade-Insecure-Requests: 1\r\n" +
				"User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36\r\n" +
				"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n" +
				"Sec-Fetch-Site: none\r\n" +
				"Sec-Fetch-Mode: navigate\r\n" +
				"Sec-Fetch-User: ?1\r\n" +
				"Sec-Fetch-Dest: document\r\n" +
				"Accept-Encoding: gzip, deflate, br\r\n" +
				"Accept-Language: en-US,en;q=0.9\r\n" +
				"sec-gpc: 1\r\n\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateBadTestCaseMethodName01() {
		String rawData = "GeT / HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateBadTestCaseMethodName02() {
		String rawData = "GETTTTTT / HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateBadTestCaseInvalidRequestLineNumOfItems01() {
		String rawData = "GET / ABCD HTTP/1.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateBadTestCaseInvalidRequestLineNumOfItems02() {
		String rawData = "\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateBadTestCaseNoLineFeed() {
		String rawData = "GET / HTTP/1.1\r" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateBadHttpVersionRequest() {
		String rawData = "GET / HTP/1.0\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateUnsupportedHttpVersion() {
		String rawData = "GET / HTTP/2.1\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

	private InputStream generateSupportedHttpVersion() {
		String rawData = "GET / HTTP/1.2\r\n" +
				"Host: localhost:8080\r\n" +
				"Connection: keep-alive\r\n";

		return new ByteArrayInputStream(rawData.getBytes(StandardCharsets.US_ASCII));
	}

}