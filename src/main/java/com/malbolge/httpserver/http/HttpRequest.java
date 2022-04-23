package com.malbolge.httpserver.http;

public class HttpRequest extends HttpMessage {

	private HttpMethod mMethod;
	private String mRequestTarget;
	private String mOriginalHttpVersion;
	private HttpVersion mBestCompatibleVersion;

	HttpRequest() {

	}

	public HttpMethod getMethod() {
		return mMethod;
	}

	public String getRequestTarget() {
		return mRequestTarget;
	}

	public HttpVersion getHttpVersion() {
		return this.mBestCompatibleVersion;
	}

	void setMethod(String method) throws HttpParsingException {
		for (HttpMethod _method : HttpMethod.values()) {
			if (method.equals(_method.name())) {
				this.mMethod = _method;
				return;
			}
		}
		throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
	}

	void setRequestTarget(String requestTarget) throws HttpParsingException {
		if (requestTarget == null || requestTarget.length() == 0)
			throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
		this.mRequestTarget = requestTarget;
	}

	void setHttpVersion(String originalVersion) throws BadHttpVersionException, HttpParsingException {
		this.mOriginalHttpVersion = originalVersion;
		this.mBestCompatibleVersion = HttpVersion.getBestCompatibleVersion(this.mOriginalHttpVersion);

		if (this.mBestCompatibleVersion == null)
			throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
	}
}
