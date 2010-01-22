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
package org.babbly.core.config;

import java.util.Properties;


public class Configurator {

	static Properties properties = new Properties();
	
	/*
	 * A static constructor that will be called only the first time a
	 * method of this class is invoked.
	 */
	static{
		for (PropEnum propEnum : PropEnum.values()) {
			System.out.println("prop name: "+propEnum.getName());
			properties.put(propEnum.getName(), propEnum.getDefaultValue());
		}
	}
	

	public static void setProperties(Properties prop){
//		properties = prop;
		
		
		for (String propKey : prop.stringPropertyNames()) {
			for(PropEnum propEnum: PropEnum.values()){
				if(propEnum.getName().equals(propKey)){
					String propValue = prop.getProperty(propKey);
					if(propEnum.isValidValue(propValue)){
						properties.setProperty(propKey, propValue);
					}else{
						// log: that property value is not valid - using default
					}
				}else{
					// log: that property name is invalid - ignoring
				}
			}

		}
		
	}

	public static String get(String property){
		if(properties.containsKey(property)){
			return properties.getProperty(property);
		}
		return Property.UNDEFINED;
	}

	public static void set(String key, String value){
		if(!properties.containsKey(key)){
			properties.setProperty(key, value);	
		}
	}

	public static void update(String key, String value){
		properties.setProperty(key, value);	
	}
}
