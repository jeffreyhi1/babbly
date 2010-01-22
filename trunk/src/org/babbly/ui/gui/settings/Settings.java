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
package org.babbly.ui.gui.settings;


/**
 * This interface contains all currently supported constants. In other words
 * the property names that are accepted from the <code>Configurator</code>.
 * The constants are grouped in small portions(network, UI, etc.)
 * 
 * @author Georgi Dimitrov (MrJ)
 *
 */
public class Settings {
	
	private String name = null;
	private String pattern = null;
	private String defaultValue = null;
	
	Settings(String name, String pattern){
		this.name = name;
	}
	
	public boolean isValidValue(String value){
		if(value.matches(pattern)){
			return true;
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

    
    
    
	
}
