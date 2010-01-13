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
package org.babbly;

import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.babbly.core.util.SettingsLoader;
import org.babbly.ui.gui.controller.PrimaryWindowController;
import org.babbly.ui.gui.view.PrimaryWindow;

/**
 * The MAIN Class. It just sets the default look and feel to the System's look
 * and starts the application.
 *
 * @author Georgi Dimitrov
 * 
 */
public class Main {


	public static void main(String [] args){
		//new Main();
		final Properties properties = SettingsLoader.loadProperties("2gogosip.properties");
		System.out.println("root path is: "+System.getProperty("user.dir"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		
//		System.getProperties().put("proxySet","true");
//	    System.getProperties().put("proxyPort","8888");
//	    System.getProperties().put("proxyHost","proxy.hs-karlsruhe.de");
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "http://proxy.hs-karlsruhe.de");
        System.setProperty("http.proxyPort", "8888");

		System.setProperty("http.proxyUserName", "dige0012");
		System.setProperty("http.proxyPassword", "jzB6r35n");
		
		
		
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				PrimaryWindow pr = new PrimaryWindow();
				new PrimaryWindowController(pr,properties);
			}});
	}
}
