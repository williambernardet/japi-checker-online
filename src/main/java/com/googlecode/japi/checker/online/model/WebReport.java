package com.googlecode.japi.checker.online.model;

import com.googlecode.japi.checker.Reporter.Report;
import com.googlecode.japi.checker.Severity;

public class WebReport {

	private Severity severity;
	private String message;
	private String source;
	
	public WebReport(Report report) {
		severity = report.getSeverity();
		message = report.getMessage();
		source = report.getSource();
	}

	public Severity getSeverity() {
		return severity;
	}

	public String getMessage() {
		return message;
	}

	public String getSource() {
		return source;
	}
	
}
