package br.com.clinic.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.com.clinic.model.security.RoleType;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

	@Override
	public String convertToDatabaseColumn(RoleType roleType) {
		return roleType.getRole();
	}

	@Override
	public RoleType convertToEntityAttribute(String name) {
		return RoleType.fromShortName(name);
	}

}
