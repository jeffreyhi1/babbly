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

import java.util.Timer;
import java.util.TimerTask;

public class RegistrationScheduler{

	private SipRegistration registration = null;
	private Timer timer = new Timer(true);
	
	public RegistrationScheduler(SipRegistration registration){
		this.registration = registration;
	}
	
	
	public void start(long time){
		// the timer works with milliseconds therefore multiply the time by 1000
		timer.schedule(new KeepRegistrationTask(), time*1000);
	}
	
	public void shutdown(){
		timer.cancel();
	}
	

	private class KeepRegistrationTask extends TimerTask{
		@Override
		public void run() {
			registration.register();
		}

	}
}
