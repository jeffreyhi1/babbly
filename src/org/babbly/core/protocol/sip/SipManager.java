/*  babbly - lightweight instant messaging and VoIP client wirtten in Java. 
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
import java.util.Properties;
import java.util.Random;

import javax.sip.InvalidArgumentException;
import javax.sip.ListeningPoint;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.SipFactory;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TransportNotSupportedException;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.AuthorizationHeader;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;
import javax.sip.header.MaxForwardsHeader;
import javax.sip.header.ProxyAuthenticateHeader;
import javax.sip.header.ProxyAuthorizationHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.babbly.core.authorization.DigestAlgorithm;
import org.babbly.core.util.Base64;






/**
 * This class is intended to be used as an Manager of the SIP components. It 
 * should make the underlying complexity of SIP transperent to the user,
 * and allow the user to create UAC and UAS easily without need of knowing
 * what's done 'behind the scene'.
 * 
 * @author Georgi Dimitrov
 * @version 0.3
 *
 */
public class SipManager {

	private static final String DEFAULT_LOG_LEVEL = "0";

	private AddressFactory addressFactory = null;
	private MessageFactory messageFactory = null;
	private HeaderFactory  headerFactory  = null;
	private SipFactory	   sipFactory	  = null;
	private SipStack 	   sipStack		  = null;
	private SipProvider    sipProvider 	  = null;

	private ArrayList<ViaHeader> viaHeaders = null;
	private Properties	   		 properties = null;

	private ListeningPoint listeningPoint = null;

	private Random randomInt = new Random();


	/**
	 * Constructs the SipManager object. Initializes the main components of the
	 * sip manager class, the sip stack and turns logging on/off.
	 * 
	 * @param stackname - the name of the sip stack 
	 * @param logfile - path of the logfile(if <code>null<code>, no logging)
	 * @throws PeerUnavailableException if the initinialization of the sip 
	 * 		   manager's components fails (peer class not found)
	 */
	public SipManager(String stackname, String logfile) throws PeerUnavailableException{
		init(stackname, logfile);
	}

	/**
	 * Constructs the SipManager object from specified Properties file
	 * 
	 * @param properties - the properties file 
	 * @throws PeerUnavailableException if the initinialization of the sip 
	 * 		   manager's components fails (peer class not found)
	 */
	public SipManager(Properties properties) throws PeerUnavailableException{
		init(properties);
	}

	/**
	 * Initializes the <code>SipManager</code> components for later use.
	 * Sets a bunch of propertis and creates instances of a SipFactory and
	 * SipStack implementations based on those properties.
	 * The SipFactory is than used to create the address, header and message
	 * Factories. The SipStack will be later used to create a sip provider. 
	 *  
	 * @throws PeerUnavailableException if the peer class could not be found
	 */
	private void init(String stackname, String logfile) throws PeerUnavailableException {
		properties = new Properties();
		properties.setProperty("javax.sip.STACK_NAME", stackname);

		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist"); // the path where the implementation of JAIN SIP is to be found

		if(logfile == null){
			//do not log anything by default (if no logging file has been specified)
			//properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", DEFAULT_LOG_LEVEL); 
		}
		else{
			//if a logging file has been specified, log the trace
			properties.setProperty("gov.nist.javax.sip.SERVER_LOG",logfile);
			properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "16"); // do logging(16-logging, 32-debug)
		}
		sipStack = sipFactory.createSipStack(properties); // creates the sipstack from the 
		// properties we specified
		headerFactory = sipFactory.createHeaderFactory();
		addressFactory = sipFactory.createAddressFactory();
		messageFactory = sipFactory.createMessageFactory();
	}

	/**
	 * Initializes the <code>SipManager</code> components for later use.
	 * Sets a bunch of propertis and creates instances of a SipFactory and
	 * SipStack implementations based on those properties.
	 * The SipFactory is than used to create the address, header and message
	 * Factories. The SipStack will be later used to create a sip provider. 
	 *  
	 * @throws PeerUnavailableException if the peer class could not be found
	 */
	private void init(Properties properties) throws PeerUnavailableException {
		this.properties = properties;

		sipFactory = SipFactory.getInstance();
		sipFactory.setPathName("gov.nist"); // the path where the implementation of JAIN SIP is to be found

		sipStack = sipFactory.createSipStack(properties); // creates the sipstack from the 
		// properties parameter
		headerFactory = sipFactory.createHeaderFactory();
		addressFactory = sipFactory.createAddressFactory();
		messageFactory = sipFactory.createMessageFactory();
	}

	/**
	 * Creates From Header from the specified username, sip-addres and sets it's
	 * displayname to the specified value. That From Header will be put in the 
	 * body of the request, so the user to which this request is to be sent
	 * can see who sended it.
	 *
	 * @param fromUsername	  -  the Username of the user 
	 * @param fromSipAddress  -  the SIP-Address/Hostname of the user 
	 * @param fromDisplayName -  the Display name of the user(can be any string)
	 * @return  the newly created FromHeader object
	 * @throws ParseException signals that an error has been reached
	 * 						   	unexpectedly while parsing the address values.	
	 */
	public FromHeader createFromHeader(String fromUsername, String fromSipAddress,
			String fromDisplayName) throws ParseException {

		SipURI fromAddress = createURI(fromUsername, fromSipAddress, -1);

		Address fromNameAddress = addressFactory.createAddress(fromAddress);
		fromNameAddress.setDisplayName(fromDisplayName);

		// creates the From header from the Address containing the "from" user details and a specific Tag
		// Usually the current program is supposed to specify the Tag value. It is not obligatory for the numbers
		// to be in ascending order.
		FromHeader fromHeader = headerFactory.createFromHeader(fromNameAddress,
				Integer.toHexString(randomInt.nextInt()));
		return fromHeader;
	}

	/**
	 * Creates To Header from the specified username, sip-addres and sets it's
	 * displayname to the specified value. The To-Header specifies the user to
	 * which a request is going to be send. 
	 * 
	 * @param toUser 		-	the Username of the target user
	 * @param toSipAddress	-	the SIP-Address/Hostname of the target user
	 * @param toDisplayName	-	the Display name of the target user
	 * @return					the newly created FromHeader object
	 * @throws ParseException	if error occurs in address/header creation
	 */
	public ToHeader createToHeader(String toUser, String toSipAddress, String toDisplayName)
	throws ParseException{

		// create To Header
		SipURI toAddress = createURI(toUser, toSipAddress, -1);
		Address toNameAddress = addressFactory.createAddress(toAddress);
		toNameAddress.setDisplayName(toDisplayName);
		ToHeader toHeader = headerFactory.createToHeader(toNameAddress, null);
		return toHeader;
	}

	public ContactHeader createContactHeader(String fromName, String host, int port) 
	throws ParseException{

		// Create the contact name address.
		SipURI contactURI = createURI(fromName, host, port);

		Address contactAddress = addressFactory.createAddress(contactURI);
		// Add the contact address.
		contactAddress.setDisplayName(fromName);
		ContactHeader contactHeader = headerFactory.createContactHeader(contactAddress);

		return contactHeader;
	}


	public ContactHeader createContactHeader(Address address){
		return headerFactory.createContactHeader(address);
	}


	/**
	 * Creates a SipURI based on the given User, Host and Port components. 
	 * The user component may be null. If the port component is less than 0
	 * no port is going to be specified.
	 *
	 * @param toUser - the new string value of the user, this value may be null
	 * @param peerHost - the new string value of the host.
	 * @param peerPort - the host's port(if less than 0 no port is set) 
	 * @return 			 the newly created URI object
	 * @throws ParseException if error occurs in uri's creation
	 */
	public SipURI createURI(String toUser, String peerHost, int peerPort) 
	throws ParseException{

		SipURI sipURI = addressFactory.createSipURI(toUser, peerHost);
		if(peerPort > 0){
			sipURI.setPort(peerPort);
		}

		return sipURI;
	}


	/**
	 * Creates a SipURI based on the given User, Host and Port components. 
	 * The user component may be null.
	 *
	 * @param toUser - the new string value of the user, this value may be null
	 * @param peerHost - the new string value of the host.
	 *  
	 * @return 			 the newly created URI object
	 * @throws ParseException if error occurs in uri's creation
	 */
	public SipURI createURI(String toUser, String peerHost) 
	throws ParseException{
		SipURI sipURI = addressFactory.createSipURI(toUser, peerHost);
		return sipURI;
	}

	/**
	 * Creates a via header from the specified arguments and adds it to the 
	 * newly created arraylist of via headers. 
	 * 
	 * @throws InvalidArgumentException if an specified argument is invalid
	 * @throws ParseException signals that an error has been reached
	 * unexpectedly while parsing the host, port, protocol or branch value. 
	 * 
	 */
	public void addViaHeader(String host, int port, String protocol, String branch) 
	throws ParseException, InvalidArgumentException{
		// gets the via headers(if null then creates new ViaHeaders-ArrayList)
		if(viaHeaders == null){
			viaHeaders = new ArrayList<ViaHeader>();
		}

		ViaHeader viaHeader = headerFactory.createViaHeader(
				host, port, protocol, branch);
		// adds the via headers
		viaHeaders.add(viaHeader);
	}


	/**
	 * Creates a via header from the specified arguments.
	 * 
	 * @param host the hostname of the via header
	 * @param port the port of the via header
	 * @param protocol the transport protocol of the via header
	 * @param branch the branch id of the via header
	 * @return the newly created <code>ViaHeader</code>
	 * 
	 * @throws ParseException if an error is reached unexpected while parsing
	 * @throws InvalidArgumentException if some invalid argument has been 
	 * specified
	 */
	public ViaHeader createViaheader(
			String host, int port, String protocol, String branch) 
	throws ParseException, InvalidArgumentException{

		return headerFactory.createViaHeader(
				host, port, protocol, branch);
	}

	/**
	 * Gets the instance of the ArrayList containing via headers. If it's <code>
	 * null</code>
	 * then creates new empty ArrayList for the via headers.
	 * 
	 * @return	an array of all added via headers.
	 */
	public ArrayList<ViaHeader> getViaHeaders() {
		if(viaHeaders == null){
			viaHeaders = new ArrayList<ViaHeader>();
		}
		return viaHeaders;
	}


	public void newViaHeaders() {
		viaHeaders = new ArrayList<ViaHeader>();
	}

	/**
	 * Creates a new RouteHeader based on the newly supplied sip uri value.
	 *
	 * @param sipUri - the newly supplied value of the SipURI
	 * @return  	   the newly created RouteHeader object.  
	 */
	public RouteHeader createRouteHeader(SipURI sipUri){
		RouteHeader routeHeader =
			headerFactory.createRouteHeader(addressFactory.createAddress(sipUri));
		return routeHeader;
	}

	/**
	 * Creates a new ContentTypeHeader based on the newly supplied contentType 
	 * and contentSubType values.
	 *  
	 * @throws ParseException if an error has occured while parsing the values
	 */
	public ContentTypeHeader createContentTypeHeader(String type, String subType)
	throws ParseException{

		//TODO wo should consider putting that piece of code in another method, say in "createRequest"
		// Create ContentTypeHeader
		ContentTypeHeader contentTypeHeader = headerFactory
		.createContentTypeHeader(type, subType);

		return contentTypeHeader;

	}

	/**
	 * Creates a new CSeqHeader based on the newly supplied sequence number and 
	 * method values. If the specified secuence is less than 0, it is set to
	 * default value of 0.
	 *  
	 * @param sequence - the new long value of the sequence number.
	 * @param requestType - the new string value of the request type.
	 * @return the newly created CSeqHeader object
	 * @throws ParseException if an error has occured while parsing the values
	 */
	public CSeqHeader createCseqHeader(long sequence, String requestType) 
	throws ParseException {
		// Create a new Cseq header
		CSeqHeader cSeqHeader = null;
		if(sequence < 0){sequence = 0;}
		try {
			cSeqHeader = headerFactory.createCSeqHeader(sequence, requestType);
		} catch (InvalidArgumentException e) { }

		return cSeqHeader;
	}

	/**
	 * Creates a new MaxForwardsHeader based on the newly supplied max value. If
	 * the specified max value is less than 0 or greater than 255, it is set to
	 * default value of 10.
	 * 
	 * @param max - the new integer value of the max forwards
	 * @return the newly created MaxForwardsHeader object.
	 */
	public MaxForwardsHeader createMaxForwardsHeader(int max) {
		//TODO maybe we do not need such method. Consider Refactoring !!!!
		// Create a new MaxForwardsHeader
		MaxForwardsHeader maxForwards = null;
		try {
			if(max > 0 && max < 255){
				maxForwards = headerFactory.createMaxForwardsHeader(max);
			}
			else{
				maxForwards = headerFactory.createMaxForwardsHeader(10);
			}
		} catch (InvalidArgumentException e) {}

		return maxForwards;
	}

	/**
	 * Creates a new custom Header based on the newly supplied name and value.
	 * 
	 * @param headerName - the name of the header
	 * @param headerValue - the value of the header
	 * @return the newly created Header object
	 * @throws ParseException if an error has occured while parsing the values
	 */
	public Header createHeader(String headerName, String headerValue) throws ParseException{
		// Add the extension header.
		Header header = headerFactory.createHeader(headerName, headerValue);

		return header;
	}

	/**
	 * Creates a listening point from a given IP address, port and protocol and 
	 * uses it to create a sip provider associated with it in started state.
	 * If this method is used, the IP address of the stack is ignored and a
	 * listening point is created with the given parameters. 
	 *  
	 * @param 	host the hostname of the listening point of the sip provider
	 * @param 	port the port of the listening point of the sip provider
	 * @param 	protocol the protocol of the listening point of the sip provider 
	 * @throws  ObjectInUseException if another SipProvider is already 
	 * 			associated with the specified address.
	 * @return	the newly created sip provider object
	 * @throws  TransportNotSupportedException  if protocol is not supported
	 * @throws  InvalidArgumentException	 if some of the arguments is invalid
	 */
	public SipProvider createSipProvider(String host, int port, String protocol) 
	throws ObjectInUseException, TransportNotSupportedException, InvalidArgumentException {
		listeningPoint = sipStack.createListeningPoint(host, port, protocol);
		System.out.println("ListeningPoint sentby: "+listeningPoint.getSentBy());
		sipProvider = sipStack.createSipProvider(listeningPoint);
		return sipProvider;
	}

	/**
	 * Creates new Request message of type specified by the method paramater,
	 * containing the URI of the Request, the mandatory headers of the message
	 * with a body in the form of a Java object and the body content type.
	 *
	 * @param requestURI  the new URI object of the requestURI value of this Message.
	 * @param method  the new string of the method value of this Message.
	 * @param callId  the new CallIdHeader object of the callId value of this Message.
	 * @param cSeq  the new CSeqHeader object of the cSeq value of this Message.
	 * @param from  the new FromHeader object of the from value of this Message.
	 * @param to  the new ToHeader object of the to value of this Message.
	 * @param via  the new List object of the ViaHeaders of this Message.
	 * @param contentType  the new ContentTypeHeader object of the content type 
	 * value of this Message.
	 * @param content  the new Object of the body content value of this Message.
	 * @return  the newly created Request object.
	 * @throws  ParseException which signals that an error has been reached
	 * 					unexpectedly while parsing the method or the body.
	 */
	public Request createRequest(URI requestURI, String method, CallIdHeader 
			callId, FromHeader from, ToHeader to, ArrayList<ViaHeader> via,
			String sdpData, int maxForward) throws ParseException{

		ContentTypeHeader contentType = 
			createContentTypeHeader("application", "sdp");

		MaxForwardsHeader maxForwards = createMaxForwardsHeader(maxForward);
		CSeqHeader cSeqHeader = createCseqHeader(1L, method);
		Object content = sdpData.getBytes();
		Request request = messageFactory.createRequest(requestURI, method, callId,
				cSeqHeader, from, to, via, maxForwards, contentType, content);

		return request;
	}

	/**
	 * Creates new Request message of type specified by the method paramater,
	 * containing the URI of the Request, the mandatory headers of the message
	 * with a body in the form of a Java object and the body content type.
	 *
	 * @param requestURI  the new URI object of the requestURI value of this Message.
	 * @param callId  the new CallIdHeader object of the callId value of this Message.
	 * @param cSeq  the new CSeqHeader object of the cSeq value of this Message.
	 * @param from  the new FromHeader object of the from value of this Message.
	 * @param to  the new ToHeader object of the to value of this Message.
	 * @param via  the new List object of the ViaHeaders of this Message.
	 * @param contentType  the new ContentTypeHeader object of the content type 
	 * value of this Message.
	 * @param content  the new Object of the body content value of this Message.
	 * @return  the newly created Request object.
	 * @throws  ParseException which signals that an error has been reached
	 * 					unexpectedly while parsing the method or the body.
	 */
	public Request createRequest(SipURI requestURI, String method, CallIdHeader 
			callId, CSeqHeader cSeq, FromHeader from, ToHeader to, 
			ArrayList<ViaHeader> via, MaxForwardsHeader maxForward)
	throws ParseException{

		Request request = messageFactory.createRequest(
				requestURI,
				method,
				callId,
				cSeq,
				from,
				to,
				via,
				maxForward);

		return request;
	}


	/**
	 * Create an <code>AuthorizationHeader</code> which is used by the UAC to
	 * re-register itself by responding to an authentication challenge that
	 * contained an <code>WWWAuthenticateHeader</code> or 
	 * <code>ProxyAuthenticateHeader</code>. The generated 
	 * <code>AuthorizationHeader</code> is then inserted in an new REGISTER
	 * request and contains user's credentials so he, the user can be verified
	 * by the UAS.
	 *
	 * @param method method of the request pending authentication
	 * @param uri the digest-uri
	 * @param requestBody the body of the request
	 * @param authHeader the challenge that provokes the autorization.
	 * @param userCredentials the password and username of the user
	 *
	 * @return an <code>AuthorizationHeader</code> to confirm the user identity.
	 */
	public AuthorizationHeader createAuthorizationHeader(
			Request request,
			WWWAuthenticateHeader authHeader,
			SipUser user){


		String response = null;
		String uri = request.getRequestURI().toString();

		String decryptedPasswd = new String(Base64.decode(user.getPassword()));

		String content = "";
		if(request.getContent() != null)
			content = request.getContent().toString();


		DigestAlgorithm algorithm = new DigestAlgorithm(
				user.getUsername(),
				decryptedPasswd,
				authHeader.getRealm(),
				authHeader.getNonce(),
				request.getMethod(),
				request.getRequestURI().toString(),
				content);

		if(authHeader.getQop() != null){
			algorithm.setQop(DigestAlgorithm.QOP.auth, "abc", "00000001");
		}

		try{
			response = algorithm.computeDigest(); 

		}
		catch (NullPointerException exc){
			// Log the error: malformed authentication
		}

		AuthorizationHeader authorization = null;
		try{

			if (authHeader instanceof ProxyAuthenticateHeader){
				authorization = headerFactory.createProxyAuthorizationHeader(
						authHeader.getScheme());
			}
			else{
				authorization = headerFactory.createAuthorizationHeader(
						authHeader.getScheme());
				System.out.println(authHeader.getScheme());
			}

			if (authHeader.getAlgorithm() != null){
				authorization.setAlgorithm(authHeader.getAlgorithm());
			}
			if (authHeader.getOpaque() != null){
				authorization.setOpaque(authHeader.getOpaque());
			}
			if (algorithm.isSuppliedQop()) {
				authorization.setQop(algorithm.getQop().toString());
				authorization.setCNonce(algorithm.getCnonce());
				authorization.setNonceCount(Integer.parseInt(algorithm.getNc()));
			}


			authorization.setUsername(user.getUsername());
			authorization.setRealm(authHeader.getRealm());
			authorization.setNonce(authHeader.getNonce());
			authorization.setParameter("uri", uri);
			authorization.setResponse(response);
		}
		catch (ParseException ex){
			// logging.error("Failed to create an authorization header!");
		}

		return authorization;
	}

	public ProxyAuthorizationHeader createHTTPProxyAuthorizationHeader(
			Request request,
			WWWAuthenticateHeader authHeader,
			SipUser user){
		
		ProxyAuthorizationHeader header = null;
		
		try {
			header = headerFactory.createProxyAuthorizationHeader("Basic");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return header;
	}



	/**
	 * Creates a new Response message of type specified by the statusCode 
	 * paramater, based on a specific Request message. 
	 * 
	 * @param statuscode - the status code value of the response
	 * @param request - the received request to which an response is to be send
	 * @return the newly created response object
	 * @throws ParseException if an error has occured while parsing the values
	 */
	public Response createResponse(int statuscode, Request request) throws ParseException{

		Response response = messageFactory.createResponse(statuscode, request);

		return response;
	}

	public Address createAddress(URI uri){
		return addressFactory.createAddress(uri);
	}

	public ExpiresHeader createExpiresHeader(int expires) 
	throws InvalidArgumentException{
		return headerFactory.createExpiresHeader(expires);
	}

	public SipProvider getSipProvider() {
		return sipProvider;
	}

	public String getProtocol(){
		return listeningPoint.getTransport();
	}

	public int getPort(){
		if(listeningPoint != null){
			return listeningPoint.getPort();
		}
		return -1;
	}


}
