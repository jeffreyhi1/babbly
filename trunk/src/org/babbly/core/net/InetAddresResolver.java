package org.babbly.core.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

import net.java.stun4j.StunAddress;
import net.java.stun4j.client.SimpleAddressDetector;

import org.babbly.core.config.Configurator;
import org.babbly.core.config.Property;
import org.babbly.core.util.AppLog;


public class InetAddresResolver {

	/**
	 * A <code>LoggingUtility</code> logger that will handle the logging.
	 */
	private static  AppLog logger =	AppLog.getLogger(InetAddresResolver.class);
	
	/**
	 * The maximum port number that could be bound.
	 */
	public static final int    MAX_PORT_NUMBER = 65535;

	/**
	 * The minimum port number that could be bound. All ports below port number
	 * 1024 are widely known as the "trusted ports", hence they should not be
	 * used by user other than the Administrator on windows or respectively the
	 * root superuser on Linux.
	 */
	public static final int    MIN_PORT_NUMBER = 1024;

	private static DatagramSocket socket = null;

	static{
		socket = initRandomPortSocket();
	}




	/**
	 * Returns an InetAddress instance that represents the network interface
	 * that is connected to the Internet, or the one that is connected to a 
	 * local network.
	 *
	 * @param dest the destination to look up
	 *
	 * @return an InetAddress representing the network interface that has 
	 * Intranet/Internet connection, or <code>null</code> if none of these is 
	 * available. 
	 */
	public static synchronized InetAddress resolveInternetInterface(
			InetAddress dest){

		InetAddress networkInterface = null;

		if(dest == null){
			//log: no destination specified, using default destination +
			// Configurator.get(Property.RESOLV_DEST_DEFAULT)
			try {
				dest = InetAddress.getByName(Configurator.get(Property.RESOLV_DEST));
			} catch (UnknownHostException e) {
				//log: could not resolve destination + 
				// Configurator.get(Property.RESOLV_DEST_DEFAULT)
			}
		}

		if(dest != null){
			socket.connect(dest, getRandomPortNumber());
			networkInterface = socket.getLocalAddress();
			socket.disconnect();	
		}

		if(networkInterface == null || networkInterface.isAnyLocalAddress()){
			//log: invalid interface address, setting network interface to local host
			try{
				networkInterface = InetAddress.getLocalHost();
			}
			catch (Exception ex){
				logger.warn("Failed to get localhost ", ex);
			}
		}
		return networkInterface;
	}

	public static synchronized InetAddress resolveInternetInterface(){
		return resolveInternetInterface(null);
	}




	private static DatagramSocket initRandomPortSocket() {

		String message = null;

		DatagramSocket socket = null;

		String bindRetriesProperty = Configurator.get(Property.BIND_RETRIES);

		int bindRetries = Integer.parseInt(bindRetriesProperty);


		int currentlyTriedPort = getRandomPortNumber();

		//try to find a random open port and bind it. If that port is in use,
		// try another one (max. bindRetries times)
		for (int i = 0; i < bindRetries; i++){
			try{
				socket = new DatagramSocket(currentlyTriedPort);
				//if binds succeeds, stop trying to bind
				logger.debug("successfully bound a datagram socket on port " + currentlyTriedPort);
				break;
			}
			catch (SocketException ex){
				if (ex.getMessage().indexOf("Address already in use") == -1){
					message = "An exception occurred while trying to create"
						+ "a local host discovery socket.";
					logger.fatal(message, ex);	
					socket = null;
					return null;
				}
				message = "Port " + currentlyTriedPort
				+ " seems to be in use.";
				logger.debug(message);	

				//this port seems to be taken. try another one.
				currentlyTriedPort = getRandomPortNumber();

				message = "Retrying bind on port " + currentlyTriedPort;
				logger.debug(message);	

			}
		}

		return socket;
	}


	public static InetSocketAddress getPublicAddress(int localPort){

		InetSocketAddress resolvedAddr = null;

		String stunAddressStr = Configurator.get(Property.STUN_ADDRESS);
		String portStr = Configurator.get(Property.STUN_PORT);
		int stunPort = Integer.parseInt(portStr);

		StunAddress stunAddr = new StunAddress(stunAddressStr, stunPort);

		SimpleAddressDetector detector = new SimpleAddressDetector(stunAddr);

		logger.debug("Created a STUN Address detector for the following "
				+ "STUN server: " + stunAddressStr + ":" + stunPort);

		detector.start();
		logger.debug("STUN server detector started;");


		//----------------------------------------------------------------------
		StunAddress mappedAddress = null;
		try {
			mappedAddress = detector.getMappingFor(localPort);
		} catch (IOException e) {
			// log: failed to bind
			//e.printStackTrace();
		}
		//----------------------------------------------------------------------
		System.out.println("lala");
		detector.shutDown();
		if(mappedAddress != null){
			resolvedAddr = mappedAddress.getSocketAddress();
		}
		else{
			String dstProperty = Configurator.get(Property.RESOLV_DEST);
			InetAddress destination = null;
			try {
				destination = InetAddress.getByName(dstProperty);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			InetAddress publicHost = resolveInternetInterface(destination);
			resolvedAddr = new InetSocketAddress(publicHost, localPort);
		}
		return resolvedAddr;
	}
	
	/**
	 * Return a random port number that is in the range between 1024 and 65536. 
	 * 
	 * @return the newly generated random port number.
	 */
	public static int getRandomPortNumber(){
		return new Random().nextInt(MAX_PORT_NUMBER - MIN_PORT_NUMBER);
		//TODO: Handle special ports that could be used by other apps 8080,80 etc.
	}

}
