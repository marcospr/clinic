package br.com.clinic.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.Stateless;

@Stateless
public class ConfigurationFactory {

	public Properties getProperties() throws IOException {
		InputStream inputStream = ConfigurationFactory.class.getResourceAsStream("/configuracoes.properties");

		Properties properties = new Properties();
		properties.load(inputStream);

		return properties;
	}

}
