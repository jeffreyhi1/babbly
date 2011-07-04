/*
 *  Copyright (C) 2010 Georgi Dimitrov  g.dimitrov[at]mail[dot]com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, at version 3 only.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 */
package org.babbly.core.util;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;


/**
 * A convinient logging mechanism that incorporates the functionality of the 
 * famous Apache Log4j Java library. This class provides an accessible central 
 * logging point for the application. This makes logging a breeze and a sealess
 * experience. 
 * 
 * <b>Note:</b>
 * You need to include the Log4j library (e.g. log4j-1.2.8.jar or above) into
 * your application's classpath in order for this class to compile.
 * 
 * @author Georgi Dimitrov (g.dimitrov@mail.com)
 *
 */
public class AppLog {


	/**
	 * The actual logger that is used for logging purposes. 
	 */
	private Logger logger = null;
	
	/**
	 * Specifies whether the logging calls are to be proceeded or dropped.  
	 */
	private boolean debug = true;

	/**
	 * A default name of the logging file. 
	 */
	private static final String DEFAULT_LOGGING_FILE = "babbly.log";

	
	/**
	 * A default logging pattern. 
	 */
	private static final String DEFAULT_LOGGING_PATTERN = 
		"%-5p %d{HH:mm:ss,SSS} %C{1} - %m%n";

	/**
	 * A default logging directory. 
	 */
	private static final String DEFAULT_LOGGING_DIRECTORY = "logs";

	/**
	 * A default date pattern. 
	 */
	private static final String DEFAULT_DATE_PATTERN = "'.'yyyy-MM-dd-HH";

	/**
	 * A message that is to be displayed if an error occurs. 
	 */
	private static final String UNABLE_TO_INIT_LOGGING_MSG = 
		"WARNING:Could not initialize logging components" +
		" due to restricted file system permissions.\r"+ 
		"Logging will be redirected to the system console!";

	
	// a static constructor that is invoked the first time the logger is called.
	static{
		
		// Don't panic here we just check if we have RW access to the file 
		// system or if a file named "gogosip.log" already exists
		File loggingDir = new File(DEFAULT_LOGGING_DIRECTORY);
		boolean dirExists = loggingDir.exists();
		boolean dirRWaccess = loggingDir.canWrite();

		/* if there is RW access to the file system, create new appender for the
		 * logger with the specified layout, path and date patterns.
		 */
		if(!dirExists){
			loggingDir.mkdirs();
			dirRWaccess = loggingDir.exists();
		}
		if(dirRWaccess) {

			PatternLayout layout = new PatternLayout(DEFAULT_LOGGING_PATTERN);

			String logFilePath = DEFAULT_LOGGING_DIRECTORY + "/" + DEFAULT_LOGGING_FILE; 

			try {
				DailyRollingFileAppender appender = new DailyRollingFileAppender(
						layout, logFilePath, DEFAULT_DATE_PATTERN);
				Logger.getRootLogger().removeAllAppenders();
				Logger.getRootLogger().addAppender(appender);
			} catch (IOException e) {
				dirRWaccess = false;
			}    
		}
		/*if we don't have RW access to the file system: */
		if(!dirRWaccess) {
			System.err.println(UNABLE_TO_INIT_LOGGING_MSG);
			addDefaultAppender();
		}
	}

	/**
	 * Tries to load the Log4J configuration file that is to be found at the 
	 * specified path and afterwards removes all appenders from the current 
	 * logger, so the new loaded configuration may work as desired. 
	 * 
	 * @param path the path where the configuration file is to be found at.
	 */
	public static void loadConfiguration(String path) {
		PropertyConfigurator.configure(path);
		Logger.getRootLogger().removeAllAppenders();
	}
	

	/**
	 * Constructs new <code>LoggingUtility</code> object.
	 * <p>
	 * <b>NOTE:</b> Not to be used externally. Use the static method
	 * <code>LoggingUtility.getLogger(String name)</code> or 
	 * <code>LoggingUtility.getLogger(Class clazz)</code>instead.
	 * 
	 * @param logger the logger to use for logging.
	 */
	private AppLog(Logger logger){
		this.logger = logger;
	}

	/**
	 * Log the object message in debug level.
	 * 
	 *@param msg the message to log.
	 */
	public void debug(Object msg){
		logger.log(logger.getName(), Level.DEBUG, msg, null);
	}
	
	/**
	 * Log the object message in debug level.
	 * 
	 * @param msg the message to log.
	 * @param t additional throwable information.
	 */
	public void debug(Object msg, Throwable t){
		logger.log(logger.getName(), Level.DEBUG, msg, t);
	}

	/**
	 * Logs the given <code>Object</code> message at level INFO.
	 * 
	 * @param msg the message to log.
	 */
	public void info(Object msg){
		log(logger.getName(), Level.INFO, msg, null);
	}
	
	/**
	 * Logs the given <code>Object</code> message at level INFO.
	 * 
	 * @param msg the message to log.
	 * @param t The <code>Throwable</code> object of the logging request, 
	 * 			may be <code>null</code>
	 */
	public void info(Object msg, Throwable t){
		log(logger.getName(), Level.INFO, msg, t);
	}

	/**
	 * Logs the given <code>Object</code> message at level WARN.
	 * 
	 * @param msg the message to log.
	 */
	public void warn(Object msg){
		log(logger.getName(), Level.WARN, msg, null);
	}
	
	/**
	 * Logs the given <code>Object</code> message at level WARN.
	 * 
	 * @param msg the message to log.
	 * @param t The <code>Throwable</code> object of the logging request, 
	 * 			may be <code>null</code>
	 */
	public void warn(Object msg, Throwable t){
		log(logger.getName(), Level.WARN, msg, t);
	}

	/**
	 * Logs the given <code>Object</code> message at level ERROR.
	 * 
	 * @param msg the message to log.
	 */
	public void error(Object msg){
		log(logger.getName(), Level.ERROR, msg, null);
	}
	
	/**
	 * Logs the given <code>Object</code> message at level ERROR.
	 * 
	 * @param msg the message to log.
	 * @param t The <code>Throwable</code> object of the logging request, 
	 * 			may be <code>null</code>
	 */
	public void error(Object msg, Throwable t){
		log(logger.getName(), Level.ERROR, msg, t);
	}

	/**
	 * Logs the given <code>Object</code> message at level FATAL.
	 * 
	 * @param msg the message to log.
	 */
	public void fatal(Object msg){
		log(logger.getName(), Level.FATAL, msg, null);
	}
	
	/**
	 * Logs the given <code>Object</code> message at level FATAL.
	 * 
	 * @param msg the message to log.
	 * @param t The <code>Throwable</code> object of the logging request, 
	 * 			may be <code>null</code>
	 */
	public void fatal(Object msg, Throwable t){
		log(logger.getName(), Level.FATAL, msg, t);
	}
	
	/**
	 * Logs the given <code>Object</code> message at level FATAL.
	 * 
	 * @param msg the message to log.
	 * @param t The <code>Throwable</code> object of the logging request, 
	 * 			may be <code>null</code>
	 */
	
	/**
	 * @param FQCN The fully qualified class name for the wrapper class.
	 * @param level The level to log onto (DEBUG, INFO, WARN, ERROR, FATAL)
	 * @param msg the message to log.
	 * @param t The <code>Throwable</code> object of the logging request, 
	 * 			may be <code>null</code>
	 */
	private void log(String FQCN, Level level, Object msg, Throwable t){
		if(debug){
			logger.log(FQCN, level, msg, t);	
		}
	}
	

	/**
	 * Creates a new logger for the specified class.
	 * 
	 * @param clazz the class for which a new logger should be created.
	 * @return the newly created logger for the given class.
	 */
	public static AppLog getLogger(Class<?> clazz){

		return new AppLog(Logger.getLogger(clazz));//getLogger(clazz.getName());
	}
	
	/**
	 * Creates a new logger for the specified class name.
	 * 
	 * @param clazz the class name for which a new logger should be created.
	 * @return the newly created logger for the given class name.
	 */
	public static AppLog getLogger(String name){
		
		return new AppLog(Logger.getLogger(name));
	}
	
	/**
	 * Adds a default appender of type <code>ConsoleAppender</code> having the
	 * <code>PatternLayout</code> with a 
	 * <code>LoggingUtility.DEFAULT_LOGGING_PATTERN</code>.
	 * 
	 */
	public static void addDefaultAppender(){
		
		Logger.getRootLogger().addAppender(
				new ConsoleAppender(new PatternLayout(DEFAULT_LOGGING_PATTERN)));
	}


	/**
	 * Sets the value for the <code>isDebug</code> property of the logger.
	 * 
	 * @param debug the new value for the <code>isDebug</code> property.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}


	/**
	 * Gets the current value of the <code>isDebug</code> property.
	 * 
	 * @return the value of the <code>isDebug</code> property
	 */
	public boolean debug() {
		return debug;
	}		
}
