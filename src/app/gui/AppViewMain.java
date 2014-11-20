/**
 * 
 * AppViewMain.java
 * 
 * Copyright (c) 2014, Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 *
 */

package app.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;
import app.model.License;
import app.util.UtilConfig;
import app.util.UtilTranslate;

public class AppViewMain extends JFrame {

	private static final long serialVersionUID = -7813296269670165536L;
	private AppController controller;
	private Vector<JMenuItem> menuItems;
	private Vector<JButton> buttons;
	private UtilConfig config;
	private UtilTranslate translate;
	private JMenuBar menuBar;
	private JMenu menuOptions;
	private JMenuItem menuItemShowConfig;
	private JMenuItem menuItemClose;
	private JMenu menuHelp;
	private JMenuItem menuItemAbout;
	private JLabel lblPath;
	private JTextField txtPath;
	private JButton btnSelectPath;
	private JPanel pnlCentral;
	private JLabel lblName;
	private JTextField txtName;
	private JLabel lblCopyright;
	private JTextField txtCopyright;
	private JLabel lblLicense;
	private JComboBox<License> cmbLicense;
	private JLabel lblYear;
	private JTextField txtYear;
	private JLabel lblExtension;
	private JTextField txtExtension;
	private JButton btnSave;
	private JLabel lblWriteLicence;
	private JCheckBox chkWriteLicence;
	private JLabel lblWriteHeaders;
	private JCheckBox chkWriteHeaders;

	public AppViewMain() {
		config = UtilConfig.getInstance();
		translate = UtilTranslate.getInstance();

		menuItems = new Vector<JMenuItem>();
		buttons = new Vector<JButton>();
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		init();
		setLocationRelativeTo(this);
		setVisible(true);
	}

	private void init() {
		setLayout(new BorderLayout());
		setSize(400, 370);
		setTitle(config.get("app.name"));

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menuOptions = new JMenu(translate.get("gui.options"));
		menuBar.add(menuOptions);

		menuItemShowConfig = new JMenuItem(translate.get("gui.showconfig"));
		menuOptions.add(menuItemShowConfig);

		menuOptions.add(new JSeparator());

		menuItemClose = new JMenuItem(translate.get("gui.close"));
		menuOptions.add(menuItemClose);

		menuHelp = new JMenu(translate.get("gui.help"));
		menuBar.add(menuHelp);

		menuItemAbout = new JMenuItem(translate.get("gui.about"));
		menuHelp.add(menuItemAbout);

		lblPath = new JLabel(translate.get("gui.pathproject"));
		txtPath = new JTextField();
		btnSelectPath = new JButton("...");

		lblName = new JLabel(translate.get("gui.nameproject"));
		txtName = new JTextField();

		lblCopyright = new JLabel(translate.get("gui.copyright"));
		txtCopyright = new JTextField();

		lblYear = new JLabel(translate.get("gui.year"));
		txtYear = new JTextField();

		lblExtension = new JLabel(translate.get("gui.extension"));
		txtExtension = new JTextField();
		txtExtension.setToolTipText(translate.get("gui.exampleextension"));

		lblLicense = new JLabel(translate.get("gui.license"));
		cmbLicense = new JComboBox<License>();

		lblWriteLicence = new JLabel(translate.get("gui.writelicence"));
		chkWriteLicence = new JCheckBox();

		lblWriteHeaders = new JLabel(translate.get("gui.writeheaders"));
		chkWriteHeaders = new JCheckBox();

		btnSave = new JButton(translate.get("gui.save"));

		pnlCentral = new JPanel();
		pnlCentral.setLayout(new MigLayout());
		add(pnlCentral, BorderLayout.CENTER);
		pnlCentral.add(lblPath, "height 25");
		pnlCentral.add(txtPath, "width 100%, height 25");
		pnlCentral.add(btnSelectPath, "width 20, height 25, wrap");
		pnlCentral.add(lblName, "grow, height 25");
		pnlCentral.add(txtName, "grow, span 2, height 25, wrap");
		pnlCentral.add(lblCopyright, "grow, height 25");
		pnlCentral.add(txtCopyright, "grow, span 2, height 25, wrap");
		pnlCentral.add(lblYear, "grow, height 25");
		pnlCentral.add(txtYear, "grow, span 2, height 25, wrap");
		pnlCentral.add(lblExtension, "grow, height 25");
		pnlCentral.add(txtExtension, "grow, span 2, height 25, wrap");
		pnlCentral.add(lblLicense, "grow, height 25");
		pnlCentral.add(cmbLicense, "grow, span 2, height 25, wrap");
		pnlCentral.add(lblWriteLicence, "grow, height 25");
		pnlCentral.add(chkWriteLicence, "grow, span 2, height 25, wrap");
		pnlCentral.add(lblWriteHeaders, "grow, height 25");
		pnlCentral.add(chkWriteHeaders, "grow, span 2, height 25, wrap 20");
		pnlCentral.add(btnSave, "grow, span 3, height 30");

		menuItems.add(menuItemShowConfig);
		menuItems.add(menuItemClose);
		menuItems.add(menuItemAbout);
		buttons.add(btnSelectPath);
		buttons.add(btnSave);
	}

	public void setController(AppController controller) {
		this.controller = controller;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		for (int i = 0; i < menuItems.size(); i++) {
			menuItems.get(i).addActionListener(controller);
		}
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).addActionListener(controller);
		}
	}

	public void close() {
		if (controller != null)
			controller.close();
	}

	public JMenuItem getMenuItemShowConfig() {
		return menuItemShowConfig;
	}

	public JMenuItem getMenuItemClose() {
		return menuItemClose;
	}

	public JMenuItem getMenuItemAbout() {
		return menuItemAbout;
	}

	public JTextField getTxtPath() {
		return txtPath;
	}

	public JButton getBtnSelectPath() {
		return btnSelectPath;
	}

	public JTextField getTxtName() {
		return txtName;
	}

	public JTextField getTxtCopyright() {
		return txtCopyright;
	}

	public JComboBox<License> getCmbLicense() {
		return cmbLicense;
	}

	public JTextField getTxtYear() {
		return txtYear;
	}

	public JTextField getTxtExtension() {
		return txtExtension;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JMenu getMenuOptions() {
		return menuOptions;
	}

	public JMenu getMenuHelp() {
		return menuHelp;
	}

	public JCheckBox getChkWriteLicence() {
		return chkWriteLicence;
	}

	public JCheckBox getChkWriteHeaders() {
		return chkWriteHeaders;
	}

}
