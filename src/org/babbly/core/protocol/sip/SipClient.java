/**
 * 
 */
package org.babbly.core.protocol.sip;

import java.net.InetAddress;
import java.util.Properties;
import java.util.TooManyListenersException;

import javax.sip.ClientTransaction;
import javax.sip.InvalidArgumentException;
import javax.sip.ObjectInUseException;
import javax.sip.PeerUnavailableException;
import javax.sip.TransportNotSupportedException;
import javax.sip.message.Response;

import org.babbly.core.config.Conf;
import org.babbly.core.config.Property;
import org.babbly.core.net.InetAddresResolver;
import org.babbly.core.protocol.sip.event.SipClientListener;
import org.babbly.core.protocol.sip.event.SipClientListener.Error;
import org.babbly.core.protocol.sip.event.SipGeneralListener;
import org.babbly.core.protocol.sip.event.SipObserver;
import org.babbly.core.protocol.sip.event.SipGeneralListener.Handler;
import org.babbly.core.protocol.sip.registration.RegistrationState;
import org.babbly.core.protocol.sip.registration.SipRegistration;
import org.babbly.core.protocol.sip.registration.SipRegistrationtListener;


/**
 * This class represents the call or the 'phone' who is responsible for the
 * incoming and outgoing calls. It defines an sip manager, and two user
 * agents, respectively User Agent Client(UAC) and User Agent Server (UAS). The
 * UAC is created when an calling attempt has been made, and is responsible for
 * inviting the selected remote UAS to call participation. An UAS is created 
 * with the start up of the program, listens for incoming calls, and manages 
 * the requests send by the remote UAC.
 * 
 * @author Georgi Dimitrov
 * @version 0.2
 *
 */
public class SipClient{

	
	private SipClientListener listener = null;
	private SipRegistration registration = null;
	private SipManager manager  = null;
	private SipUser localusr = null;


	public SipClient(Properties prop, SipClientListener listener){
		this.listener = listener;

		if(prop != null && prop.size() > 0){
			Conf.setProperties(prop);
		}

		InetAddress addr = InetAddresResolver.resolveInternetInterface();
		String ip = addr.getHostAddress();
		int port = Integer.parseInt(Conf.get(Property.SIP_BIND_PORT));
		String protocol = Conf.get(Property.TRANSPORT_PROTOCOL);
		/* TODO: set a timer to check the Internet connection in intervals
		 * in order to see if our public IP has changed
		 */

		try {
			manager = new SipManager(ip, port, protocol);
		} catch (PeerUnavailableException e) {
			e.printStackTrace(); //FIXME: retry to create manager NO MATTER WHAT!
		} catch (TransportNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ObjectInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registration = new SipRegistration(manager);
		registration.addObserver(new SipRegistrationObserver());
		SipGeneralListener ual = new SipGeneralListener();
		ual.addListener(Handler.REGISTRATION, new SipRegistrationtListener(registration));
		try {
			manager.getSipProvider().addSipListener(ual);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}

	}

	public SipClient(SipClientListener listener) {
		this(null, listener);
	}

	public void login(SipUser callee){
		localusr = callee;
		registration.register(callee);
	}

	public void logout(){
		registration.unregister();
	}
	
	private class SipRegistrationObserver implements SipObserver<RegistrationState>{

		@Override
		public void update(RegistrationState state) {
			System.out.println("STATE: "+state);
			switch (state) {
			case UNREGISTERED:
				listener.onLogout();
				break;
			case REGISTERING:
				listener.onConnecting();
				break;
			case AUTHENTICATING:
				listener.onLoginAuth();
				break;
			case AUTHENTICATION_FAILED:
				listener.onError(Error.WRONG_PASSWORD);
				break;
			case REGISTERED:
				listener.onLogin();
				break;
			default:
				break;
			}
			// TODO Auto-generated method stub
			
		}
	}
}