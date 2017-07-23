package org.inv3r53.fx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorrelationData {
	private static Pattern CorrIdPattern1 = Pattern.compile("(\"CorrelationId:\\s+)(.*?)\"");
	private static Pattern CorrIdPattern2 = Pattern.compile("(CorrelationId:\\s+)(.*?),");
	private static Pattern tsPattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2},\\d{3})");

	public static String getCorrelationId(String logEntry) {
		Matcher m = CorrIdPattern1.matcher(logEntry);
		while (m.find()) {
			// first matching corrId is sufficient;
			return m.group(2);
		}
		m = CorrIdPattern2.matcher(logEntry);
		while (m.find()) {
			// first matching corrId is sufficient;
			return m.group(2);
		}
		return "";
	}

	public static Date getTimeStamp(String logEntry) {
		Matcher m = tsPattern.matcher(logEntry);
		while (m.find()) {
			// first matching ts is sufficient;
			String ts = m.group(1);
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
			try {
				return sd.parse(ts);
			} catch (ParseException e) {
				System.err.println(logEntry + "---parsing timestamp has failed");
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {

		//String logEntry = ",,,\"CorrelationId: 847a58eb-1af7-44bf-acb1-b425c7af70e8\",,,,,,\"INFO  847a58eb-1af7-44bf-acb1-b425c7af70e8 2017-07-20 20:12:52,825 [[fast-experience]  \"CorrelationId: another-847a58eb-1af7-44bf-acb1-b425c7af70e8\"";
		String logEntry = ",,, CorrelationId: 847a58eb-1af7-44bf-acb1-b425c7af70e8,,,,,,\"INFO  847a58eb-1af7-44bf-acb1-b425c7af70e8 2017-07-20 20:12:52,825 [[fast-experience] , CorrelationId: another-847a58eb-1af7-44bf-acb1-b425c7af70e8,";
		// String logEntry =
		// "dmc_group_search_head\",,67,43,,,,,\"Apache-HttpClient/4.5.3\",,,,,,close,";
		// String logEntry = ",,,,,,,,\"INFO
		// 847a58eb-1af7-44bf-acb1-b425c7af70e8 2017-07-20 20:12:52,825
		// [[fast-experience] \"CorrelationId:
		// another-847a58eb-1af7-44bf-acb1-b425c7af70e8\"";
		System.out.println(getCorrelationId(logEntry));
		System.out.println(getTimeStamp(logEntry));

	}

}
