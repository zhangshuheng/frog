package com.frog.core.mvc.handler;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomObjectMapper() {
		this.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}
}
