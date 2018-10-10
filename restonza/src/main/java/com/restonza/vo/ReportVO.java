package com.restonza.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportVO {
	String date;
	int value;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) throws ParseException {
		Date initDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	    String parsedDate = formatter.format(initDate);
		this.date = parsedDate;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}

	
	
}
