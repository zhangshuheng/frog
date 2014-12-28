package com.frog.core.log4j.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Log4j2ConfigListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		Log4j2WebConfigurer.shutdownLogging(event.getServletContext());
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		Log4j2WebConfigurer.initLogging(event.getServletContext());
	}

}
