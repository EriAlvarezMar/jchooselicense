/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 */

package jchooselicense.base;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public abstract class View extends JFrame {

	private static final long serialVersionUID = 4787719384159443057L;
	private Vector<JMenuItem> menuItems;
	private Vector<JButton> buttons;
	private Vector<JComboBox<?>> comboBoxses;
	private Vector<JList<?>> lists;
	private Ctrl controller;

	public void addComboBoxCtrl(JComboBox<?> comboBox) {
		if (!comboBoxses.contains(comboBox))
			comboBoxses.add(comboBox);

		if (controller != null) {
			comboBox.removeActionListener(controller);
			comboBox.addActionListener(controller);
		}
	}

	public void addListCtrl(JList<?> list) {
		if (!lists.contains(list))
			lists.add(list);

		if (controller != null) {
			list.removeListSelectionListener(controller);
			list.addListSelectionListener(controller);
		}
	}

	public void addButtonCtrl(JButton button) {
		if (!buttons.contains(button))
			buttons.add(button);

		if (controller != null) {
			button.removeActionListener(controller);
			button.addActionListener(controller);
		}
	}

	public void addMenuItemCtrl(JMenuItem menuItem) {
		if (!menuItems.contains(menuItem))
			menuItems.add(menuItem);

		if (controller != null) {
			menuItem.removeActionListener(controller);
			menuItem.addActionListener(controller);
		}
	}

	public Ctrl getController() {
		return controller;
	}

	public void setController(Ctrl controller) {
		this.controller = controller;
		removeWindowListener(controller);
		addWindowListener(controller);
		for (int i = 0; i < menuItems.size(); i++) {
			menuItems.get(i).removeActionListener(controller);
			menuItems.get(i).addActionListener(controller);
		}
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).removeActionListener(controller);
			buttons.get(i).addActionListener(controller);
		}
		for (int i = 0; i < comboBoxses.size(); i++) {
			comboBoxses.get(i).removeActionListener(controller);
			comboBoxses.get(i).addActionListener(controller);
		}
		for (int i = 0; i < lists.size(); i++) {
			lists.get(i).removeListSelectionListener(controller);
			lists.get(i).addListSelectionListener(controller);
		}
	}

	public abstract void init();

	public View() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		menuItems = new Vector<JMenuItem>();
		buttons = new Vector<JButton>();
		comboBoxses = new Vector<JComboBox<?>>();
		lists = new Vector<JList<?>>();
		init();
		setLocationRelativeTo(this);
	}

}
