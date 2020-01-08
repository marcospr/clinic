package br.com.clinic.schedule;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Singleton
public class ClearTokenProcess implements Process {

	@Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public void process() {

	}
}
