package br.com.clinic.service.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService {
	private static MailService theService;

	public static void sendMessageRecuperar(String destinatario, String messagem, String parametro, String nomeCliente)
			throws MessagingException, UnsupportedEncodingException {

		try {
			if (theService == null) {
				theService = new MailService();
			}
			// Criar tipo de mensagem multipart para permitir inclusão de anexos
			Multipart mpCorpoPrincipal = new MimeMultipart("related");
			// Criando corpo da mensagem (com texto e html)
			MimeMultipart mpContent = new MimeMultipart("alternative");
			// A raiz para agrupar os dois tipos de textos
			MimeBodyPart corpoRaiz = new MimeBodyPart();
			corpoRaiz.setContent(mpContent);
			// Adiciona a raiz à mensagem
			mpCorpoPrincipal.addBodyPart(corpoRaiz);

			// Adicionando texto puro à raiz
			MimeBodyPart mbpTextPlain = new MimeBodyPart();
			mbpTextPlain.setText("Recuperacao de senha", "iso-8859-1", "plain");
			mpContent.addBodyPart(mbpTextPlain);
			// Adicionando texto html à raiz
			MimeBodyPart mbpTextHtml = new MimeBodyPart();
			String mensagem = htmlMessage(parametro, nomeCliente);
			mbpTextHtml.setText(mensagem, "iso-8859-1", "html");
			mpContent.addBodyPart(mbpTextHtml);

			// Obtém a referência ao arquivo de imagem
			File f = new File("C:\\Users\\amadeu.carvalho\\Pictures\\SantaEmail.png");
			// Cria a parte de e-mail que irá armazenar a imagem e adiciona o arquivo
			MimeBodyPart mbpImagemInline = new MimeBodyPart();
			mbpImagemInline.setDataHandler(new DataHandler(new FileDataSource(f)));
			mbpImagemInline.setFileName(f.getName());
			// Define um id que pode ser utilizado no html
			mbpImagemInline.setHeader("Content-ID", "myImage");
			// Insere na mensagem
			mpCorpoPrincipal.addBodyPart(mbpImagemInline);

			MimeMessage mimeMessage = new MimeMessage(mailSession);

			mimeMessage.setFrom(new InternetAddress("seuEmail", "NomeRemetente"));
			// Titulo email
			mimeMessage.setSubject("Nova Senha");
			mimeMessage.setContent(mpCorpoPrincipal, "text/html; charset=utf-8");
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			Transport transport = mailSession.getTransport("smtps");
			transport.connect("smtp.gmail.com", 465, "seuEmail", "suaSenha");

			mimeMessage.saveChanges();

			mimeMessage.setSentDate(new Date());

			transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));

			transport.close();
		} catch (AddressException e) {
			System.out.println("Saída email invalido " + e.getMessage());
		}
	}

	private static Session mailSession;

	private MailService() {

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.auth", "true");

		mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
			;
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("seuEmail", "suaSenha");// Specify the Username and the PassWord
			}
		});
		mailSession.setDebug(true);
	}

	private static String htmlMessage(String parametro, String nomeCliente) {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "<html>" + "<head>" + "<title>Recuperação de Senha</title>" + "</head>" + "<body>"
				+ "<div style='font-size: 14px'>" + "<p>Olá " + nomeCliente + " para alterar sua senha clique no link "
				+ "<a href=\"http://" + ia.getHostAddress() + ":8080/sta/login?parametro=" + parametro
				+ "\">Nova Senha</a>" + "</p>" + "</div>" + "<p> Antenciosamente <br/> SantaConstancia </p> "
				+ "<img src=\"cid:myImage\"/>" + "</body>" + "</html>";
	}
}
