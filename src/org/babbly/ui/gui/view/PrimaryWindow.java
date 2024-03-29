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
package org.babbly.ui.gui.view;

import javax.swing.ImageIcon;


/**
 *
 * @author  Georgi Dimitrov
 * @version 0.5
 */
@SuppressWarnings("serial")
public class PrimaryWindow extends javax.swing.JFrame {


    private ImageIcon ringIcon;
	private ImageIcon userIcon;
	private ImageIcon callIcon;
	private ImageIcon hangupIcon;
	/**
     * Creates new form PrimaryWindow
     */
    public PrimaryWindow() {
    	try{
    	ringIcon = new javax.swing.ImageIcon(getClass().getResource("/image/ring_ring.gif"));
    	userIcon = new javax.swing.ImageIcon(getClass().getResource("/image/user-48.png"));
    	callIcon = new javax.swing.ImageIcon(getClass().getResource("/image/call40x40.png"));
    	hangupIcon = new javax.swing.ImageIcon(getClass().getResource("/image/hangup40x40.png"));
    	}catch(Exception e){
    		System.err.println("WARNING: Some Icon Resources couldn't be loaded. Check your path!");
    	}
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusPane = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        userDetailsPane = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        screenNameLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        usernameLabel = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ipLabel = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        hostnameLabel = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        listenPortLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fullSipAddressLabel = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        statusBarPane = new javax.swing.JPanel();
        statusTextLabel = new javax.swing.JLabel();
        statusBusyLabel = new javax.swing.JLabel();
        cardPane = new javax.swing.JPanel();
        loginPane = new LoginPanel();
        mainPane = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        callPane = new javax.swing.JPanel();
        callInfoPane = new javax.swing.JPanel();
        callInfoLabel = new javax.swing.JLabel();
        ringLabel = new javax.swing.JLabel();
        callPartyLabel = new javax.swing.JLabel();
        callComboBox = new javax.swing.JComboBox();
        callButtonsPane = new javax.swing.JPanel();
        callButton = new javax.swing.JButton();
        hangupButton = new javax.swing.JButton();
        menuBar1 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        logoutMenuItem = new javax.swing.JMenuItem();
        fmSseparator = new javax.swing.JSeparator();
        exitMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        tmSeparator = new javax.swing.JSeparator();
        optionsMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        statusPane.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusPane.setDoubleBuffered(false);

        statusLabel.setText("Not Connected");

        org.jdesktop.layout.GroupLayout statusPaneLayout = new org.jdesktop.layout.GroupLayout(statusPane);
        statusPane.setLayout(statusPaneLayout);
        statusPaneLayout.setHorizontalGroup(
            statusPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPaneLayout.createSequentialGroup()
                .add(statusLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(33, Short.MAX_VALUE))
        );
        statusPaneLayout.setVerticalGroup(
            statusPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusLabel)
        );

        userDetailsPane.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("User Details");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Screen name:");

        screenNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        screenNameLabel.setText("Georgi");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel5.setText("Username:");

        usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        usernameLabel.setText("mrj");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("IP:");

        ipLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ipLabel.setText("172.20.45.21");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel9.setText("Hostname:");

        hostnameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        hostnameLabel.setText("o106.hadiko.de");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Listen port:");

        listenPortLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        listenPortLabel.setText("5060");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Full Sip Address");
        jLabel13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        fullSipAddressLabel.setForeground(new java.awt.Color(51, 51, 255));
        fullSipAddressLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        fullSipAddressLabel.setText("<sip:mrj@o106.hadiko.de>");

        org.jdesktop.layout.GroupLayout userDetailsPaneLayout = new org.jdesktop.layout.GroupLayout(userDetailsPane);
        userDetailsPane.setLayout(userDetailsPaneLayout);
        userDetailsPaneLayout.setHorizontalGroup(
            userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(userDetailsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(userDetailsPaneLayout.createSequentialGroup()
                        .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .add(jLabel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                            .add(jLabel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, usernameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, ipLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, screenNameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .add(listenPortLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, hostnameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, fullSipAddressLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel13, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                    .add(userDetailsPaneLayout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(16, 16, 16))
                    .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                .addContainerGap())
        );
        userDetailsPaneLayout.setVerticalGroup(
            userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, userDetailsPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(screenNameLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(usernameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel7)
                    .add(ipLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel9)
                    .add(hostnameLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(userDetailsPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(listenPortLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(34, 34, 34)
                .add(jLabel13)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(fullSipAddressLabel)
                .add(144, 144, 144))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("gogoSIP");
        setMinimumSize(new java.awt.Dimension(208, 500));

        statusBarPane.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(0, 0, 0)));

        statusTextLabel.setText("Connecting");

        statusBusyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout statusBarPaneLayout = new org.jdesktop.layout.GroupLayout(statusBarPane);
        statusBarPane.setLayout(statusBarPaneLayout);
        statusBarPaneLayout.setHorizontalGroup(
            statusBarPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusBarPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusTextLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .add(27, 27, 27)
                .add(statusBusyLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        statusBarPaneLayout.setVerticalGroup(
            statusBarPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusBarPaneLayout.createSequentialGroup()
                .add(statusBarPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, statusBusyLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, statusTextLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cardPane.setLayout(new java.awt.CardLayout());
        cardPane.add(loginPane, "card2");

        tabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tabbedPane.setFocusable(false);
        tabbedPane.setMinimumSize(new java.awt.Dimension(200, 200));
        tabbedPane.setPreferredSize(new java.awt.Dimension(300, 140));

        callPane.setBackground(new java.awt.Color(255, 255, 255));
        callPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        callInfoPane.setBackground(new java.awt.Color(102, 255, 102));

        callInfoLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        callInfoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        callInfoLabel.setText("You are online!");
        callInfoLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        callInfoLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        org.jdesktop.layout.GroupLayout callInfoPaneLayout = new org.jdesktop.layout.GroupLayout(callInfoPane);
        callInfoPane.setLayout(callInfoPaneLayout);
        callInfoPaneLayout.setHorizontalGroup(
            callInfoPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(callInfoPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(callInfoLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                .addContainerGap())
        );
        callInfoPaneLayout.setVerticalGroup(
            callInfoPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(callInfoPaneLayout.createSequentialGroup()
                .add(callInfoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1, Short.MAX_VALUE))
        );

        ringLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ringLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        callPartyLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        callPartyLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        org.jdesktop.layout.GroupLayout callPaneLayout = new org.jdesktop.layout.GroupLayout(callPane);
        callPane.setLayout(callPaneLayout);
        callPaneLayout.setHorizontalGroup(
            callPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(callPaneLayout.createSequentialGroup()
                .addContainerGap()
                .add(callPartyLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .add(9, 9, 9))
            .add(callInfoPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(callPaneLayout.createSequentialGroup()
                .add(19, 19, 19)
                .add(ringLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .add(22, 22, 22))
        );
        callPaneLayout.setVerticalGroup(
            callPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, callPaneLayout.createSequentialGroup()
                .addContainerGap(297, Short.MAX_VALUE)
                .add(ringLabel)
                .add(41, 41, 41)
                .add(callPartyLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(callInfoPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tabbedPane.addTab("General", callPane);

        callComboBox.setEditable(true);
        callComboBox.setMaximumRowCount(4);
        callComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));
        callComboBox.setMinimumSize(new java.awt.Dimension(20, 20));
        callComboBox.setPreferredSize(new java.awt.Dimension(20, 2));

        callButtonsPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20, 5));

        callButton.setBorder(null);
        callButton.setBorderPainted(false);
        callButton.setContentAreaFilled(false);
        callButton.setEnabled(false);
        callButton.setFocusPainted(false);
        callButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        callButtonsPane.add(callButton);

        hangupButton.setBorder(null);
        hangupButton.setBorderPainted(false);
        hangupButton.setContentAreaFilled(false);
        hangupButton.setEnabled(false);
        hangupButton.setFocusPainted(false);
        hangupButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        callButtonsPane.add(hangupButton);

        org.jdesktop.layout.GroupLayout mainPaneLayout = new org.jdesktop.layout.GroupLayout(mainPane);
        mainPane.setLayout(mainPaneLayout);
        mainPaneLayout.setHorizontalGroup(
            mainPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPaneLayout.createSequentialGroup()
                .add(mainPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .add(callComboBox, 0, 223, Short.MAX_VALUE)
                    .add(mainPaneLayout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(callButtonsPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)))
                .add(0, 0, 0))
        );
        mainPaneLayout.setVerticalGroup(
            mainPaneLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPaneLayout.createSequentialGroup()
                .add(tabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(callComboBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(12, 12, 12)
                .add(callButtonsPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardPane.add(mainPane, "card3");

        fileMenu.setText("File");

        logoutMenuItem.setText("Logout");
        fileMenu.add(logoutMenuItem);
        fileMenu.add(fmSseparator);

        exitMenuItem.setText("Exit");
        fileMenu.add(exitMenuItem);

        menuBar1.add(fileMenu);

        toolsMenu.setText("Tools");
        toolsMenu.add(tmSeparator);

        optionsMenuItem.setText("Options");
        toolsMenu.add(optionsMenuItem);

        menuBar1.add(toolsMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar1.add(helpMenu);

        setJMenuBar(menuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusBarPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, cardPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(cardPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusBarPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrimaryWindow().setVisible(true);
            }
        });
    }

    public javax.swing.JButton getCallButton() {
        return callButton;
    }

    public void setCallButton(javax.swing.JButton callButton) {
        this.callButton = callButton;
    }

    public javax.swing.JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public void setExitMenuItem(javax.swing.JMenuItem exitMenuItem) {
        this.exitMenuItem = exitMenuItem;
    }

    public javax.swing.JButton getHangupButton() {
        return hangupButton;
    }

    public void setHangupButton(javax.swing.JButton hangupButton) {
        this.hangupButton = hangupButton;
    }

    public javax.swing.JLabel getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(javax.swing.JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }

    public javax.swing.JPanel getStatusPane() {
        return statusPane;
    }

    public void setStatusPane(javax.swing.JPanel statusPane) {
        this.statusPane = statusPane;
    }

    public javax.swing.JMenuItem getAboutMenuItem() {
        return aboutMenuItem;
    }

    public void setAboutMenuItem(javax.swing.JMenuItem aboutMenuItem) {
        this.aboutMenuItem = aboutMenuItem;
    }

    public javax.swing.JMenuItem getOptionsMenuItem() {
        return optionsMenuItem;
    }

    public void setOptionsMenuItem(javax.swing.JMenuItem optionsMenuItem) {
        this.optionsMenuItem = optionsMenuItem;
    }

    public javax.swing.JTabbedPane getJTabbedPane1() {
        return tabbedPane;
    }

    public javax.swing.JLabel getCallInfoLabel() {
        return callInfoLabel;
    }

    public javax.swing.JPanel getCallInfoPane() {
        return callInfoPane;
    }

    public javax.swing.JPanel getCallPane() {
        return callPane;
    }

    public javax.swing.JLabel getPartyLabel() {
        return callPartyLabel;
    }

    public void addCallTab(String tabName){
    	tabbedPane.add(tabName, callPane);
    	tabbedPane.setSelectedComponent(callPane);
    }

    public void removeCallTab(){
    	tabbedPane.remove(callPane);
    }

    public javax.swing.JPanel getUserDetailsPane() {
        return userDetailsPane;
    }

    public javax.swing.JLabel getFullSipAddressLabel() {
        return fullSipAddressLabel;
    }

    public javax.swing.JLabel getHostnameLabel() {
        return hostnameLabel;
    }

    public javax.swing.JLabel getIpLabel() {
        return ipLabel;
    }

    public javax.swing.JLabel getListenPortLabel() {
        return listenPortLabel;
    }

    public javax.swing.JLabel getScreenNameLabel() {
        return screenNameLabel;
    }

    public javax.swing.JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public javax.swing.JComboBox getCallComboBox() {
        return callComboBox;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton callButton;
    private javax.swing.JPanel callButtonsPane;
    private javax.swing.JComboBox callComboBox;
    private javax.swing.JLabel callInfoLabel;
    private javax.swing.JPanel callInfoPane;
    private javax.swing.JPanel callPane;
    private javax.swing.JLabel callPartyLabel;
    private javax.swing.JPanel cardPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator fmSseparator;
    private javax.swing.JLabel fullSipAddressLabel;
    private javax.swing.JButton hangupButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel hostnameLabel;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel listenPortLabel;
    private LoginPanel loginPane;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JPanel mainPane;
    private javax.swing.JMenuBar menuBar1;
    private javax.swing.JMenuItem optionsMenuItem;
    private javax.swing.JLabel ringLabel;
    private javax.swing.JLabel screenNameLabel;
    private javax.swing.JPanel statusBarPane;
    private javax.swing.JLabel statusBusyLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JPanel statusPane;
    private javax.swing.JLabel statusTextLabel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JSeparator tmSeparator;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JPanel userDetailsPane;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables

    public LoginPanel getLoginPane() {
        return loginPane;
    }

    public javax.swing.JPanel getStatusBarPane() {
        return statusBarPane;
    }

    public javax.swing.JLabel getStatusBusyLabel() {
        return statusBusyLabel;
    }

    public javax.swing.JLabel getStatusTextLabel() {
        return statusTextLabel;
    }

    public javax.swing.JPanel getCardPane() {
        return cardPane;
    }

    public javax.swing.JMenuItem getLogoutMenuItem() {
        return logoutMenuItem;
    }

}
