/**
 * 
 */
package com.restonza.dao;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.restonza.vo.GreetingsDetailsVO;

/**
 * @author flex-grow developers
 *
 */
@Entity
@Table(name = "greetings_details")
public class GreetingsDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id;
	@NotNull
	private String greetingname;
	@NotNull
	private String greetingdetails;
	@Lob
	private byte[] greetingimg;
	@NotNull
	private Date greetingdate;
	@NotNull
	private boolean status;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGreetingname() {
		return greetingname;
	}
	public void setGreetingname(String greetingname) {
		this.greetingname = greetingname;
	}
	public String getGreetingdetails() {
		return greetingdetails;
	}
	public void setGreetingdetails(String greetingdetails) {
		this.greetingdetails = greetingdetails;
	}
	public byte[] getGreetingimg() {
		return greetingimg;
	}
	public void setGreetingimg(byte[] greetingimg) {
		this.greetingimg = greetingimg;
	}
	public Date getGreetingdate() {
		return greetingdate;
	}
	public void setGreetingdate(Date greetingdate) {
		this.greetingdate = greetingdate;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public GreetingsDetails prepareGreetingDetails(GreetingsDetails aGreetingsDetails, GreetingsDetailsVO aGreetingsDetailsVO) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		aGreetingsDetails.setGreetingname(aGreetingsDetailsVO.getGreetingname());
		aGreetingsDetails.setGreetingdetails(aGreetingsDetailsVO.getDescription());
		aGreetingsDetails.setGreetingimg(aGreetingsDetailsVO.getGreetingimg());
		aGreetingsDetails.setGreetingdate(sdf.parse(aGreetingsDetailsVO.getGreetingdate()));
		aGreetingsDetails.setStatus(aGreetingsDetailsVO.getStatus().equals("active") ? true : false);
		return aGreetingsDetails;
	}
}
