/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE file.
 */

package jchooselicense.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class Ctrl implements ActionListener, WindowListener, ItemListener, ListSelectionListener {

	public abstract void initTrl();

	public abstract void initView();

	public abstract void init();

	public abstract void closeView();

	public abstract void action(Object source);

	public Ctrl() {
		init();
		initView();
		initTrl();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		action(e.getSource());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		action(e.getSource());
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		closeView();
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			action(e.getSource());
	}

}
