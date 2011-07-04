/*  babbly - lightweight instant messaging and VoIP client written in Java. 
 * 
 *
 *  Copyright (C) 2010  Georgi Dimitrov  mrj[at]abv[dot]bg
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

package org.babbly.core.authorization;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class implements closely the Digest Algorithm definition as specified in
 * rfc2617. 
 * 
 * When an UAC receives a response which is an Authentication challenge from an 
 * UAS, there is an algorithm value within, that is defined by the UAS. 
 * That value defines the algorithm to be used from the UAC to create the 
 * checksum or digest. It can be either "MD5" or "MD5-sess", whereby the 
 * exact way of computing of the response depends on it. 
 * <p>
 * <b>As stated in rfc2617:</b>
 * <br>
 * If this algorithm value is not present it is assumed to be "MD5". 
 * If the algorithm value is not understood, the challenge should be ignored
 * (and a different one used, if there is more than one).
 * <p>
 * The string obtained by applying the Digest Algorithm to the data "data" with 
 * secret "secret" will be denoted by KD(secret, data), 
 * and the string obtained by applying the checksum algorithm to the data "data"
 * will be denoted H(data).
 *  
 * <pre>
 * <code>
 * For the "MD5" and "MD5-sess" algorithms:
 *
 *        H(data) = MD5(data)
 *
 *    and
 *
 *        KD(secret, data) = H(concat(secret, ":", data))
 *</code>
 *</pre>
 * 
 * @author Georgi Dimitrov (g.dimitrov@mail.com)
 *
 */
/**
 * @author Georgi Dimitrov (MrJ)
 *
 */
public class DigestAlgorithm {


//	public static enum Method{Digest};

	/**
	 * An enumeration class to contain all of the possible MD5 options, which
	 * are applicable for the used auth algorithm.
	 * 
	 * @author Georgi Dimitrov (g.dimitrov@mail.com)
	 */
	public enum Algorithm{

		MD5("MD5"), MD5Session("MD5-sess");

		/**
		 * The textual representation for the <code>Algorithm</code>
		 *  enumeration.
		 */
		String text = null;

		/**
		 * Constructs the <code>Algorithm</code> enumeration and assigns the 
		 * given text to its <code>text</code> field.
		 * 
		 * @param text the text information to assign to the instance of this
		 * enumeration. 
		 */
		Algorithm(String text){
			this.text = text;
		}
	};
	
	/**
	 * An enumeration class to represent the grade on quality of protection 
	 * (QOP). Possible options are <code>auth</code>, <code>authint</code> and
	 * <code>unspecified</code>.
	 * 
	 * @author Georgi Dimitrov (g.dimitrov@mail.com)
	 */
	public enum QOP{

		auth("auth"), authint("auth-int"), unspecified(null);

		String text = null;

		QOP(String text){
			this.text = text;
		}

	};

	/** The used authentication algorithm */
	private Algorithm algorithm = Algorithm.MD5;
	
	/** The used authentication method */
	private String method = null;
	
	/** The grade on quality of protection (QOP) */
	private QOP qop = QOP.unspecified;
	
	
	/** The client nonce parameter */
	private String cnonce = null;
	
	/** The request counter */
	private String nc = null;


	/** Stores the user's username */
	private String username = null;
	
	/** Stores the user's password */
	private String password = null;
	
	/** An additional field used to store the authentication realm */
	private String realm = null;
	
	/**
	 * A randomly generated value by the server, for one time use only
	 */
	private String nonce = null;
	
	/** The body of the digest algorithm */
	private String body = null;
	
	/** The body of the digest algorithm */
	private String digestURI = null;
	

	/**
	 * An array containing all valid values of the Hexadecimal
	 * (hex, base16, etc.) numeral system.
	 */
	private static final char hexTable[] = {
		'0', '1', '2', '3',
		'4', '5', '6', '7',
		'8', '9', 'a', 'b',
		'c', 'd', 'e', 'f'
	};

	/**
	 * Fast convert a byte array to a hex string with possible leading zero.
	 * <p>
	 * <b>Note: </b>The code for this method was seen on: 
	 * 
	 * {@link}http://mindprod.com/jgloss/hex.html
	 * 
	 * @param b the byte array that is going to be converted into its hex value.
	 * 
	 * @return the hex value of the given byte array.
	 */
	public static String toHexString (byte[] b){
		StringBuffer sb = new StringBuffer(b.length * 2);

		for ( int i=0; i<b.length; i++ ){
			// look up high nibble char
			sb.append(hexTable [(b[i] & 0xf0 ) >>> 4]);

			// look up low nibble char
			sb.append(hexTable [b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * Takes as input parameters that are two types of data. The first one is
	 * containing the user's credentials and the second is the data received 
	 * from the server within the Authentication header of the response. 
	 * Having that data the <code>computeDigest</code> method generates a valid 
	 * digest response that will be included in an Authorization header within a
	 * REGISTER request.
	 * <p>
	 * <b>Note: </b> This method is compliant with rfc2617.
	 * 
	 * @param username the username of the current user
	 * @param password the password for the given username
	 * @param realm the authentication realm
	 * @param nonce randomly, one time use generated value by the server 
	 * @param method the method of the authentication algorithm
	 * @param digestURI the URI of the digest algorithm
	 * @param body the body e.g. the content, may be empty, null or vice versa
	 */
	public DigestAlgorithm(String username, String password, String realm,
			String nonce, String method, String digestURI, String body) 
	throws IllegalArgumentException{ 

		if(method == null){
			throw new IllegalArgumentException(
					"parameter method can not be null!"); 
		}
		if(realm == null){
			throw new IllegalArgumentException(
					"parameter realm can not be null!");
		}
		if(nonce == null){
			throw new IllegalArgumentException(
					"parameter nonce can not be null!");
		}
		if(digestURI == null){
			throw new IllegalArgumentException(
					"parameter digestURI can not be null!");
		}
		
		this.username = username;
		this.password = password;
		this.realm = realm;
		this.nonce = nonce;
		this.method = method;
		this.digestURI = digestURI;
		if(body != null){
			this.body = body;	
		}
		else{
			this.body = "";
		}

	}


	/**
	 * Calculate the value of the Digest algorithm according to rfc2617.
	 * 
	 * @return the newly computed digest value
	 */
	public String computeDigest() {

		String a1 = a1(), a2 = a2();

	        if( qop != QOP.unspecified) {
		        return kd( h(a1), 
		        		   nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" + 
		        		   h(a2) );
	        }
	        else {
	            return kd(h(a1), nonce + ":" + h(a2));
	        }
	}


	
	// A private helper method to compute the value of the KD key
	private String kd(String secret, String data){
		return h(secret + ":" + data);
	}


	// Calculate the value of H
	private String h(String data){

		MessageDigest digest = null;
		
		try {
			digest = MessageDigest.getInstance(algorithm.toString());
		} catch (NoSuchAlgorithmException e) { 
			// This will never happen since we have an enum that is controlling
			// this issue.
		}
		
		byte[] digestedData = digest.digest(data.getBytes());
		
		
		return toHexString(digestedData);
	}

	// Calculate the value of A1
	private String a1(){

		if (algorithm == Algorithm.MD5) {
			return username + ":" + realm + ":" + password;
		}
		else if(algorithm == Algorithm.MD5Session){
			if(cnonce == null || cnonce.length() < 1){
				throw new NullPointerException(
				"cnonce have to be specified for MD5-Sess algorithm.");
			}
			else{
				return h(username + ":" + realm + ":" + password)
				+ ":" + nonce + ":" + cnonce;
			}
		}
		else{
			return null;
		}

	}

	//Calculate the value of A2
	private String a2(){

		if (qop == QOP.unspecified || qop == QOP.auth) {
			return method + ":" + digestURI;
		}
		else {
			return method + ":" + digestURI + ":" + h(body);
		}
	}

	/**
	 * Checks whether there is a supplied quality of protection (QOP) property.
	 * 
	 * @return <code>true</code> if a QOP property has been supplied, 
	 * <code>false</code> otherwise.
	 */
	public boolean isSuppliedQop() {
		return qop != null && qop != QOP.unspecified;
	}

	/**
	 * Sets a value for the quality of protection (QOP) property.
	 * 
	 * @param qop the new value for the QOP property
	 * @param cnonce the value of the client nonce
	 * @param nc the value of the request counter
	 * 
	 * @throws IllegalArgumentException if the given request counter (nc) is
	 * null.
	 */
	public void setQop(QOP qop, String cnonce, String nc) 
	throws IllegalArgumentException {
		if(cnonce == null){
			throw new IllegalArgumentException(
					"parameter cnonce can not be null");
		}
		if(nc == null){
			throw new IllegalArgumentException(
					"parameter nc can not be null");
		}
		this.qop = qop;
		this.cnonce = cnonce;
		this.nc = nc;
	}
	
	/**
	 * Sets the algorithm of choice for the digest authentication
	 * 
	 * @param algorithm the algorithm to use for the authentication
	 */
	public void setAlgorithm(Algorithm algorithm){
		this.algorithm = algorithm;
	}

	/**
	 * Gets the value of the quality of protection property (QOP)
	 * 
	 * @return the value of the QOPproperty
	 */
	public QOP getQop() {
		return qop;
	}

	/**
	 * Gets the value of the client nonce (cnonce) property
	 * 
	 * @return the value of the cnonce property
	 */
	public String getCnonce() {
		return cnonce;
	}

	/**
	 * Gets the value of the request counter (nc) property
	 * 
	 * @return the value of the nc property
	 */
	public String getNc() {
		return nc;
	}

}
