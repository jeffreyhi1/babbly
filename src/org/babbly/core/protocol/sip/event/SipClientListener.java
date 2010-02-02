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
package org.babbly.core.protocol.sip.event;

/**
 * @author Georgi Dimitrov
 * @version 0.3
 */
public interface SipClientListener {
	
	enum Error{WRONG_PASSWORD, INVALID_ADDR, CONNECTION_TIMEOUT, BUSY, REFUSED}
	
	public void onLogin();
	
	public void onLoginAuth();
	
	public void onError(Error error);
	
	public void onLogout();
	
	public void onConnecting();
}