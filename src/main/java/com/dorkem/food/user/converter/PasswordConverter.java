package com.dorkem.food.user.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

	@Override
	public String convertToDatabaseColumn(String password) {
		if (password == null) return null;
		return BCrypt.withDefaults().hashToString(12, password.toCharArray());
	}

	@Override
	public String convertToEntityAttribute(String dbPassword) {
		return dbPassword;
	}
}
