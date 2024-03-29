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

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import org.babbly.core.protocol.sip.SipClient;
import org.babbly.core.protocol.sip.SipManager;
import org.babbly.core.protocol.sip.SipUser;
import org.babbly.core.protocol.sip.event.SipClientListener;
import org.babbly.ui.gui.settings.Options;
import org.babbly.ui.gui.settings.SettingsLoader;
import org.babbly.ui.gui.view.OptionsWindow;
import org.babbly.ui.gui.view.PrimaryWindow;

/**
 * 
 * @author Georgi Dimitrov
 * 
 * @version 0.8
 */
public class PrimaryWindowController implements SipClientListener {

	private static PrimaryWindow primaryWindow = null;
	private OptionsWindowController optionsWindowController = null;

	private Properties properties = null;
	private Options options = null;
	private static SipUser callee;
	private SipManager manager = null;

	static Timer busyIconTimer = null;
	int busyIconIndex = 0;
	static Icon idleIcon = new ImageIcon("image/status_icons/idle-icon.png");

	private SipClient client = null;

	private static final int DEFAULT_LISTENING_PORT = 6060;
	private static final String DEFAULT_TRANSPORT_PROTOCOL = "UDP";

	/**
	 * Creates a new instance of PrimaryWindowController from the given primary
	 * window, which is going to be controlled and the given properties that
	 * contain the settings of the application. Then initializes the controller
	 * components.
	 * 
	 * @param primaryWindow
	 *            - the primary window to be controlled
	 * @param properties
	 *            - the properties which contain the app's settings
	 */
	public PrimaryWindowController(JFrame primaryWindow, Properties properties) {
		this.primaryWindow = (PrimaryWindow) primaryWindow;
		this.properties = properties;
		init();
	}

	/**
	 * Initializes all of the components of the PrimaryWindowController and adds
	 * listeners and actions to the components of the primary window object,
	 * that allow easy out-of-the-box control of the main graphical user
	 * interface. In addition defines methods that handle the occurring events
	 * of the primary window. Loads the application sounds, so they can be ready
	 * for playing when the appropriate event has occurred.
	 * 
	 */
	public void init() {
		options = new Options(properties);

		SettingsLoader.storeSettings();
		// this is important since it creates a new properties file,
		// in case there was not such one, and saves it for further use

		primaryWindow.setTitle("babbly");

		optionsWindowController = new OptionsWindowController(
				new OptionsWindow(primaryWindow, "Options"), options);
		primaryWindow.setLocationRelativeTo(null);

		primaryWindow.setVisible(true);
		
		primaryWindow.getLoginPane().getPasswordField().setEnabled(true);

		primaryWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				primaryWindowClosed(e);
			}
		});

		primaryWindow.getExitMenuItem().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.exit(0);
			}
		});
		primaryWindow.getOptionsMenuItem().addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						optionsMenuActionPerformed(evt);
					}
				});

		// The actual Actions that are relative to the registration

		// primaryWindow.removeCallTab();

		primaryWindow.getLogoutMenuItem().setEnabled(false);
		primaryWindow.getLoginPane().getMessagePane().setVisible(false);
		primaryWindow.getLoginPane().getLoginButton().setEnabled(false);
		primaryWindow.getStatusBarPane().setVisible(false);
		primaryWindow.getLoginPane().getPasswordField().setEnabled(false);
		
		client = new SipClient(this);

		System.out.println("Sip manager = " + manager);
		initStatusBarAnimation();

		primaryWindow.getLoginPane().getUsernameField().addKeyListener(
				new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						usernameTextFieldKeyReleased(e);
					}
				});

		primaryWindow.getLoginPane().getPasswordField().addKeyListener(
				new KeyAdapter() {
					@Override
					public void keyReleased(KeyEvent e) {
						passwordFieldKeyReleased(e);
					}
				});

		primaryWindow.getLoginPane().getLoginButton().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						loginButtonActionPerformed(e);
					}
				});

		primaryWindow.getLogoutMenuItem().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						logoutMenuItemActionPerformed(e);
					}
				});

		primaryWindow.getLoginPane().getLoginButton().setEnabled(true);
		primaryWindow.getLoginPane().getLoginButton().setEnabled(true);
		primaryWindow.getLoginPane().getPasswordField().setEnabled(true);

	}

	protected void primaryWindowClosed(WindowEvent e) {
		System.exit(0);
	}

	private void usernameTextFieldKeyReleased(KeyEvent evt) {

		callee = new SipUser(primaryWindow.getLoginPane().getUsernameField()
				.getText());

		if (callee.isValidSipUser()) {
			primaryWindow.getLoginPane().getMessagePane().setVisible(false);
			primaryWindow.getLoginPane().getLoginButton().setEnabled(true);
			primaryWindow.getLoginPane().getPasswordField().setEnabled(true);
		} else {
			primaryWindow.getLoginPane().getMessagePane().setVisible(true);
			primaryWindow.getLoginPane().getMessageLabel().setText(
			"Invalid SIP username!");
			primaryWindow.getLoginPane().getLoginButton().setEnabled(false);
		}

	}

	private void passwordFieldKeyReleased(KeyEvent e) {
		callee.setPassword(new String(primaryWindow.getLoginPane()
				.getPasswordField().getPassword()));
	}

	private void optionsMenuActionPerformed(ActionEvent evt) {
		optionsWindowController.showOptions(true);
	}

	private void loginButtonActionPerformed(ActionEvent e) {

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				primaryWindow.getLoginPane().getLoginButton().setEnabled(false);
				primaryWindow.getLoginPane().getUsernameField().setEnabled(
						false);
				primaryWindow.getLoginPane().getPasswordField().setEnabled(
						false);
				primaryWindow.getLoginPane().getRememberPassCheckBox()
				.setEnabled(false);
				primaryWindow.getLoginPane().getPassForgotLabel().setEnabled(
						false);
				primaryWindow.getLoginPane().getRegisterLabel().setEnabled(
						false);
				primaryWindow.getLoginPane().getMessagePane().setVisible(false);

				if (primaryWindow.getLoginPane().getPasswordField()
						.getPassword().length > 0) {
					String passwd = new String(primaryWindow.getLoginPane()
							.getPasswordField().getPassword());
					callee.setPassword(passwd);
					System.out.println("passwd: " + callee.getPassword());
				}

				primaryWindow.getStatusBarPane().setVisible(true);
				busyIconTimer.start();

				// ((CardLayout)pw.getCardPane().getLayout()).show(pw.getCardPane(),
				// "card3");

				primaryWindow.getStatusTextLabel().setText(
				"Proceeding with registration...");
				
				client.login(callee);
			}

		});
		t.start();

	}

	private void logoutMenuItemActionPerformed(ActionEvent e) {
		client.logout();
		statusMessage("Disconnecting...");
		statusRunning(true);
	}

	private void initStatusBarAnimation() {
		final Icon[] busyIcons = new Icon[15];

		int busyAnimationRate = 30;

		String iconPrf = "image/status_icons/busy-icon";

		for (int i = 0; i < busyIcons.length; i++) {
			busyIcons[i] = new ImageIcon(iconPrf + i + ".png");
		}
		busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				busyIconIndex = (busyIconIndex + 1) % busyIcons.length;

				primaryWindow.getStatusBusyLabel().setIcon(
						busyIcons[busyIconIndex]);
			}
		});
	}

	private static void feedbackMessage(String message) {
		if (message != null) {
			primaryWindow.getLoginPane().getMessageLabel().setText(message);
			primaryWindow.getLoginPane().getMessagePane().setVisible(true);
		}
	}

	private void statusMessage(String message) {
		if (message != null) {
			primaryWindow.getStatusTextLabel().setText(message);
			primaryWindow.getStatusPane().setVisible(true);

		}
	}

	private static void statusRunning(boolean runIt) {
		if (runIt) {
			busyIconTimer.start();
		} else {
			busyIconTimer.stop();
			primaryWindow.getStatusBusyLabel().setIcon(idleIcon);
		}
	}

	private void hideFeedback() {
		primaryWindow.getLoginPane().getMessagePane().setVisible(false);
	}

	private void hideStatus() {
		primaryWindow.getStatusPane().setVisible(false);
	}

	private void loginEnabled(boolean enabled) {
		primaryWindow.getLoginPane().getLoginButton().setEnabled(enabled);
		primaryWindow.getLoginPane().getUsernameField().setEnabled(enabled);
		primaryWindow.getLoginPane().getPasswordField().setEnabled(enabled);
		primaryWindow.getLoginPane().getPassForgotLabel().setEnabled(enabled);
		primaryWindow.getLoginPane().getRegisterLabel().setEnabled(enabled);
		primaryWindow.getLoginPane().getRememberPassCheckBox().setEnabled(
				enabled);
	}

	void showCallPane() {
		primaryWindow.getLogoutMenuItem().setEnabled(true);
		CardLayout cardLayout = (CardLayout) primaryWindow.getCardPane()
		.getLayout();
		cardLayout.show(primaryWindow.getCardPane(), "card3");
	}

	void showLoginPane() {
		primaryWindow.getLogoutMenuItem().setEnabled(false);

		if (!primaryWindow.getLoginPane().getRememberPassCheckBox()
				.isSelected()) {
			primaryWindow.getLoginPane().getPasswordField().setText("");
		}

		CardLayout cardLayout = (CardLayout) primaryWindow.getCardPane()
		.getLayout();

		cardLayout.show(primaryWindow.getCardPane(), "card2");
	}

	@Override
	public void onConnecting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Error error) {
		loginEnabled(true);
		statusRunning(false);
		statusMessage("Not Connected");
		switch(error){
		case WRONG_PASSWORD:
			feedbackMessage("Wrong password!");
			break;
		case INVALID_ADDR:
			feedbackMessage("Unknown Server Address");
			break;
		default:
			;
		}
	}

	@Override
	public void onLogin() {
		statusMessage("Connected");
		statusRunning(false);
		showCallPane();
	}

	@Override
	public void onLoginAuth() {
		System.out.println("onLoginAuth");

	}

	@Override
	public void onLogout() {
		statusMessage("Not Connected");
		statusRunning(false);
		showLoginPane();
		loginEnabled(true);
	}

}
