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

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.CSeqHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;






/**
 * @author Georgi Dimitrov (MrJ)
 * @version 0.2 - June 6th 2008
 */
public class SipRegistrationtListener implements SipListener{

	private SipRegistration registration = null;

	/**
	 * Constructs a <code>SipRegistrationListener</code>
	 * 
	 * @param registration the initiator of the manager request.
	 */
	public SipRegistrationtListener(SipRegistration registration){
		this.registration = registration;
	}

	@Override
	public void processDialogTerminated(DialogTerminatedEvent e) {
		System.out.println("processDialogTerminated");

	}

	@Override
	public void processIOException(IOExceptionEvent e) {
		System.out.println("processIOException");

	}

	@Override
	public void processRequest(RequestEvent e) {
		System.out.println("processRequest");

	}

	@Override
	public void processResponse(ResponseEvent e) {
		System.out.println("Response: "+e.getResponse());
		System.out.println("processResponse");

		Response response = e.getResponse();
		CSeqHeader cSeq = (CSeqHeader) response.getHeader(CSeqHeader.NAME);
		String method = cSeq.getMethod();

		// if it is not a response to the REGISTER request we don't handle it!
		if(method != Request.REGISTER){
			return;
		}

		int statusCode = response.getStatusCode();

		ClientTransaction clientTransaction = e.getClientTransaction();

		//OK
		if (statusCode == Response.OK) {
			switch(registration.getState()){
			case AUTHENTICATING:
			case REGISTERING:
				registration.keepAlive(response);
				registration.setState(RegistrationState.REGISTERED);
				break;
			case UNREGISTERING:
				registration.setState(RegistrationState.UNREGISTERED);
				break;
			default: // in every other case ignore it
				break;
			}
		}
		//TRYING
		else if (statusCode == Response.TRYING) {
			registration.setState(RegistrationState.REGISTERING);
		}
		//401 UNAUTHORIZED
		else if (statusCode == Response.UNAUTHORIZED
				|| statusCode == Response.PROXY_AUTHENTICATION_REQUIRED){

			switch(registration.getState()){
			case AUTHENTICATING: // so the authorization has failed - wrong password!
				registration.setState(RegistrationState.AUTHENTICATION_FAILED);
				registration.setState(RegistrationState.UNREGISTERED);
				break;
			case UNREGISTERING: // so the authorization has failed - wrong password!
				registration.authorize(response, clientTransaction);
				break;
			default: // in every other case try to authorize
				registration.authorize(response, clientTransaction);
				registration.setState(RegistrationState.AUTHENTICATING);
				break;
			}
			// SO A Registration information is needed here !!
		}
		//403 FORBIDDEN
		else if (statusCode == Response.FORBIDDEN){
			// !?!?!?!?
			System.out.println("FORBIDDEN");
		}
		//an 4xx error other than FORBIDDEN and UNAUTHORIZED !
		else if (statusCode / 100 == 4 ){
			// Handle ERR
			System.out.println("ERROR 4xx");
		}
		//NOT_IMPLEMENTED
		else if (statusCode == Response.NOT_IMPLEMENTED) {
			// handle NOT_IMPLEMENTED Resp.
			System.out.println("NOT_IMPLEMENTED");
		}
		//ignore everything else.

	}

	@Override
	public void processTimeout(TimeoutEvent e) {
		System.out.println("processTimeout");
	}

	@Override
	public void processTransactionTerminated(TransactionTerminatedEvent e) {
		//if(e.isServerTransaction())
		System.out.println("processTransactionTerminated");

		Transaction transaction = null;
		if(e.isServerTransaction())
			transaction = e.getServerTransaction();
		else
			transaction = e.getClientTransaction();

		if (transaction == null) {
			// ignoring this event
			return;
		}

		Request request = transaction.getRequest();
		// the transaction for request $xyz has been terminated
		//System.out.println("TerminatedTrans for Req: "+request.getExpires()+"\n---------------");
	}

}
