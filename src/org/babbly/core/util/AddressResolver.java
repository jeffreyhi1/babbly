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

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Georgi Dimitrov
 * @version 0.1
 */
public class AddressResolver {

	/**
	 * Checks if the given ip or hostname exists and is reachable
	 * 
	 * @param Hostname  the hostname to look for
	 * @return  true if the given hostname exists, false otherwise
	 */
	public static boolean isExistingHostname(String Hostname){
		try {
			InetAddress.getByName(Hostname);
		} catch (UnknownHostException ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * Returns the name address of the local host (eg: computer-name) 
	 * 
	 * @return the local hostname if present, the loopback Address otherwise
	 */
	public static String getlocalHostname(){
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
	    } catch (UnknownHostException e) {
	    	return "localhost";
	    }
	    return addr.getHostName();
	}
	
	/**
	 * Returns the IP-Address of the local host
	 * 
	 * @return  the local IP if available, the loopback IP (127.0.0.1) otherwise
	 */
	public static String getLocalIpAddress(){
		InetAddress addr = null;
		try {
	        addr = InetAddress.getLocalHost();
	    } catch (UnknownHostException e) {
	    	return "127.0.0.1";
	    }
	    return addr.getHostAddress();
	}
}
