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
package org.babbly.core.protocol.sip;



/**
 * This interface serves the purpose of handling all possible feedback messages
 * which are meant for the end user to receive in order for the program to
 * provide that user with comprehensive understanding for itself and the actions
 * it is
 * taking.
 * 
 * @author Georgi Dimitrov (MrJ)
 *
 */
public interface FeedbackListener {
	
	
	/**
	 * Received by the <code>FeedbackListener</code> when the user has to be 
	 * informed about specific actions that are being currently taken or when
	 * user interrogation is needed.
	 * 
	 * @param string
	 */
	public void onFeedbackMessage(String string);
	
	
	

}
