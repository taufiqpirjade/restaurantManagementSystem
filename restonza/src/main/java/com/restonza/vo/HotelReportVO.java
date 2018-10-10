/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class HotelReportVO {
	String hotelname;
	String reporttype;
	String startdate;
	String todate;
	String fromdate;
	
	public HotelReportVO() {
		super();
	}
	

	/**
	 * @param hotelname
	 * @param startdate
	 * @param todate
	 * @param fromdate
	 */
	public HotelReportVO(String hotelname, String startdate, String todate,
			String fromdate) {
		this.hotelname = hotelname;
		this.startdate = startdate;
		this.todate = todate;
		this.fromdate = fromdate;
	}

	
	public HotelReportVO(String hotelname, String reporttype, String startdate,
			String todate, String fromdate) {
		this.hotelname = hotelname;
		this.reporttype = reporttype;
		this.startdate = startdate;
		this.todate = todate;
		this.fromdate = fromdate;
	}


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
		return "HotelReportVO [hotelname=" + hotelname + ", reporttype="
				+ reporttype + ", startdate=" + startdate + ", todate="
				+ todate + ", fromdate=" + fromdate + "]";
	}
	

}
