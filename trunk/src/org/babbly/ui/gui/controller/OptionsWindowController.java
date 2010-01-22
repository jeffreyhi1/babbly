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
package org.babbly.ui.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.babbly.ui.gui.settings.Options;
import org.babbly.ui.gui.view.OptionsWindow;


/**
 *
 * @author Georgi Dimitrov
 */
public class OptionsWindowController {
    
    private OptionsWindow optionsWindow = null;
    private Options options = null;
    /**
     * Creates a new instance of OptionsWindowController
     */
    public OptionsWindowController(OptionsWindow optionsWindow, Options options) {
        this.optionsWindow = optionsWindow;
        this.options = options;
        init();
    }
    
    private void init(){
    	
    	optionsWindow.setLocationRelativeTo(optionsWindow.getParent());
    	
        optionsWindow.getOkButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        optionsWindow.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        optionsWindow.getPortTextField().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                portTextFieldKeyTyped(evt);
            }
            public void keyReleased(KeyEvent evt){
            	portTextFieldKeyReleased(evt);
            }
        });
    }
    
    private void cancelButtonActionPerformed(ActionEvent evt){
        optionsWindow.dispose();
        
    }
    
    private void okButtonActionPerformed(ActionEvent evt){
        optionsWindow.dispose();
	        options.setListenPort(Integer.valueOf(optionsWindow.getPortTextField().getText()));
        options.setAutoAcceptCall(optionsWindow.getAutoCallCheckBox().isSelected());
        if(optionsWindow.getUdpRadioButton().isSelected())
        	options.setNetworkProtocol(optionsWindow.getUdpRadioButton().getText());
        else{
        	options.setNetworkProtocol(optionsWindow.getTcpRadioButton().getText());
        }
        options.saveOptions();
        	
    }
    
    private void portTextFieldKeyTyped(KeyEvent evt){
    	if("1234567890".indexOf(evt.getKeyChar()) == -1 ||
    		optionsWindow.getPortTextField().getText().length()>4){
    		evt.consume();
    	}
    }
    
    private void portTextFieldKeyReleased(KeyEvent evt){
    	if(optionsWindow.getPortTextField().getText().length() !=  0){
    		int port = Integer.valueOf(optionsWindow.getPortTextField().getText()); 
    		if( port > 65536 || port < 4000 ){
        		optionsWindow.getPortTextField().setText(""+options.getListenPort());
        	}	
    	} 
    	
    }
    
    
    public void showOptions(boolean showOptions){
    	System.out.println("v showOptions");
    	optionsWindow.getPortTextField().setText(""+options.getListenPort());
        optionsWindow.getAutoCallCheckBox().setSelected(options.isAutoAcceptCall());
        if(options.getNetworkProtocol().equals("UDP")){
        	optionsWindow.getUdpRadioButton().setSelected(true);
        }
        else{
        	optionsWindow.getTcpRadioButton().setSelected(true);
        }
        optionsWindow.setVisible(showOptions);
    }

	public Options getOptions() {
		return options;
	}
	
	
	
}
