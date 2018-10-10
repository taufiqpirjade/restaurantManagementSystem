package com.restonza.vo;

public class ReportRequest {
	

	private String hotelname;
	private String reporttype;
	private String startdate;
	private String todate;
	private String fromdate;
	
	public String getHotelname() {
		return hotelname;
	}
	public void setHotelname(String hotelname) {
		this.hotelname = hotelname;
	}
	public String getReporttype() {
		return reporttype;
	}
	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	@Override
	public String toString() {
		return "ReportRequest [hotelname=" + hotelname + ", reporttype="
				+ reporttype + ", startdate=" + startdate + ", todate="
				+ todate + ", fromdate=" + fromdate + "]";
	}
	
	


}
