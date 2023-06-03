package com.dollarsbank.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtil {

	public static String asJsonString(final Object obj) {
		ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return writer.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
