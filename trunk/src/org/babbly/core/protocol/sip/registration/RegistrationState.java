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
package org.babbly.core.protocol.sip.registration;

/**
 * The register states are a way to easily distinguish between the 
 * different situations that are occurring. They provide an effective way of
 * collaboration between the different parts of the program, since by
 * knowing what is the current state one can eventually more effectively
 * deal with the current situation.
 * 
 * @author Georgi Dimitrov (MrJ)
 * @version 0.1
 */
public enum RegistrationState {

	
	
	/**
	 * The possible states of the <code>RegistrationStates</code> enum.
	 */
	REGISTERING("registering"), AUTHENTICATING("authenticating"),
	REGISTERED("registered"),UNREGISTERING("unregistering"),
	UNREGISTERED("unregistered"),UNSPECIFIED(null), 
	AUTHENTICATION_FAILED("authentication failed");

	/**
	 * Contains a more human readable description of the state.
	 */
	String text = null;

	
	/**
	 * Constructs a new <code>RegistrationStates</code> enumeration object with
	 * the specified human readable text. 
	 * <p>
	 * <b>Note:</b> This constructor is an enum constructor and hence not 
	 * accessible outside the <code>RegistrationStates</code> class.
	 * 
	 * @param text the specified user friendly text to be used for the creation
	 * of an enumeration object.
	 */
	RegistrationState(String text){
		this.text = text;
	}
	
	
	
}
