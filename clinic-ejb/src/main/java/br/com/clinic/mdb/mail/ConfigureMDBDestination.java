package br.com.clinic.mdb.mail;

import javax.ejb.Singleton;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

@JMSDestinationDefinitions({
		@JMSDestinationDefinition(name = "java:/jms/topics/PasswordTopic", interfaceName = "javax.jms.Topic", destinationName = "java:/jms/topics/PasswordTopic") })
@Singleton
public class ConfigureMDBDestination {

}
