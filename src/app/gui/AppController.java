/**
 * 
 * AppController.java
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import app.model.License;
import app.util.UtilConfig;
import app.util.UtilFile;
import app.util.UtilLog;
import app.util.UtilTranslate;

public class AppController implements ActionListener {

	private AppViewMain appView;
	private UtilConfig config;
	private UtilLog log;
	private UtilTranslate translate;
	private List<License> licenses;
	private UtilFile utilFile;
	private DefaultComboBoxModel<License> cmbLicenseModel;
	private String[] extensions;
	private String[] allowExtensions = { "java", "php", "h", "cpp" };
	private License license;

	public AppController() {
		config = UtilConfig.getInstance();
		log = UtilLog.getInstance();
		translate = UtilTranslate.getInstance();

		appView = new AppViewMain();
		appView.setController(this);

		appView.getTxtCopyright().setText(config.get("default.copyright"));
		appView.getTxtName().setText(config.get("default.appname"));
		appView.getTxtYear().setText(String.format("%tY", new Date()));
		appView.getTxtExtension().setText(config.get("default.fileextension"));

		licenses = new ArrayList<License>();
		cmbLicenseModel = new DefaultComboBoxModel<License>();

		utilFile = new UtilFile();

		File[] filesLicenses = utilFile.directories(config.get("path.licenses"));

		for (File file : filesLicenses) {
			if (file.isDirectory()) {
				License l = new License(file);
				licenses.add(l);
				cmbLicenseModel.addElement(l);
			}
		}

		appView.getCmbLicense().setModel(cmbLicenseModel);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(appView.getMenuItemClose()))
			close();
		else if (source.equals(appView.getMenuItemAbout()))
			showAbout();
		else if (source.equals(appView.getMenuItemShowConfig()))
			showConfig();
		else if (source.equals(appView.getBtnSelectPath()))
			selectPath();
		else if (source.equals(appView.getBtnSave()))
			save();
	}

	public void editFile(File file) throws IOException {
		if (!file.isFile())
			return;
		if (!utilFile.isFileType(file, extensions))
			return;

		String msg = String.format(translate.get("log.processfile"), file.getName());

		appView.getLblStatusBar().setText(msg);
		log.info(getClass(), msg);

		license.setHeader(file, appView.getTxtName().getText(), appView.getTxtCopyright().getText(), appView.getTxtYear().getText());

	}

	public void findFile(File file) throws IOException {
		if (file.isFile())
			editFile(file);
		else
			for (File f : file.listFiles())
				findFile(f);
	}

	public void enableDisable(boolean e) {
		appView.getTxtCopyright().setEnabled(e);
		appView.getTxtExtension().setEnabled(e);
		appView.getTxtName().setEnabled(e);
		appView.getTxtPath().setEnabled(e);
		appView.getTxtYear().setEnabled(e);
		appView.getMenuHelp().setEnabled(e);
		appView.getMenuOptions().setEnabled(e);
		appView.getCmbLicense().setEnabled(e);
		appView.getBtnSave().setEnabled(e);
		appView.getBtnSelectPath().setEnabled(e);
	}

	public void save() {
		String stringPath = appView.getTxtPath().getText().trim();

		if (stringPath.isEmpty()) {
			String msg = translate.get("error.pathempty");
			appView.getLblStatusBar().setText(msg);
			log.error(getClass(), msg);
			JOptionPane.showMessageDialog(appView, msg, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		extensions = appView.getTxtExtension().getText().split(",");

		boolean allowExt = false;
		for (String ext2 : extensions) {
			allowExt = false;
			for (String ext1 : allowExtensions) {
				if (ext1.equals(ext2)) {
					allowExt = true;
					break;
				}
			}
			if (!allowExt)
				break;
		}

		if (!allowExt) {
			String msg = String.format(translate.get("error.allowedextesions"), Arrays.toString(allowExtensions));
			appView.getLblStatusBar().setText(msg);
			log.error(getClass(), msg);
			JOptionPane.showMessageDialog(appView, msg, "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		license = (License) appView.getCmbLicense().getSelectedItem();

		enableDisable(false);
		File path = new File(stringPath);

		try {
			license.copyLicence(path, appView.getTxtName().getText(), appView.getTxtCopyright().getText(), appView.getTxtYear().getText());
			String msg = String.format(translate.get("info.savelicense"));
			appView.getLblStatusBar().setText(msg);
			log.info(getClass(), msg);
		} catch (IOException e) {
			String msg = String.format(translate.get("error.savelicense"));
			appView.getLblStatusBar().setText(msg);
			log.error(getClass(), e, msg);
			JOptionPane.showMessageDialog(appView, msg, "Error", JOptionPane.ERROR_MESSAGE);
		}

		try {
			findFile(path);
		} catch (IOException e) {
			String msg = String.format(translate.get("error.savelicense"));
			appView.getLblStatusBar().setText(msg);
			log.error(getClass(), e, msg);
			JOptionPane.showMessageDialog(appView, msg, "Error", JOptionPane.ERROR_MESSAGE);
		}
		enableDisable(true);
	}

	public void selectPath() {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fileChooser.showSaveDialog(appView);
		File path = fileChooser.getSelectedFile();

		if (path != null) {
			appView.getTxtPath().setText(fileChooser.getSelectedFile().getAbsolutePath());
		}

	}

	public void showAbout() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AppViewAbout();
			}
		});
	}

	public void showConfig() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AppViewConfig();
			}
		});
	}

	public void close() {
		appView.dispose();
		System.exit(0);
	}

}
