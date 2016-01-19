/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE file.
 */

package jchooselicense.view;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import net.miginfocom.swing.MigLayout;
import jchooselicense.base.View;
import jchooselicense.model.License;
import jchooselicense.util.JUrlLinkLabel;

public class ViewLicenses extends View {

	private static final long serialVersionUID = 7329136884614247511L;

	private JList<License> lstLicenses;
	private JTextArea logArea;
	private JScrollPane scrollLicenses;
	private JPanel panelLicense;
	private JPanel panelConfig;
	private JCheckBox chkWriteHeaders;
	private JCheckBox chkWriteFileName;
	private JCheckBox chkWriteLicense;
	private JPanel panelLicenses;
	private JPanel panelConfigApp;
	private JComboBox<File> cmbTranslate;
	private JLabel lblCmbTranslate;
	private JLabel lblCode;
	private JLabel lblValueCode;
	private JLabel lblName;
	private JLabel lblValueName;
	private JLabel lblUrl;
	private JUrlLinkLabel lblValueUrl;
	private JPanel panelAction;
	private JButton btnClose;
	private JButton btnSave;
	private JMenuBar menuBar;
	private JMenu menuOptions;
	private JMenuItem menuShowConfig;
	private JMenuItem menuClose;
	private JMenu menuHelp;
	private JMenuItem menuAbout;
	private JPanel panelProject;
	private JTextField txtPath;
	private JButton btnSelectPath;
	private JLabel lblPath;
	private JCheckBox chkFilter;
	private JTextField txtFilter;
	private JPanel panelParameters;
	private JTable tblParameters;

	@Override
	public void init() {
		setLayout(new BorderLayout());
		setSize(800, 600);

		// BAR

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menuOptions = new JMenu();
		menuBar.add(menuOptions);

		menuShowConfig = new JMenuItem();
		menuOptions.add(menuShowConfig);

		menuOptions.add(new JSeparator());

		menuClose = new JMenuItem();
		menuOptions.add(menuClose);

		menuHelp = new JMenu();
		menuBar.add(menuHelp);

		menuAbout = new JMenuItem();
		menuHelp.add(menuAbout);

		addMenuItemCtrl(menuAbout);
		addMenuItemCtrl(menuHelp);
		addMenuItemCtrl(menuOptions);
		addMenuItemCtrl(menuShowConfig);
		addMenuItemCtrl(menuClose);

		// SOUTH PANEL

		JPanel panelSouth = new JPanel();
		panelSouth.setLayout(new MigLayout());
		add(panelSouth, BorderLayout.SOUTH);
		logArea = new JTextArea();
		JScrollPane scrollLog = new JScrollPane();
		scrollLog.setViewportView(logArea);
		DefaultCaret caret = (DefaultCaret) logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		panelSouth.add(scrollLog, "width 100%, height 100");

		// PANEL WEST

		JPanel panelWest = new JPanel();
		panelWest.setLayout(new MigLayout());
		add(panelWest, BorderLayout.WEST);

		panelConfigApp = new JPanel();
		panelConfigApp.setLayout(new MigLayout());
		panelWest.add(panelConfigApp, "grow, wrap");

		lblCmbTranslate = new JLabel();
		cmbTranslate = new JComboBox<File>();
		addComboBoxCtrl(cmbTranslate);
		panelConfigApp.add(lblCmbTranslate, "height 25");
		panelConfigApp.add(cmbTranslate, "height 25, width 100");

		panelLicenses = new JPanel();
		panelLicenses.setLayout(new MigLayout());
		panelWest.add(panelLicenses, "wrap, width 250");

		lstLicenses = new JList<License>();
		addListCtrl(lstLicenses);
		scrollLicenses = new JScrollPane(lstLicenses);
		panelLicenses.add(scrollLicenses, "width 100%");

		panelConfig = new JPanel();
		panelConfig.setLayout(new MigLayout());
		panelWest.add(panelConfig, "grow, wrap");

		chkWriteLicense = new JCheckBox("", true);
		chkWriteHeaders = new JCheckBox("", true);
		chkWriteFileName = new JCheckBox("", true);

		panelConfig.add(chkWriteLicense, "grow, height 25, wrap");
		panelConfig.add(chkWriteHeaders, "grow, height 25, wrap");
		panelConfig.add(chkWriteFileName, "grow, height 25");

		panelAction = new JPanel();
		panelAction.setLayout(new MigLayout("insets 2 2 2 2"));
		panelWest.add(panelAction, "width 250, wrap");

		btnClose = new JButton();
		addButtonCtrl(btnClose);
		btnSave = new JButton();
		addButtonCtrl(btnSave);

		panelAction.add(btnClose, "width 50%");
		panelAction.add(btnSave, "width 50%");

		// PANEL CENTER

		JPanel panelCenter = new JPanel();
		panelCenter.setLayout(new MigLayout());
		add(panelCenter, BorderLayout.CENTER);

		panelLicense = new JPanel();
		panelLicense.setLayout(new MigLayout());
		panelCenter.add(panelLicense, "width 100%, wrap");

		lblCode = new JLabel();
		lblValueCode = new JLabel();

		lblName = new JLabel();
		lblValueName = new JLabel();

		lblUrl = new JLabel();
		lblValueUrl = new JUrlLinkLabel();

		panelLicense.add(lblCode, "height 25");
		panelLicense.add(lblValueCode, "grow, height 25, wrap");
		panelLicense.add(lblName, "height 25");
		panelLicense.add(lblValueName, "grow, height 25, wrap");
		panelLicense.add(lblUrl, "height 25");
		panelLicense.add(lblValueUrl, "grow, height 25, wrap");

		panelProject = new JPanel();
		panelProject.setLayout(new MigLayout());
		panelCenter.add(panelProject, "width 100%, wrap");

		lblPath = new JLabel();
		txtPath = new JTextField();
		btnSelectPath = new JButton("...");
		addButtonCtrl(btnSelectPath);

		chkFilter = new JCheckBox("", true);
		txtFilter = new JTextField(".*");

		panelProject.add(lblPath, "height 25");
		panelProject.add(txtPath, "width 100%, height 25");
		panelProject.add(btnSelectPath, "width 20, height 25, wrap");
		panelProject.add(chkFilter, "height 25");
		panelProject.add(txtFilter, "width 100%, height 25");

		panelParameters = new JPanel();
		panelParameters.setLayout(new MigLayout());
		panelCenter.add(panelParameters, "width 100%");

		tblParameters = new JTable();
		JScrollPane scrollParameters = new JScrollPane();
		scrollParameters.setViewportView(tblParameters);
		panelParameters.add(scrollParameters, "width 100%, grow");
	}

	public void setTrlChkFilter(String trl) {
		chkFilter.setText(trl);
	}

	public void setTrlLblPath(String trl) {
		lblPath.setText(trl);
	}

	public void setTrlMenuHelp(String trl) {
		menuHelp.setText(trl);
	}

	public void setTrlMenuAbout(String trl) {
		menuAbout.setText(trl);
	}

	public void setTrlMenuClose(String trl) {
		menuClose.setText(trl);
	}

	public void setTrlMenuShowConfig(String trl) {
		menuShowConfig.setText(trl);
	}

	public void setTrlMenuOptions(String trl) {
		menuOptions.setText(trl);
	}

	public void setTrlBtnClose(String trl) {
		btnClose.setText(trl);
	}

	public void setTrlBtnSave(String trl) {
		btnSave.setText(trl);
	}

	public void setTrlLblUrl(String trl) {
		lblUrl.setText(trl);
	}

	public void setTrlLblName(String trl) {
		lblName.setText(trl);
	}

	public void setTrlLblCode(String trl) {
		lblCode.setText(trl);
	}

	public void setTrlChkWriteLicense(String trl) {
		chkWriteLicense.setText(trl);
	}

	public void setTrlChkWriteHeaders(String trl) {
		chkWriteHeaders.setText(trl);
	}

	public void setTrlChkWriteFileName(String trl) {
		chkWriteFileName.setText(trl);
	}

	public void setTrlPanelParameters(String trl) {
		panelParameters.setBorder(BorderFactory.createTitledBorder(trl));
	}

	public void setTrlPanelLicense(String trl) {
		panelLicense.setBorder(BorderFactory.createTitledBorder(trl));
	}

	public void setTrlPanelLicenses(String trl) {
		panelLicenses.setBorder(BorderFactory.createTitledBorder(trl));
	}

	public void setTrlPanelProject(String trl) {
		panelProject.setBorder(BorderFactory.createTitledBorder(trl));
	}

	public void setTrlPanelConfig(String trl) {
		panelConfig.setBorder(BorderFactory.createTitledBorder(trl));
	}

	public void setTrlPanelConfigApp(String trl) {
		panelConfigApp.setBorder(BorderFactory.createTitledBorder(trl));
	}

	public void setTrlLblCmbTranslate(String trl) {
		lblCmbTranslate.setText(trl);
	}

	public void appendLog(String msg) {
		logArea.append(msg);
	}

	public JList<License> getLstLicenses() {
		return lstLicenses;
	}

	public JCheckBox getChkWriteHeaders() {
		return chkWriteHeaders;
	}

	public JCheckBox getChkWriteFileName() {
		return chkWriteFileName;
	}

	public JCheckBox getChkWriteLicense() {
		return chkWriteLicense;
	}

	public JComboBox<File> getCmbTranslate() {
		return cmbTranslate;
	}

	public JLabel getLblValueCode() {
		return lblValueCode;
	}

	public JLabel getLblValueName() {
		return lblValueName;
	}

	public JUrlLinkLabel getLblValueUrl() {
		return lblValueUrl;
	}

	public JButton getBtnClose() {
		return btnClose;
	}

	public JButton getBtnSave() {
		return btnSave;
	}

	public JMenu getMenuOptions() {
		return menuOptions;
	}

	public JMenuItem getMenuShowConfig() {
		return menuShowConfig;
	}

	public JMenuItem getMenuClose() {
		return menuClose;
	}

	public JMenu getMenuHelp() {
		return menuHelp;
	}

	public JMenuItem getMenuAbout() {
		return menuAbout;
	}

	public JTextField getTxtPath() {
		return txtPath;
	}

	public JButton getBtnSelectPath() {
		return btnSelectPath;
	}

	public JCheckBox getChkFilter() {
		return chkFilter;
	}

	public JTextField getTxtFilter() {
		return txtFilter;
	}

	public JTable getTblParameters() {
		return tblParameters;
	}

}
