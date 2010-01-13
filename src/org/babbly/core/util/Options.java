/*  babbly - lightweight instant messaging and VoIP client written in Java. 
 * 
 *  Copyright (C) 2008  Georgi Dimitrov  mrj[at]abv[dot]bg
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

import java.util.Properties;

/**
 * @author Georgi Dimitrov
 *
 */
public class Options {
	
	Properties properties = null;
	
	private boolean autoAcceptCall;
	private String networkProtocol;
	private int listenPort;
	private String loggingFile;
	
	/**
	 * Creates new options and sets options values from the given properties 
	 * file.
	 * 
	 */
	public Options(Properties properties){
		this.properties = properties;
		setAutoAcceptCall(Boolean.parseBoolean(properties.getProperty("AUTO_ACCEPT_CALL")));
		setListenPort(Short.valueOf(properties.getProperty("LISTEN_PORT")));
		setNetworkProtocol(properties.getProperty("PROTOCOL"));
		setLoggingFile(properties.getProperty("LOGFILE"));
	}

	/**
	 * Checks whether the call is to be auto-accepted or not 
	 * 
	 * @return true if call is going to be autoaccepted, false otherwise
	 */
	public boolean isAutoAcceptCall() {
		return autoAcceptCall;
	}

	/**
	 * Sets the autoaccept call on or off.
	 * 
	 * @param autoAcceptCall - the new boolean value that is going to be set
	 */
	public void setAutoAcceptCall(boolean autoAcceptCall) {
		this.autoAcceptCall = autoAcceptCall;
	}

	public int getListenPort() {
		return listenPort;
	}

	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}

	public String getNetworkProtocol() {
		return networkProtocol;
	}

	public void setNetworkProtocol(String networkProtocol) {
		this.networkProtocol = networkProtocol;
	}
	
	public void saveOptions(){
		SettingsLoader.saveOptions(this);
	}

	public String getLoggingFile() {
		return loggingFile;
	}

	public void setLoggingFile(String loggingFile) {
		this.loggingFile = loggingFile;
	}
}
