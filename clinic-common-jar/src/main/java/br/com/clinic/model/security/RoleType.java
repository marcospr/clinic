package br.com.clinic.model.security;

public enum RoleType {
	ADMIN_SECURITY("admin.security"), PATIENT_REGISTRATION_READ("patient.registration.read"),
	PATIENT_REGISTRATION_CREATE("patient.registration.create"),
	PATIENT_REGISTRATION_UPDATE("patient.registration.update"), MAIN("main");

	private String role;

	RoleType(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public static RoleType fromShortName(String shortName) {
		switch (shortName) {
		case "admin.security":
			return ADMIN_SECURITY;

		case "patient.registration.read":
			return PATIENT_REGISTRATION_READ;

		case "patient.registration.create":
			return PATIENT_REGISTRATION_CREATE;

		case "patient.registration.update":
			return PATIENT_REGISTRATION_UPDATE;

		case "main":
			return MAIN;
		default:
			throw new IllegalArgumentException("ShortName [" + shortName + "] not supported.");
		}
	}

}
