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

import gov.nist.javax.sip.header.AuthenticationHeader;

import javax.sip.ClientTransaction;
import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipListener;
import javax.sip.TimeoutEvent;
import javax.sip.Transaction;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.babbly.ui.gui.controller.PrimaryWindowController;






/**
 * @author Georgi Dimitrov (MrJ)
 * @version 0.2 - June 6th 2008
 */
public class SipRegistrationtListener implements SipListener{


	private SipRegistration registration = null;
	
	/**
	 * Constructs a <code>SipRegistrationListener</code> with the specified
	 * <code>SipRegistration</code> that initiated the registration request.
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
		System.out.println("processResponse");

		ClientTransaction clientTransaction = e.getClientTransaction();

		Request request = clientTransaction.getRequest();
		Response response = e.getResponse();
		int respStatusCode = response.getStatusCode();

//		Dialog dialog = clientTransaction.getDialog();
//		String method = ( (CSeqHeader) response.getHeader(CSeqHeader.NAME)).
//		getMethod();
//		Response responseClone = (Response) response.clone();
//		SipProvider sourceProvider = (SipProvider)responseEvent.getSource();

		//OK
		if (respStatusCode == Response.OK) {
			// processOK !

			int expirationInterval = 0;

			//------------------------------------------------------------------
			//ExpiresHeader expiresHeader = request.getExpires();
			ExpiresHeader expiresHeader = response.getExpires();
			request.setExpires(expiresHeader);
			//------------------------------------------------------------------
			
			ContactHeader contactHeader = (ContactHeader) request
			.getHeader(ContactHeader.NAME);

			if (expiresHeader != null)
				expirationInterval = expiresHeader.getExpires();
			else if(contactHeader != null){
				expirationInterval = contactHeader.getExpires();
			}

			if(registration.registerState == RegisterState.AUTHENTICATING){
				PrimaryWindowController.statusMessage("Connected");
				PrimaryWindowController.statusRunning(false);

				// keep alive the registration re-sending an registration request
				// slightly before the current registration expires 
				registration.keepRegistrationAlive(expirationInterval-100);
				PrimaryWindowController.showCallPane();
			}
			else if(registration.getRegisterState() == RegisterState.UNREGISTERING){
				registration.setRegisterState(RegisterState.UNREGISTERED);
				PrimaryWindowController.statusMessage("Not Connected");
				PrimaryWindowController.statusRunning(false);
				PrimaryWindowController.showLoginPane();
				PrimaryWindowController.loginEnabled(true);
				
			}

			System.out.println("OK");
		}
		//TRYING
		else if (respStatusCode == Response.TRYING) {
			System.out.println("TRYING");
			if(registration.getRegisterState() == RegisterState.REGISTERING){
				PrimaryWindowController.statusMessage("Connecting...");
			}
		}
		//401 UNAUTHORIZED
		else if (respStatusCode == Response.UNAUTHORIZED
				|| respStatusCode == Response.PROXY_AUTHENTICATION_REQUIRED){

			System.out.println(response.getReasonPhrase());
			System.out.println(respStatusCode);
			System.out.println("UNAUTHORIZED");

			if(registration.getRegisterState() == RegisterState.REGISTERING){
				PrimaryWindowController.statusMessage("Trying to authenticate...");
				registration.authorize(response, clientTransaction);
			}
			else if(registration.getRegisterState() == RegisterState.REGISTERED){
				//PrimaryWindowController.statusMessage("Trying to authenticate...");
				registration.authorize(response, clientTransaction);
			}
			else if(registration.getRegisterState() == RegisterState.AUTHENTICATING){
				PrimaryWindowController.feedbackMessage("Wrong password!");
				PrimaryWindowController.loginEnabled(true);
				PrimaryWindowController.statusRunning(false);
				PrimaryWindowController.statusMessage("Not Connected");
				registration.setRegisterState(RegisterState.UNREGISTERED);
			}
			else if(registration.getRegisterState() == RegisterState.UNREGISTERING){
				System.out.println("UNREGISTERING - UNAUTH - authorize!");
				registration.authorize(response, clientTransaction);
				registration.setRegisterState(RegisterState.UNREGISTERING);
//				System.out.println("UNREGISTERING - UNAUTH - authorize! -> Register!!!!");
//				registration.unregister();
			}

			// SO A Registration information is needed here !!


		}
		//403 FORBIDDEN
		else if (respStatusCode == Response.FORBIDDEN){
			// !?!?!?!?
			System.out.println("FORBIDDEN");
		}
		//an 4xx error other than FORBIDDEN and UNAUTHORIZED !
		else if (respStatusCode / 100 == 4 ){
			// Handle ERR
			System.out.println("ERROR 4xx");
		}
		//NOT_IMPLEMENTED
		else if (respStatusCode == Response.NOT_IMPLEMENTED) {
			// handle NOT_IMPLEMENTED Resp.
			System.out.println("NOT_IMPLEMENTED");
		}
		//ignore everything else.

	}

	@Override
	public void processTimeout(TimeoutEvent e) {
		System.out.println("processTimeout");
		PrimaryWindowController.statusMessage("Connection timed out");
		PrimaryWindowController.statusRunning(false);
		PrimaryWindowController.loginEnabled(true);
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
