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


 import java.util.regex.Matcher;
 import java.util.regex.Pattern;

 import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

 /**
  * @author Georgi Dimitrov
  * @version 0.2 - May 6th 2008
  */
 public class SipUser {


	 private boolean validSipUser = false;

	 private String sipAddress = null;
	 private String hostname = null;
	 private String username = null;
	 private String screenname = "";
	 private String password = null;
	 private int port;

	 public SipUser(String sipAddress){
		 this.sipAddress = sipAddress;
		 extractFields();
	 }

	 public SipUser(String screenname, String sipAddress){
		 this.sipAddress = sipAddress;
		 this.screenname = screenname;
		 extractFields();
	 }

	 public SipUser(String screenname, String username, String sipHostname, int port){
		 this.screenname = screenname;
		 this.username = username;
		 this.hostname = sipHostname;
		 this.port = port;
		 createAddress();
	 }

	 private void extractFields(){

		 if(sipAddress.contains("@")){
			 // ok it is some king of sip similar input so check it!
			 String tmp = sipAddress;//.substring(4,sipAddress.length());
			 username = tmp.substring(0, tmp.indexOf("@"));

			 hostname = tmp.substring(tmp.indexOf("@")+1, tmp.length());
			 
			 if(!(hostname.matches(
					 "[[[a-zA-Z_0-9]{1,}[-]{0,1}[a-zA-Z_0-9]{1,}]{1,}" + // suffix
					 "[.]{1}[a-zA-Z]{1,}]{1,}" +
					 "[.]{1}[a-zA-Z]{2,3}")
			 || 
			 checkIPRegex(hostname))){
				 validSipUser = false;
				 username = null;
				 hostname = null;
				 return;
			 }
			 validSipUser = true;

//			 port = Integer.valueOf(tmp.substring(tmp.indexOf(":")+1, tmp.length()));
//			 System.out.println("SIPport = "+port);
			 tmp = null;
		 }

	 }

	 private String createAddress(){
		 return sipAddress = username + "@" + hostname;
	 }


	 public int getPort() {
		 return port;
	 }
	 public void setPort(int port) {
		 this.port = port;
	 }
	 public String getScreenname() {
		 return screenname;
	 }
	 public void setScreenname(String screenname) {
		 this.screenname = screenname;
	 }
	 public String getHostname() {
		 return hostname;
	 }
	 public void setHostname(String sipAddres) {
		 this.hostname = sipAddres;
	 }
	 public String getUsername() {
		 return username;
	 }
	 public void setUsername(String username) {
		 this.username = username;
	 }

	 public boolean isValidSipUser() {
		 return validSipUser;
	 }

	 public String getSipAddress() {
		 if(!validSipUser)
			 return sipAddress;
		 return createAddress();
	 }

	 public void setSipAddress(String sipAddress) {
		 this.sipAddress = sipAddress;
	 }

	 public String getFullAddress() {
		 return "\"" + screenname + "\" <" + sipAddress + ">";
	 }

	 /**
	  * Gets the user's password. The password is encoded so you have to decode
	  * it before it can be used for authentication.
	  * 
	  * @return the encoded user password.
	  */
	 public String getPassword() {
		 if(password == null)
			 return "";
		 return password;
	 }

	 public void setPassword(String password) {

		 this.password = Base64.encode(password.getBytes());
	 }

	 private boolean checkIPRegex(String s){
		 String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
		 Pattern p = Pattern.compile( "^(?:" + _255 + "\\.){3}" + _255 + "$");
		 Matcher m = p.matcher(s);
		 return m.matches();

	 }

 }
