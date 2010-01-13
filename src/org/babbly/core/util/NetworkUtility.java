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
package org.babbly.core.util;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

import net.java.stun4j.StunAddress;
import net.java.stun4j.client.SimpleAddressDetector;

/**
 * @author Georgi Dimitrov (MrJ)
 * <p>
 * <b>Note:</b> Some parts of that code was seen in sip-communicator which is
 * LGPL, see licensing terms at http://www.gnu.org
 */
public class NetworkUtility {


	/**
	 * A <code>LoggingUtility</code> logger that will handle the logging.
	 */
	private static  AppLog logger =
		AppLog.getLogger(NetworkUtility.class);

	private String message;

	/**
	 * A string representing the "any" local address.
	 */
	public static final String LOCAL_ADDR_ANY = "0.0.0.0";


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



	private DatagramSocket localHostFinderSocket = null;
	private StunAddress stunServerAddress = null;
	private SimpleAddressDetector detector = null;




	public NetworkUtility(){

		this.localHostFinderSocket = initRandomPortSocket();

		// initialize STUN
		String stunAddressStr = 
			Configurator.getConfigProperty("STUN_SERVER_ADDRESS");
		String portStr = 
			Configurator.getConfigProperty("STUN_SERVER_PORT");

		int port = -1;

		if (stunAddressStr.equals(Configurator.UNDEFINED_PROPERTY)
				|| portStr.equals(Configurator.UNDEFINED_PROPERTY)){


			stunServerAddress = new StunAddress("stun.iptel.org",3478);

			message = "Stun server address("+stunAddressStr+")/port("
			+portStr +") not set (or invalid). Disabling STUN."; 

			logger.info(message);
		}
		else{
			try{
				port = Integer.valueOf(portStr).intValue();
			}
			catch (NumberFormatException ex){
				message = 
					portStr + " is not a valid port number. "
					+"Defaulting port number to 3478";
				logger.info(message, ex);
				port = 3478;
			}

			stunServerAddress = new StunAddress(stunAddressStr, port);
			detector = new SimpleAddressDetector(stunServerAddress);

			message = "Created a STUN Address detector for the following "
				+ "STUN server: " + stunAddressStr + ":" + port;

			logger.debug(message);

			detector.start();

			logger.debug("STUN server detector started");

			launchStunServerTest();
		}
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



	/**
	 * Returns an InetAddress instance that represents the local host, and that
	 * a socket can bind upon or distribute to peers as a contact address.
	 *
	 * @param intendedDestination the destination to which future connections are to be
	 * established.
	 *
	 * @return an InetAddress instance representing the local host, and that
	 * a socket can bind upon.
	 */
	public synchronized InetAddress getLocalHost(InetAddress intendedDestination){

		InetAddress localHost = null;

		localHostFinderSocket.connect(intendedDestination,56789);
		localHost = localHostFinderSocket.getLocalAddress();
		localHostFinderSocket.disconnect();

		// because of the fact that windows systems return the any address
		// e.g. 0.0.0.0 here is a small workaround for that using InetAddress
		if( localHost.isAnyLocalAddress()){
			try{
				localHost = InetAddress.getLocalHost();
			}
			catch (Exception ex){
				logger.warn("Failed to get localhost ", ex);
			}
		}
		return localHost;
	}




	private DatagramSocket initRandomPortSocket(){
		DatagramSocket resultSocket = null;

		String bindRetryCount = 
			Configurator.getConfigProperty("BIND_RETRIES");

		int bindRetries = 5;

		// if such property has been defined
		if (!bindRetryCount.equalsIgnoreCase(Configurator.UNDEFINED_PROPERTY)){
			try{
				bindRetries = Integer.parseInt(bindRetryCount);
			}
			catch (NumberFormatException ex){
				// the defined property was invalid
				String message = 
					"The property BIND_RETRIES" + 
					" seems to have invalid value: " +	bindRetryCount +
					". Defaulting port bind retries to: " +	bindRetries;
				logger.debug(message, ex);
			}
		}
		else{
			String message = 
				"No property BIND_RETRIES defined. " +
				"Using default value(" + bindRetries + ")";
			logger.debug(message);
		}

		int currentlyTriedPort = NetworkUtility.getRandomPortNumber();
		System.out.println("port:"+ currentlyTriedPort);

		//try to find a random open port and bind it. If that port is in use,
		// try another one (max. bindRetries times)
		for (int i = 0; i < bindRetries; i++){
			try{
				resultSocket = new DatagramSocket(currentlyTriedPort);
				//if binds succeeds, stop trying to bind
				break;
			}
			catch (SocketException exc){
				if (exc.getMessage().indexOf("Address already in use") == -1){
					message = 
						"An exception occurred while trying to create"
						+ "a local host discovery socket.";
					logger.fatal(message, exc);	
					resultSocket = null;
					return null;
				}
				message = "Port " + currentlyTriedPort
				+ " seems to be in use.";
				logger.debug(message);	

				//this port seems to be taken. try another one.
				currentlyTriedPort = NetworkUtility.getRandomPortNumber();

				message = "Retrying bind on port " + currentlyTriedPort;
				logger.debug(message);	

			}
		}

		return resultSocket;
	}


	private void launchStunServerTest(){
		Thread stunServerTestThread = new StunServerTestThread();

		stunServerTestThread.setDaemon(true);
		stunServerTestThread.start();

	}

	class StunServerTestThread extends Thread{
		public void run(){
			DatagramSocket randomSocket = initRandomPortSocket();

			try{
				StunAddress stunAddress	= detector.getMappingFor(randomSocket);
				randomSocket.disconnect();

				if (stunAddress != null){

					message = 
						"StunServer check succeeded for server: "
						+ detector.getServerAddress()
						+ " and local port: "
						+ randomSocket.getLocalPort();
					logger.debug(message);	
				}
				else{
					message = 
						"StunServer check failed for server: "
						+ detector.getServerAddress()
						+ " and local port: "
						+ randomSocket.getLocalPort()
						+ ". No address returned by server.";
					logger.debug(message);		
				}
			}
			catch (Throwable ex){
				message = "Failed to run a stun query against "
					+ "server :" + detector.getServerAddress();
				logger.error(message, ex);	

				if (randomSocket.isConnected()){
					randomSocket.disconnect();
				}
			}
		}
	}
}