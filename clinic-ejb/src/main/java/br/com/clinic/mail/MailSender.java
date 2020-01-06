package br.com.clinic.mail;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Singleton
public class MailSender implements MailSenderLocal {
	@Resource(mappedName = "java:jboss/mail/gmail")
	private Session session;

	@Override
	public void send(String from, String to, String subject, String body) {
		MimeMessage message = new MimeMessage(session);

		try {
			message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
			message.setFrom(new InternetAddress(from));
			message.setSubject(subject);
			message.setContent(body, "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void send(String to, String subject, String body) {
		MimeMessage message = new MimeMessage(session);

		try {
			message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(body, "text/html");

			Transport.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
