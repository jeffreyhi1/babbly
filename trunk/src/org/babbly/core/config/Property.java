/*  babbly - lightweight instant messaging and VoIP client written in Java. 
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
package org.babbly.core.config;


/**
 * This interface contains all currently supported constants. In other words
 * the property names that are accepted from the <code>Configurator</code>.
 * The constants are grouped in small portions(network, UI, etc.)
 * 
 * @author Georgi Dimitrov (MrJ)
 *
 */
public interface Property {

	
	
	/**
     * The <b>property.undefined</b> property
     */
    public static final String UNDEFINED = "property.undefined";
	
    /* 
     * Network properties. 
     * 
     */
    
    /**
     * The <b>stun.server.address</b> property
     */
    public static final String STUN_ADDRESS = "stun.server.address";
    
    /**
     * The <b>stun.server.port</b> property
     */
    public static final String STUN_PORT = "stun.server.port";
    
    /**
     * The <b>bind.retries</b> property
     */
    public static final String BIND_RETRIES = "bind.retries";
    
	public static final String RESOLV_DEST = "resolve.destination";
    
    
    
    /*
     * Default properties
     * 
     */
    
    /**
     * The default value for the <b>stun.server.address</b> property
     */
    public static final String STUN_ADDRESS_DEFAULT = "stun.xten.net";
    
    /**
     * The default value for the <b>stun.server.port</b> property
     */
    public static final String STUN_PORT_DEFAULT = "3478";
    
    /**
     * The default value for the <b>bind.retries</b> property
     */
    public static final String BIND_RETRIES_DEFAULT = "5";

	public static final String RESOLV_DEST_DEFAULT = "google.com";

    
    public class Pattern{
        /**
         * The <b>stun.server.address</b> pattern. Has to be a either a valid 
         * hostname address or valid IP address.
         */
        public static final String STUN_ADDRESS = "";
        
        /**
         * The <b>stun.server.port</b> property
         */
        public static final String STUN_PORT = "";
        
        /**
         * The <b>bind.retries</b> property
         */
        public static final String BIND_RETRIES = "";

		public static final String RESOLV_DEST = "";
    }
    
    
    
	
}
