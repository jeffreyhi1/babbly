/*  babbly - lightweight instant messaging and VoIP client written in Java. 
 * 
 *  Copyright (C) 2010  Georgi Dimitrov  mrj[at]abv[dot]bg
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


/**
 * This interface contains all currently supported constants. In other words
 * the property names that are accepted from the <code>Configurator</code>.
 * The constants are grouped in small portions(network, UI, etc.)
 * 
 * @author Georgi Dimitrov (MrJ)
 *
 */
public enum PropertyRule {
	
	//UNDEFINED(Property.UNDEFINED, null, null),
	STUN_ADDRESS(Property.STUN_ADDRESS, Property.Pattern.STUN_ADDRESS, Property.STUN_ADDRESS_DEFAULT),
	STUN_PORT(Property.STUN_PORT, Property.Pattern.STUN_PORT, Property.STUN_PORT_DEFAULT),
	BIND_RETRIES(Property.BIND_RETRIES, Property.Pattern.BIND_RETRIES, Property.BIND_RETRIES_DEFAULT),
	RESOLV_DEST(Property.RESOLV_DEST, Property.Pattern.RESOLV_DEST, Property.RESOLV_DEST_DEFAULT),
	TRANSPORT_PROTOCOL(Property.TRANSPORT_PROTOCOL, Property.Pattern.TRANSPORT_PROTOCOL, Property.TRANSPORT_PROTOCOL_DEFAULT),
	JAIN_SIP_PATH(Property.JAINSIP_PATH, Property.Pattern.JAINSIP_PATH, Property.JAINSIP_PATH_DEFAULT),
	JAIN_SIP(Property.JAINSIP_STACKNAME, Property.Pattern.JAINSIP_STACKNAME, Property.JAINSIP_STACKNAME_DEFAULT),
	SIP_BIND_PORT(Property.SIP_BIND_PORT, Property.Pattern.SIP_BIND_PORT, Property.SIP_BIND_PORT_DEFAULT),
	SIP_REQ_EXPIRE(Property.SIP_REQ_EXPIRE, Property.Pattern.SIP_REQ_EXPIRE, Property.SIP_REQ_EXPIRE_DEFAULT);

	private String name = null;
	private String pattern = null;
	private String defaultValue = null;

	PropertyRule(String name, String pattern, String defaultValue){
		this.name = name;
		this.pattern = pattern;
		this.defaultValue = defaultValue;
	}
	
	public boolean isValidValue(String value){
		if(value.matches(pattern)){
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
}
