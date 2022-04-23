package com.malbolge.httpserver.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {

	HTTP_1_1("HTTP/1.1", 1, 1);

	public final String LITERAL;
	public final int MAJOR;
	public final int MINOR;

	private static final Pattern sHttpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

	HttpVersion(String LITERAL, int MAJOR, int MINOR) {
		this.LITERAL = LITERAL;
		this.MAJOR = MAJOR;
		this.MINOR = MINOR;
	}

	public static HttpVersion getBestCompatibleVersion(String version) throws BadHttpVersionException {
		Matcher matcher = sHttpVersionRegexPattern.matcher(version);
		if (!matcher.find() || matcher.groupCount() != 2)
			throw new BadHttpVersionException();

		int major = Integer.parseInt(matcher.group("major"));
		int minor = Integer.parseInt(matcher.group("minor"));

		HttpVersion bestCompatible = null;
		for (HttpVersion ver : HttpVersion.values()) {
			if (ver.LITERAL.equals(version))
				return ver;
			else {
				if (ver.MAJOR == major) {
					if (ver.MINOR < minor)
						bestCompatible = ver;
				}
			}
		}
		return bestCompatible;
	}
}
