package br.com.clinic.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import br.com.clinic.model.Doctor;
import br.com.clinic.model.Patient;
import br.com.clinic.model.Schedule;
import br.com.clinic.service.ReadOperation;

public class SchedulerService implements ReadOperation<Schedule, Long>, Serializable {

	private static final long serialVersionUID = 1L;

	public Schedule reschedule(Schedule schedule, Calendar date) {
		return null;
	}

	public Schedule schedule(Calendar date, Patient patient, Doctor doctor) {
		return null;
	}

	public void cancel(Schedule schedule) {
	}

	public List<Schedule> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Schedule findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
