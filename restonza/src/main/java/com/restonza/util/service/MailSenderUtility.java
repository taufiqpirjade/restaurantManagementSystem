/**
 * 
 */
package com.restonza.util.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

/**
 * @author FlexGrow developers
 *
 */
@EnableAsync(proxyTargetClass=true)
@Service
public class MailSenderUtility {
	@Autowired
	private JavaMailSender mailSender;
	// private SimpleMailMessage templateMessage;

	@Value("${send.from.email}")
	private String fromEmail;

	/**
	 * Mail Utility that can be used for sending mails to clients
	 * 
	 * @param toAddress
	 *            client address
	 * @param subject
	 *            Subject of the mail
	 * @param bodyContents
	 *            Matter of the mail
	 * 
	 *            Currently added only simple structure later on we can add
	 *            attachment functionality as well[if required] Added sysout for
	 *            testing purpose
	 */
	public void send(String toAddress, String subject, String bodyContents) {
		System.out.println("Starting Send...");
		String response = "OK";
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(toAddress);
			helper.setFrom(this.fromEmail);
			helper.setSubject(subject);
			helper.setText(bodyContents, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			response = "NO_OK";
			System.err.println(e.getMessage());
		} catch (MailException e) {
			response = "NO_OK";
			System.err.println(e.getMessage());
		}
		System.out.println("Status of sender " + this.fromEmail + " :"
				+ response + "\nFinished Send...");
	}

	@Async
	public void asyncSend(String toAddress, String subject, String bodyContents) {
		System.out.println("Starting Send...");
		String response = "OK";
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(toAddress);
			helper.setFrom(this.fromEmail);
			helper.setSubject(subject);
			helper.setText(bodyContents, true);
			mailSender.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = "NO_OK";
			System.err.println(e.getMessage());
		} catch (MailException e) {
			// TODO: handle exception
			response = "NO_OK";
			System.err.println(e.getMessage());
		}
		System.out.println("Status of sender " + this.fromEmail + " :"
				+ response + "\nFinished Send...");
	}
}
