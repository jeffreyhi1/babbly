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

public class Configurator {

	public static final String UNDEFINED_PROPERTY = "property.undefined";
	
	static Properties properties = new Properties();
	
	
	//TODO: Constructor
	
	public static String getConfigProperty(String property){
		//if(properties.con)
		if(properties.containsKey(property)){
			return properties.getProperty(property);
		}
		return UNDEFINED_PROPERTY;
	}
}
