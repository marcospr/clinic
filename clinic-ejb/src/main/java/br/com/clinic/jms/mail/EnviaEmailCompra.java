package br.com.clinic.jms.mail;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import br.com.clinic.mail.MailSender;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topics/CarrinhoComprasTopico") })
public class EnviaEmailCompra implements MessageListener {

	@EJB
	private MailSender mailSender;

	@Override
	public void onMessage(Message message) {

		TextMessage textMessage = (TextMessage) message;

		String messageBody;
		try {
			messageBody = "Clique atualizar sua senha: " + textMessage.getText();
			String de = "marcospinheirorocha@gmail.com";
			String para = "marcospinheirorocha@hotmail.com";
			mailSender.send(de, para, "Alteração de senha", messageBody);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
