/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package inherited;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author FrankFLY
 */
public abstract class DocumentListenerAdapter implements DocumentListener {

    @Override
    public void insertUpdate(DocumentEvent e) {
        modified();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        modified();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        modified();
    }

    public abstract void modified();

}
