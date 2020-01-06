package br.com.clinic.security.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;

import br.com.clinic.model.security.UserSystem;
import br.com.clinic.security.GeneratorPasswordServiceRemote;
import br.com.clinic.service.SecurityServiceRemote;
import br.com.clinic.service.UserServiceRemote;

@Singleton
public class SecurityService implements SecurityServiceRemote {
	@EJB
	private GeneratorPasswordServiceRemote generatorPasswordService;

	@EJB
	private TokenServiceRemote tokenService;

	@EJB
	private UserServiceRemote userService;

	@Inject
	private JMSContext jmsContext;

	@Resource(name = "java:/jms/topics/PasswordTopic")
	private Destination destinationMdb;

	public Boolean login(UserSystem user) {
		return Boolean.TRUE;
	}

	public Boolean logout(UserSystem user) {
		return Boolean.TRUE;
	}

	@Override
	public void rememberPassword(UserSystem user, String url) {
		// Gera Senha
		String senhaToken = generatorPasswordService.gerarNovaSenha();

		// Gerar Token
		String token = tokenService.generate(user.getId().toString(), senhaToken);

		// Complementa url com Token
		String urlToken = url.concat("&token=" + token);

		String htmlMessage = mountHtmlMessage(urlToken, user.getName());

		// Persistir o Token e sua senha
		user.setToken(token);
		user.setPasswordToken(senhaToken);
		userService.save(user);

		// mandar e-mail via JMS
		JMSProducer producer = jmsContext.createProducer();

		Map<String, Object> map = new HashMap<>();
		map.put("message", htmlMessage);
		map.put("to", user.getEmail());
		producer.send(destinationMdb, map);

	}

	private String mountHtmlMessage(String parametro, String nomeCliente) {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "<html>" + "<head>" + "<title>Recuperação de Senha</title>" + "</head>" + "<body>"
				+ "<div style='font-size: 14px'>" + "<p>Olá " + nomeCliente + " para alterar sua senha clique no link "
				+ "<a href=\"http://" + ia.getHostAddress() + ":8081" + parametro + "\">Nova Senha</a>" + "</p>"
				+ "</div>" + "<p> Antenciosamente <br/> Administrador </p> " + "<img src=\"cid:myImage\"/>" + "</body>"
				+ "</html>";
	}

}
