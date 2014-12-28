package com.frog.core.log4j.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

public abstract class Log4j2Configurer {

	/** Pseudo URL prefix for loading from the class path: "classpath:" */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** Extension that indicates a log4j XML config file: ".xml" */
	public static final String XML_FILE_EXTENSION = ".xml";


	/**
	 * Initialize log4j from the given file location, with no config file refreshing.
	 * Assumes an XML file in case of a ".xml" file extension, and a properties file
	 * otherwise.
	 * @param location the location of the config file: either a "classpath:" location
	 * (e.g. "classpath:myLog4j.properties"), an absolute file URL
	 * (e.g. "file:C:/log4j.properties), or a plain absolute path in the file system
	 * (e.g. "C:/log4j.properties")
	 * @throws FileNotFoundException if the location specifies an invalid file path
	 */
	public static void initLogging(String location) throws FileNotFoundException {
		String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
		URL url = ResourceUtils.getURL(resolvedLocation);
		if (ResourceUtils.URL_PROTOCOL_FILE.equals(url.getProtocol()) && !ResourceUtils.getFile(url).exists()) {
			throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
		}
		File file = ResourceUtils.getFile(resolvedLocation);
		if (!file.exists()) {
			throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
		}
		ConfigurationSource source;  
		try {  
			//方法1  使用  public ConfigurationSource(InputStream stream) throws IOException 构造函数  
			source = new ConfigurationSource(new FileInputStream(file.getAbsolutePath()));
			Configurator.initialize(null, source); 
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize log4j from the given location, with the given refresh interval
	 * for the config file. Assumes an XML file in case of a ".xml" file extension,
	 * and a properties file otherwise.
	 * <p>Log4j's watchdog thread will asynchronously check whether the timestamp
	 * of the config file has changed, using the given interval between checks.
	 * A refresh interval of 1000 milliseconds (one second), which allows to
	 * do on-demand log level changes with immediate effect, is not unfeasible.
	 * <p><b>WARNING:</b> Log4j's watchdog thread does not terminate until VM shutdown;
	 * in particular, it does not terminate on LogManager shutdown. Therefore, it is
	 * recommended to <i>not</i> use config file refreshing in a production J2EE
	 * environment; the watchdog thread would not stop on application shutdown there.
	 * @param location the location of the config file: either a "classpath:" location
	 * (e.g. "classpath:myLog4j.properties"), an absolute file URL
	 * (e.g. "file:C:/log4j.properties), or a plain absolute path in the file system
	 * (e.g. "C:/log4j.properties")
	 * @param refreshInterval interval between config file refresh checks, in milliseconds
	 * @throws FileNotFoundException if the location specifies an invalid file path
	 */
	public static void initLogging(String location, long refreshInterval) throws FileNotFoundException {
		String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
		File file = ResourceUtils.getFile(resolvedLocation);
		if (!file.exists()) {
			throw new FileNotFoundException("Log4j config file [" + resolvedLocation + "] not found");
		}

		ConfigurationSource source;  
		try {  
			//方法1  使用  public ConfigurationSource(InputStream stream) throws IOException 构造函数  
			source = new ConfigurationSource(new FileInputStream(file.getAbsolutePath()));
			Configurator.initialize(null, source); 
		}catch(Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shut down log4j, properly releasing all file locks.
	 * <p>This isn't strictly necessary, but recommended for shutting down
	 * log4j in a scenario where the host VM stays alive (for example, when
	 * shutting down an application in a J2EE environment).
	 */
	public static void shutdownLogging() {
		//shutdown log4j2
        if( LogManager.getContext() instanceof LoggerContext ) {
//            logger.info("Shutting down log4j2");
            Configurator.shutdown((LoggerContext)LogManager.getContext());
        } 
//            logger.warn("Unable to shutdown log4j2");
	}

	/**
	 * Set the specified system property to the current working directory.
	 * <p>This can be used e.g. for test environments, for applications that leverage
	 * Log4jWebConfigurer's "webAppRootKey" support in a web environment.
	 * @param key system property key to use, as expected in Log4j configuration
	 * (for example: "demo.root", used as "${demo.root}/WEB-INF/demo.log")
	 * @see org.springframework.web.util.Log4jWebConfigurer
	 */
	public static void setWorkingDirSystemProperty(String key) {
		System.setProperty(key, new File("").getAbsolutePath());
	}
}
