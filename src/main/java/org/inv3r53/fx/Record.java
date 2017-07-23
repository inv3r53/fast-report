package org.inv3r53.fx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Record {

	String groupId;
	private List<Log> logs = new ArrayList<Record.Log>();

	public Record(String groupId) {
		super();
		this.groupId = groupId;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void addLog(String templateKey, String logEntry) {
		Date ts = CorrelationData.getTimeStamp(logEntry);
		if (ts == null) {
			System.err.println("Skipping logEntry as timestamp parsing has failed!");
			return;
		}
		logs.add(new Log(ts, logEntry, templateKey));
	}

	@Override
	public String toString() {
		return "Record [groupId=" + groupId + ", logs=" + logs + "]";
	}

	static class Log {

		private String templateKey;
		private Date timestamp;
		private String message;

		public Log(Date timestamp, String message, String templateKey) {
			super();
			this.timestamp = timestamp;
			this.message = message;
			this.templateKey = templateKey;
		}

		public Date getTimestamp() {
			return timestamp;
		}

		public String getMessage() {
			return message;
		}

		public String getTemplateKey() {
			return templateKey;
		}

		public String formattedTs() {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			return sd.format(timestamp);
		}

		@Override
		public String toString() {
			return "Log [templateKey=" + getTemplateKey() + ", timestamp=" + getTimestamp() + ", message="
					+ getMessage() + "]";
		}

	}
}
