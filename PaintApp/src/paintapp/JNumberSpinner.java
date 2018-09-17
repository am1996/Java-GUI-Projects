/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paintapp;

import java.awt.Dimension;
import javax.swing.*;
import javax.swing.event.*;

public class JNumberSpinner extends JSpinner{
    SpinnerNumberModel model;

    public JNumberSpinner(int strokeSize) {
        this.model = new SpinnerNumberModel(strokeSize,1,2000,1);
        this.setModel(this.model);
        this.setMaximumSize(new Dimension(50,50));
        this.setAlignmentY(CENTER_ALIGNMENT);
    }
}
