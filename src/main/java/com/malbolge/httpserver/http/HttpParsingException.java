package com.malbolge.httpserver.http;

public class HttpParsingException extends Exception {

	private final HttpStatusCode mErrorCode;

	public HttpParsingException(HttpStatusCode errorCode) {
		super(errorCode.MESSAGE);
		this.mErrorCode = errorCode;
	}

	public HttpStatusCode getErrorCode() {
		return mErrorCode;
	}
}
