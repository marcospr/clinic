package br.com.clinic.mail;

import javax.ejb.Local;

@Local
public interface MailSenderLocal {

	void send(String from, String to, String subject, String body);

	void send(String to, String subject, String body);

}
