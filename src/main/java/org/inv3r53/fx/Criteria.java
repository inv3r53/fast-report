package org.inv3r53.fx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Criteria {
	private static final String match_delimiter = "\\|";
	private String key;
	private List<String> matchers;

	public List<String> getMatchers() {
		return matchers;
	}

	public Criteria(String key, String matches) {
		super();
		this.key = key;
		this.matchers = Collections.unmodifiableList(Arrays.asList(matches.split(match_delimiter)));
	}

	public String getKey() {
		return key;
	}

	public boolean matches(String logEntry) {

		for (String match : matchers) {
			if (!logEntry.contains(match)) {
				// atleast one unsuccessful match === no-match.
				return false;
			}
		}
		// all matching strings exists in the log entry.
		return true;
	}

	public static void main(String[] args) {
		String matches = "fast-experience|processing started";
		String[] arr =matches.split(match_delimiter);
		System.out.println(arr.length);
		Criteria c = new Criteria("test", matches);
		System.out.println(c.matchers);
	}

}
