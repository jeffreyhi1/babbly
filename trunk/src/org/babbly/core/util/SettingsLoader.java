/*  babbly - lightweight instant messaging and VoIP client wirtten in Java. 
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

import org.babbly.core.protocol.sip.SipUser;



/**
 * A simple util Class, that manages the storing as also the loading of the
 * system configuration settings
 * 
 * @author Georgi Dimitrov
 *
 */
public class SettingsLoader {
	
	private static Properties properties = new Properties();
	private static String propertiesFile = null;

	/**
	 * Loads the application specific settings from the specified propertis file
	 * If the given file does not exists or could not be found, generates an 
	 * sample configuration with default values.
	 * 
	 * @param file - the file from wich the properties values should be loaded
	 * @return the newly loaded properties object
	 */
	public static Properties loadProperties(String file){
		propertiesFile = file;
		try {
			properties.load(new FileInputStream(new File(propertiesFile)));
		} catch (FileNotFoundException e) {
			properties.put("LISTEN_PORT","5070");
			properties.put("PROTOCOL","UDP");
			properties.put("USERNAME","guest");
			properties.put("SCREENNAME","Guest");
			properties.put("AUTO_ACCEPT_CALL","false");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
	
	/**
	 * Stores all of the options on the disk. First sets the corresponding 
	 * property values from the given options, and then writes the properties to
	 * the default properties file.
	 * 
	 * @param options - the options whose values should be saved to disk.
	 */
	public static void saveOptions(Options options){
		properties.setProperty("LISTEN_PORT","" + options.getListenPort());
		properties.setProperty("PROTOCOL",options.getNetworkProtocol());
		if(options.getLoggingFile() != null)
			properties.setProperty("LOGFILE",options.getLoggingFile());
		properties.setProperty("AUTO_ACCEPT_CALL","" + options.isAutoAcceptCall());
		storeSettings();
	}
	
	/**
	 * Writes the values of the properties object to the default properties file
	 * 
	 */
	public static void storeSettings(){
		try {
			FileOutputStream out = new FileOutputStream(propertiesFile);
			properties.store(out,null);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads an stored history-contacts file, e.g. all the valid sip addresses
	 * that the current user has called, or has tried to call are stored in this
	 * file. If the file contains erroneous values, then cuts them off and 
	 * leaves only the correct values. 
	 * 
	 * @return a list of all valid called sip numbers
	 */
	public static ArrayList loadCallList(){
		ArrayList<String> contacts = new ArrayList<String>();
		try {
			String readedLine = null;
			SipUser user = null;
			FileInputStream in = new FileInputStream("calllist");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
			while((readedLine = buffer.readLine()) != null){
				user = new SipUser(readedLine);
				if(user.isValidSipUser()){
					contacts.add(readedLine);
				}
			}
			in.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		if(contacts.isEmpty())
			contacts.add("");
		return contacts;
	}
	
	/**
	 * Stores all of the sip numbers(addresses) that have been called. If such 
	 * a file already exists, then append to the existing entries.
	 * NOTE: the values can be dublicated!(just as an normal phone bill)
	 * 
	 * @param calllist - the list of sip numbers that should be disk-saved
	 */
	public static void storeCallList(ArrayList<String> calllist){
		try {
			FileWriter out = new FileWriter("calllist");
			BufferedWriter writer = new BufferedWriter(out);
			
			for(int i = 0; i < calllist.size();i++){
				writer.append(calllist.get(i)+"\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	
	
}
