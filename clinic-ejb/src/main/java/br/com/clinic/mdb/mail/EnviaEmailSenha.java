package br.com.clinic.mdb.mail;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import br.com.clinic.mail.MailSenderLocal;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/topics/PasswordTopic") })
public class EnviaEmailSenha implements MessageListener {

	@EJB
	private MailSenderLocal mailSender;

	@Override
	public void onMessage(Message message) {

		MapMessage map = (MapMessage) message;

		String messageBody;
		try {
			messageBody = map.getString("message");
			// String de = map.getString("from");
			String para = map.getString("to");
			mailSender.send(para, "Alteração de senha", messageBody);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

}
