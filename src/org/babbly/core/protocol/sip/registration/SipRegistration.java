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

import java.net.InetSocketAddress;
import java.text.ParseException;
import java.util.ListIterator;
import java.util.Observable;

import javax.imageio.spi.RegisterableService;
import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import javax.sip.TransactionUnavailableException;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ProxyAuthenticateHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.babbly.core.config.Conf;
import org.babbly.core.config.Property;
import org.babbly.core.net.InetAddresResolver;
import org.babbly.core.protocol.sip.SipManager;
import org.babbly.core.protocol.sip.SipUser;
import org.babbly.core.protocol.sip.event.SipObservable;




/**
 * @author Georgi Dimitrov (MrJ)
 * @version 0.3 - 29/01/2010
 */
public class SipRegistration extends SipObservable<RegistrationState> {

	private SipManager manager = null;

	private int expires;
	private Request authRequest;
	private SipUser user;
	private RegistrationScheduler scheduler;
	private RegistrationState state = RegistrationState.UNREGISTERED;
	
	public SipRegistration(SipManager manager){
		
		this.manager = manager; 
		expires = Integer.parseInt(Conf.get(Property.SIP_REQ_EXPIRE));
	}

	public void register(SipUser user){
		this.setState(RegistrationState.REGISTERING);
		this.user = user;
		String username = user.getUsername();
		String hostname = user.getHostname();
		String displayName = user.getScreenname();

		FromHeader fromHeader = null;
		ToHeader toHeader = null;
		CSeqHeader cSeq = null;


		try {
			fromHeader = manager.createFromHeader(username, hostname, displayName);
			cSeq = manager.createCseqHeader(Request.REGISTER);

			// rfc3261 states that the To header and the From header are equal
			// when dealing with REGISTER requests.
			toHeader = manager.createToHeader(username, hostname, displayName);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		CallIdHeader callIdHeader = manager.createNewCallId();

		InetSocketAddress publicAddress = 
			InetAddresResolver.getPublicAddress(InetAddresResolver.getRandomPortNumber());

		String publicIP = publicAddress.getAddress().getHostAddress();
		int publicPort = manager.getPort();
		String protocol = manager.getProtocol();

		manager.newViaHeaders();

		try {
			manager.addViaHeader(publicIP, publicPort, protocol, null);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (InvalidArgumentException e1) {
			e1.printStackTrace();
		}

		//MaxForwardsHeader - good idea is to set it to 70 to avoid loops (rfc3621)
		MaxForwardsHeader maxForwardsHeader = manager.createMaxForwardsHeader(70);

		// begin to prepare the Request Header //

		//first prepare domain address e.g "sip:biloxi.com"
		SipURI domainURI = (SipURI) toHeader.getAddress().getURI();
		String domainAddr = domainURI.getHost();

		// we don't need a user for this SipURI therefore user = null 
		SipURI requestURI = null;

		Request request = null;

		try {
			requestURI = manager.createURI(null, domainAddr);
			//requestURI.setPort(6070);//*****************************************
			request = manager.createRequest(
					requestURI, 
					Request.REGISTER,
					callIdHeader,
					cSeq,
					fromHeader,
					toHeader,
					manager.getViaHeaders(),
					maxForwardsHeader);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		//Expires Header
		ExpiresHeader expHeader = null;
		try {
			expHeader = manager.createExpiresHeader(expires);
			//TODO: DEFAULT_EXPIRE_TIME??? 
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}

		request.addHeader(expHeader);

		//Contact Header ( has to contain an IP)
		SipURI contactURI = null;

		try {
			contactURI = manager.createURI(user.getUsername(),
					user.getHostname());

			contactURI.setTransportParam(manager.getProtocol());
			contactURI.setPort(manager.getPort());
		} catch (ParseException e) {
			e.printStackTrace();
		}


		Address contactAddress = manager.createAddress(contactURI);

		if (user.getScreenname() != null) {
			try {
				contactAddress.setDisplayName(user.getScreenname());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		ContactHeader contactHeader
		= manager.createContactHeader(contactAddress);

		//add expires in the contact header as well in any case
		try {
			contactHeader.setExpires(expires);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}


		request.addHeader(contactHeader);


		//Transaction
		ClientTransaction regTrans = null;

		try {
			regTrans = manager.createNewClientTransaction(request);
			regTrans.sendRequest();
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		} catch (SipException e) {
			e.printStackTrace();
		}
		System.out.println("-Request send: "+request);
	}

	public void register() {
		this.register(user);
	}

	@SuppressWarnings("unchecked")
	public void authorize(Response response, ClientTransaction transaction){
		//this.setState(RegistrationState.AUTHENTICATING);

		int respStatusCode = response.getStatusCode();
		//String branchID = transaction.getBranchId();
		authRequest = (Request) transaction.getRequest().clone();
		
		ListIterator<WWWAuthenticateHeader> authHeaders = null;

		if (respStatusCode == Response.UNAUTHORIZED){
			authHeaders = response.getHeaders(WWWAuthenticateHeader.NAME);
		}
		else if (respStatusCode == Response.PROXY_AUTHENTICATION_REQUIRED){
			authHeaders = response.getHeaders(ProxyAuthenticateHeader.NAME);
		}
		//TODO: What if no authentication headers has been specified ?

		ViaHeader viaHeader = (ViaHeader) authRequest.getHeader(ViaHeader.NAME);
		authRequest.removeHeader(ViaHeader.NAME);
		ViaHeader refreshedViaHeader = null;

		try {
			refreshedViaHeader = manager.createViaheader(
					viaHeader.getHost(), viaHeader.getPort(),
					viaHeader.getTransport(), null);
			//TODO: try to clone and remove branch instead!
		} catch (ParseException e2) {
			e2.printStackTrace();
		} catch (InvalidArgumentException e2) {
			e2.printStackTrace();
		}

		authRequest.setHeader(refreshedViaHeader);
		authRequest.removeHeader(AuthorizationHeader.NAME);
		authRequest.removeHeader(ProxyAuthorizationHeader.NAME);

		try {
			manager.incrementCSeq(authRequest);
			//			reRegisterRequest.addHeader(cSeq);
		} catch (InvalidArgumentException e1) {
			e1.printStackTrace();
		}

		ClientTransaction reRegisterTransaction = null;
		try {
			reRegisterTransaction = manager.createNewClientTransaction(authRequest);
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		}


		WWWAuthenticateHeader authHeader = null;

		while (authHeaders.hasNext()){
			authHeader = (WWWAuthenticateHeader) authHeaders.next();
			String realm = authHeader.getRealm();

			AuthorizationHeader authorization = 
				manager.createAuthorizationHeader(authRequest, authHeader, user);

			//			CallIdHeader call = (CallIdHeader)reoriginatedRequest
			//			.getHeader(CallIdHeader.NAME);
			authRequest.addHeader(authorization);
			//updateRegisterSequenceNumber(retryTran);

			System.out.println(authRequest);
			try {
				reRegisterTransaction.sendRequest();
			} catch (SipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}
	
	public void keepAlive(Response response) {
		scheduler = new RegistrationScheduler(this);
		
		ExpiresHeader expiresHeader = response.getExpires();
		ContactHeader contactHeader = (ContactHeader) response
		.getHeader(ContactHeader.NAME);
		
		if (expiresHeader != null)
			expires = expiresHeader.getExpires();
		else if(contactHeader != null){
			expires = contactHeader.getExpires();
		}
		scheduler.start(expires/2);
	}

	public void unregister(){
		this.setState(RegistrationState.UNREGISTERING);
		
		Request unregister = (Request) authRequest.clone();

		try{
			unregister.getExpires().setExpires(0);
			manager.incrementCSeq(unregister);

			ViaHeader viaHeader
			= (ViaHeader)unregister.getHeader(ViaHeader.NAME);
			if(viaHeader != null)
				viaHeader.removeParameter("branch");

		}catch (InvalidArgumentException ex){
			//TODO: catch me ! :)
		}
		ContactHeader contact
		= (ContactHeader)unregister.getHeader(ContactHeader.NAME);
		try {
			contact.setExpires(0);
		} catch (InvalidArgumentException e1) {
			e1.printStackTrace();
		}

		CallIdHeader call = (CallIdHeader)
		unregister.getHeader(CallIdHeader.NAME);
		//String callid = call.getCallId();

		//		AuthorizationHeader authorization = 
		//			manager.createAuthorizationHeader(
		//				unregister, authHeader, user);

		//		if(authorization != null)
		//			unregisterRequest.addHeader(authorization);

		ClientTransaction transaction = null;
		System.out.println(unregister);
		try {
			transaction = manager.createNewClientTransaction(unregister);
		} catch (TransactionUnavailableException e) {
			e.printStackTrace();
		}
		try {
			transaction.sendRequest();
		} catch (SipException e) {
			e.printStackTrace();
		}
		scheduler.shutdown();
	}
	
	public RegistrationState getState() {
		return state;
	}

	public void setState(RegistrationState state) {
		this.state = state;
		notifyObservers(state);
	}

}
