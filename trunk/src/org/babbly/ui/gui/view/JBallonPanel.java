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

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author mrj
 */
public class JBallonPanel extends JPanel{
    
    Color backgroundColor = null;
    
    public JBallonPanel(){
        this(null);
    }
    
    /** Creates a new instance of JBallon */
    public JBallonPanel(Color backgroundColor) {
     
        super();
        this.backgroundColor = backgroundColor;
        // We must be non-opaque since we won't fill all pixels.
        // This will also stop the UI from filling our background.
        setOpaque(false);

        // Add an empty border around us to compensate for
        // the rounded corners.
      // setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
    }

    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        // Paint a rounded rectangle in the background.
        g.setColor(backgroundColor);
        //g.fillArc(getX(), getY(), width, height, 210, 120);
//        System.out.println("getX : "+  getX());
//        System.out.println("getY : "+  getY());
        g.fillRoundRect(0, 0, width, height, 22, 22);
        // Now call the superclass behavior to paint the foreground.
        super.paintComponent(g);
    }
    
    
}
