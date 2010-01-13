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

import java.text.ParseException;
import java.util.ArrayList;

import javax.sip.ClientTransaction;
import javax.sip.Dialog;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.SipException;
import javax.sip.SipProvider;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.SipURI;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;

import org.babbly.core.util.AddressResolver;

/**
 * Represents the Caller(UAC) which is responsible for the 
 * initiation of the communication session between two sip applications, in 
 * other words the user who has to do the calling. The UAC works in that way,
 * that it first sends an INVITE to the Callee(UAS), who may decide to accept or
 * respectively decline the invitation for some reason. 
 * 
 * @author Georgi Dimitrov
 *
 */
public class UserAgentClient {

	private String protocol = "UDP";
	private static final String sdpData = 
		"v=0\r\n o=4855 13760799956958020 13760799956958020"
		+ " IN IP4  129.6.55.78\r\n" + "s=mysession session\r\n"
		+ "p=+46 8 52018010\r\n" + "c=IN IP4  129.6.55.78\r\n"
		+ "t=0 0\r\n" + "m=audio 6022 RTP/AVP 0 4 18\r\n"
		+ "a=rtpmap:0 PCMU/8000\r\n" + "a=rtpmap:4 G723/8000\r\n"
		+ "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n";

	private String fromUsername = null;
	private String fromHostname = null;
	private String fromScreenname = null;
	private int    fromPort = 0;
	private String fromIpAddress = null;
	
	private String toUsername = null;
	private String toHostname = null;
	private String toScreenname = null;
	private int    toPort = 0;

	private SipManager manager = null;

	private SipProvider provider = null;
	private ClientTransaction transaction = null;
	private Dialog dialog = null;

	//private Properties properties = null;


	/**
	 * Creates an UAC for the specified sip user and sip manager. This sip 
	 * manager contains the functionality to create, manage and send requests. 
	 * 
	 * @param caller - the user who has the role of 'caller'
	 * @param manager - a sip manager that cointains the elements needed of UAS	
	 */
	public UserAgentClient(SipUser caller, SipManager manager){
		this.manager = manager;
		provider = manager.getSipProvider();
		fromUsername = caller.getUsername();
		fromHostname = caller.getHostname();
		fromScreenname = caller.getScreenname();
		fromPort = caller.getPort();
		fromIpAddress = AddressResolver.getLocalIpAddress(); 
	}

	/**
	 * Creates an session invitation request with specific headers and sends it 
	 * to the specified remote UAS(the Callee). The UAS can optionally send an 
	 * 1xx response which informs the UAC for the progress of the invitation,
	 * and can accept the invitation by sending a 2xx response , or respectively
	 * reject it with a 3xx, 4xx, 5xx or 6xx response, depending on the reason
	 * for the rejection.
	 *  
	 * @param callee - the remote UAS which should be invited to participation
	 * @throws ParseException if the creation of the INVITE Request, 
	 * 									or the creation of it's headers fails 
	 */
	public void invite(SipUser callee) throws ParseException {
		toUsername = callee.getUsername();
		toHostname = callee.getHostname();
		toPort = callee.getPort();
		// create the request URI, which contains the callee's details
		SipURI requestURI = manager.createURI(toUsername, toHostname, toPort);
		
		// Create Headers
		FromHeader fromHeader = manager.createFromHeader(fromUsername,fromHostname+":"+fromPort, fromScreenname);
		ToHeader toHeader = manager.createToHeader(toUsername,toHostname, toScreenname);
		try {
			manager.addViaHeader(fromIpAddress, fromPort, protocol, null);
		} catch (InvalidArgumentException e) {}
		ArrayList<ViaHeader> viaHeaders = manager.getViaHeaders();
		CallIdHeader callIdHeader = provider.getNewCallId();	
		ContactHeader contactHeader = manager.createContactHeader(fromUsername, fromHostname, fromPort);

		// create the Request
		Request request = manager.createRequest(requestURI, Request.INVITE, callIdHeader, 
				fromHeader, toHeader, viaHeaders, sdpData, 20);
		request.addHeader(contactHeader);

		System.out.println("-------------------------------------------------------------------------");
		System.out.println("fromHeader = "+fromHeader);
		System.out.println("toHeader = "+toHeader);
		System.out.println("viaHeader = "+viaHeaders.get(0));
		System.out.println("request.getRequestURI() = "+request.getRequestURI());
		System.out.println("Listening point sentBy = "+provider.getListeningPoint("udp").getSentBy());
		System.out.println("-------------------------------------------------------------------------");
		// send the Request
		try {
			transaction = provider.getNewClientTransaction(request);
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		} 
		try {
			transaction.sendRequest();
		} catch (SipException e) {
			e.printStackTrace();
		}
		dialog = transaction.getDialog();
		System.out.println("UAC.invite: transaction = "+transaction);
	}

	/**
	 * Sends BYE Request to the remote party of the dialog and terminates the
	 * active session or the attempted session. When a BYE is received on a 
	 * dialog, any session associated with that dialog SHOULD terminate.
	 * 
	 */
	public void bye(){
		Request byeRequest;
		try {
			byeRequest = dialog.createRequest(Request.BYE);
			ClientTransaction transaction = provider.getNewClientTransaction(byeRequest);
			dialog.sendRequest(transaction);
		} catch (SipException e) {
			e.printStackTrace();
		}
		byeRequest = null;
	}
	
	/**
	 * Sends ACK Request to the remote party of the dialog. The application is
	 * functioning as User Agent Client. Does not increment the local 
	 * sequence number(cSeq).
	 * 
	 */
	public void acknowledge(){
		Request ackRequest;
		try {
			ackRequest = dialog.createRequest(Request.ACK);
			dialog.sendAck(ackRequest);
			transaction = null;
		} catch (SipException e) {
			e.printStackTrace();
		}
		ackRequest = null;
	}
	
	/**
	 * Creates a new Cancel message, used to cancel the previous 
     * request (which should be INVITE) sent by the client transaction. It asks 
     * the UAS to generate an error response to the INVITE request. A CANCEL 
     * request constitutes its own transaction. CANCEL has no effect on a 
     * request to which a UAS has already given a final(OK) response.
	 */
	public void cancel(){
		System.out.println("UAC.cancel: transaction = "+transaction);
		Request cancelRequest;
		try {
			cancelRequest = transaction.createCancel();
			ClientTransaction cancelTransaction = provider.getNewClientTransaction(cancelRequest);
			cancelTransaction.sendRequest();
			System.out.println("UAC.cancel: sending CANCEL with transaction = "+cancelTransaction);
		} catch (SipException e) {
			e.printStackTrace();
		}
		cancelRequest = null;
	}
	
	/**
	 * Terminates the sip client transaction and sets it to <code>null</code> 
	 * 
	 */
	public void terminateTransaction(){
		System.out.println("UAC terminateTransaction: "+transaction);
		try {
			transaction.terminate();
		} catch (ObjectInUseException e) {
			e.printStackTrace();
		}
		transaction = null;
	}

	public Dialog getDialog() {
		return dialog;
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
	}

}
