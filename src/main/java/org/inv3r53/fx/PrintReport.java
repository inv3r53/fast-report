package org.inv3r53.fx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.inv3r53.fx.Record.Log;

public class PrintReport {

	public static void main(String[] args) {
		List<Criteria> templateData = loadTemplate(args[0]);
		List<String> corrIds = getCorrIds(args[1]);
		Map<String, Record> records = getRecords(corrIds, templateData, args[2]);

		int maxCols = 0;
		Record maxRecord = null;
		for (Map.Entry<String, Record> row : records.entrySet()) {

			List<Log> logs = row.getValue().getLogs();

			if (maxCols <= logs.size()) {
				maxCols = logs.size();
				maxRecord = row.getValue();
			}

		}

		String header = getHeader(maxRecord);

		StringBuilder sb = new StringBuilder();
		sb.append(header).append("\n");

		for (Map.Entry<String, Record> row : records.entrySet()) {
			sb.append(row.getKey()).append("|");
			List<Log> logs = row.getValue().getLogs();
			if (logs.size() < maxCols) {
				System.err.println("In correct length for " + row.getKey());
			}
			for (Log log : logs) {
				sb.append(log.getTemplateKey()).append("=").append(log.formattedTs()).append("|");
			}
			sb.append("\n");
		}
		System.out.println("********************************************");
		System.out.println(sb.toString());
		System.out.println("********************************************");
		PrintWriter pw = null;
		try {

			pw = new PrintWriter(new FileWriter(new File("report-" + System.currentTimeMillis() + ".csv")));
			pw.write(sb.toString());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(pw);
		}
	}

	private static String getHeader(Record maxRecord) {
		StringBuilder sb = new StringBuilder();
		sb.append("CorrId|");
		List<Log> logs = maxRecord.getLogs();
		for (Log l : logs) {
			sb.append(l.getTemplateKey()).append("|");
		}
		return sb.toString();
	}

	private static Map<String, Record> getRecords(List<String> corrIds, List<Criteria> templateData, String logFile) {
		Map<String, Record> records = new HashMap<String, Record>();
		try {
			LineIterator lineIterator = FileUtils.lineIterator(new File(logFile));
			while (lineIterator.hasNext()) {
				String line = lineIterator.next();

				String corrId = CorrelationData.getCorrelationId(line);
				if (corrId.isEmpty()) {
					// line does not have corrId , skip this and continue;
					continue;
				}
				// line has corrId - get its record;
				Record record = records.get(corrId);
				if (record == null) {
					record = new Record(corrId);
					records.put(corrId, record);
				}
				// now check if it matches any of the criteria defined
				for (Criteria c : templateData) {
					if (c.matches(line)) {
						// match found
						record.addLog(c.getKey(), line);
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return records;
	}

	private static List<Criteria> loadTemplate(String templateFile) {
		List<Criteria> list = new LinkedList<Criteria>();
		try {
			LineIterator lineIterator = FileUtils.lineIterator(new File(templateFile));
			while (lineIterator.hasNext()) {
				String line = lineIterator.next();
				int keyEndIndex = line.indexOf('=');
				String key = line.substring(0, keyEndIndex);
				String matches = line.substring(keyEndIndex + 1);
				list.add(new Criteria(key, matches));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static List<String> getCorrIds(String corrIdFile) {
		List<String> corrIds = new LinkedList<String>();
		try {
			LineIterator lineIterator = FileUtils.lineIterator(new File(corrIdFile));
			while (lineIterator.hasNext()) {
				corrIds.add(lineIterator.next());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return corrIds;
	}

}
