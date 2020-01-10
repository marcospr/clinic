package br.com.clinic.schedule;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.com.clinic.exception.FailureToken;
import br.com.clinic.model.security.UserSystem;
import br.com.clinic.security.service.TokenServiceRemote;
import br.com.clinic.service.UserServiceRemote;

@Singleton
@Startup
public class ClearTokenProcess implements Process {
	/** Logger. */
	private static Logger log = Logger.getLogger(ClearTokenProcess.class.getCanonicalName());

	@EJB
	private UserServiceRemote userService;
	@EJB
	private TokenServiceRemote tokenService;
	private boolean running;

	@Schedule(minute = "*/1", hour = "*", persistent = false)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public void process() {
		if (running) {
			return;
		}
		log.info("Scheduler execution started.");
		long startTime = System.currentTimeMillis();
		running = true;
		List<UserSystem> users = userService.findAllByTokenIsNotNull();
		for (UserSystem user : users) {
			if (canClearToken(user)) {
				userService.clearToken(user);
			}
		}
		running = false;
		log.info("Scheduler execution finished in " + (System.currentTimeMillis() - startTime) + " millsec");
	}

	private boolean canClearToken(UserSystem user) {
		try {
			// Parse Token
			tokenService.parse(user.getToken(), user.getPasswordToken());
		} catch (FailureToken e) {
			return true;
		}
		return false;
	}
}
