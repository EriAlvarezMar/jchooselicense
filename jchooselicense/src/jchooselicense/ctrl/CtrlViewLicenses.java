/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 */

package jchooselicense.ctrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jchooselicense.app.AppConfig;
import jchooselicense.app.AppLog;
import jchooselicense.app.AppTranslate;
import jchooselicense.base.Ctrl;
import jchooselicense.model.Language;
import jchooselicense.model.License;
import jchooselicense.util.UtilFile;
import jchooselicense.util.UtilFileText;
import jchooselicense.view.ViewAbout;
import jchooselicense.view.ViewConfig;
import jchooselicense.view.ViewLicenses;

public class CtrlViewLicenses extends Ctrl implements Runnable {

	private ViewLicenses view;
	private AppConfig config;
	private AppLog log;
	private AppTranslate translate;
	private String logPattern = "TYPE: %s\nTIME: %s\n----> %s\n\n";
	private DefaultComboBoxModel<File> cmbTranslateModel;
	private DefaultListModel<License> lstLicensesModel;
	private License licSelected;
	private ParametersModelTable parametersModelTable;
	private ArrayList<Language> lstLanguagesModel;
	private UtilFile utilFile;
	private UtilFileText utilFileText;
	private Gson gson;

	private void logError(String msgCode, String msgDefault, Exception e) {
		String msg = translate.get(msgCode, msgDefault);
		log.error(getClass(), e, msg);
		if (view != null) {
			view.appendLog(String.format(logPattern, "ERROR", (new Date()).toString(), msg));
		}
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void logError(String msgCode, String msgDefault) {
		String msg = translate.get(msgCode, msgDefault);
		log.error(getClass(), msg);
		if (view != null) {
			view.appendLog(String.format(logPattern, "ERROR", (new Date()).toString(), msg));
		}
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}

	private void logInfo(String msgCode, String msgDefault) {
		String msg = translate.get(msgCode, msgDefault);
		log.info(getClass(), msg);
		if (view != null) {
			view.appendLog(String.format(logPattern, "INFO", (new Date()).toString(), msg));
		}
	}

	private void logInfo(String msgCode, String msgDefault, Object... params) {
		String msg = translate.get(msgCode, msgDefault);
		for (Object object : params) {
			msg += object;
		}
		log.info(getClass(), msg);
		if (view != null) {
			view.appendLog(String.format(logPattern, "INFO", new Date(), msg));
		}
	}

	@Override
	public void initView() {
		try {
			view = new ViewLicenses();
			view.setController(this);
			view.setVisible(true);
			view.getCmbTranslate().setModel(cmbTranslateModel);
			view.getLstLicenses().setModel(lstLicensesModel);
			parametersModelTable = new ParametersModelTable(null, view.getTblParameters());

		} catch (Exception e) {
			logError("error.error_init_view", "Error launching view", e);
		}
	}

	@Override
	public void initTrl() {
		if (view == null)
			return;
		try {
			view.setTitle(config.get("app.name"));
			view.setTrlPanelLicense(translate.get("gui.license", "License"));
			view.setTrlPanelLicenses(translate.get("gui.licenses", "Licenses"));
			view.setTrlPanelConfig(translate.get("gui.config", "Configuration"));
			view.setTrlPanelConfigApp(translate.get("gui.app", "Application"));
			view.setTrlChkWriteFileName(translate.get("gui.write_file_name", "Write file name"));
			view.setTrlChkWriteHeaders(translate.get("gui.write_headers", "Write headers"));
			view.setTrlChkWriteLicense(translate.get("gui.write_license", "Write license"));
			view.setTrlLblCmbTranslate(translate.get("gui.trl", "Translate"));
			view.setTrlLblCode(translate.get("gui.code", "Code"));
			view.setTrlLblName(translate.get("gui.name", "Name"));
			view.setTrlLblUrl(translate.get("gui.url", "Url"));
			view.setTrlBtnClose(translate.get("gui.close", "Close"));
			view.setTrlBtnSave(translate.get("gui.save", "Save"));
			view.setTrlMenuOptions(translate.get("gui.options", "Options"));
			view.setTrlMenuShowConfig(translate.get("gui.show_config", "Show config"));
			view.setTrlMenuClose(translate.get("gui.close", "Close"));
			view.setTrlMenuAbout(translate.get("gui.about", "About"));
			view.setTrlMenuHelp(translate.get("gui.help", "Help"));
			view.setTrlPanelProject(translate.get("gui.project", "Project"));
			view.setTrlLblPath(translate.get("gui.path", "Path"));
			view.setTrlChkFilter(translate.get("gui.filter", "Filter"));
			view.setTrlPanelParameters(translate.get("gui.parameters", "Parameters"));

			ArrayList<String> titles = new ArrayList<String>();
			titles.add("#");
			titles.add(translate.get("gui.name"));
			titles.add(translate.get("gui.value"));
			parametersModelTable.setTitles(titles);
		} catch (Exception e) {
			logError("error.error_init_view", "Error launching view", e);
		}
	}

	@Override
	public void init() {
		try {
			utilFile = new UtilFile();
			utilFileText = new UtilFileText();
			gson = new Gson();
			config = AppConfig.getInstance();
			log = AppLog.getInstance();
			translate = AppTranslate.getInstance();
			loadLicensesModel();
			loadTranslateList();
			loadLanguagesModel();
		} catch (Exception e) {
			logError("error.error_init_view", "Error launching view", e);
		}
	}

	private void loadTranslateList() throws Exception {
		cmbTranslateModel = new DefaultComboBoxModel<File>();
		File[] translateDir = utilFile.files(config.get("app.trl_path", "trl"));
		if (translateDir == null || translateDir.length == 0)
			throw new FileNotFoundException(translate.get("error.translate_files_not_found", "Translate files not found"));

		for (File file : translateDir) {
			logInfo("info.loading_trl", "Loading translate: ", file.getName());
			cmbTranslateModel.addElement(file);
			if (config.get("app.trl").equals(file.getName()))
				cmbTranslateModel.setSelectedItem(file);
		}
		logInfo("info.trls_loaded", "Translates loaded");
	}

	private void loadLanguagesModel() throws Exception {
		lstLanguagesModel = new ArrayList<Language>();

		File[] langFiles = utilFile.files(config.get("app.langs_patch", "languages"));
		Arrays.sort(langFiles);

		if (langFiles == null || langFiles.length == 0)
			throw new FileNotFoundException(translate.get("error.langs_files_not_found", "Languages files not found"));

		for (File file : langFiles) {
			logInfo("info.loading_lang", "Loading language: ", file.getName());

			Language temp;
			try {
				temp = gson.fromJson(utilFileText.readFile(file.getAbsolutePath()), Language.class);
			} catch (JsonSyntaxException e) {
				throw new JsonSyntaxException(translate.get("error.lang_json_file", "Language json file error: ") + file.getName(), e);
			} catch (IOException e) {
				throw new IOException(translate.get("error.lang_file_not_found", "License file not found: ") + file.getName(), e);
			}
			lstLanguagesModel.add(temp);
		}
		logInfo("info.langs_loaded", "Languages loaded");
	}

	private void loadLicensesModel() throws Exception {
		lstLicensesModel = new DefaultListModel<License>();

		File[] licensesDir = utilFile.directories(config.get("app.lic_patch", "licenses"));
		Arrays.sort(licensesDir);

		if (licensesDir == null || licensesDir.length == 0)
			throw new FileNotFoundException(translate.get("error.licenses_files_not_found", "Licenses files not found"));

		for (File file : licensesDir) {
			logInfo("info.loading_lic", "Loading license: ", file.getName());
			String jsonPath = Paths.get(file.getAbsolutePath(), config.get("app.lic_config_name", "config.json")).toString();
			License temp;
			try {
				temp = gson.fromJson(utilFileText.readFile(jsonPath), License.class);
				temp.setPath(file.getAbsolutePath());
			} catch (JsonSyntaxException e) {
				throw new JsonSyntaxException(translate.get("error.license_json_file", "License json file error: ") + file.getName(), e);
			} catch (IOException e) {
				throw new IOException(translate.get("error.license_file_not_found", "License file not found: ") + file.getName(), e);
			}
			lstLicensesModel.addElement(temp);
		}
		logInfo("info.licenses_loaded", "Licenses loaded");
	}

	@Override
	public void closeView() {
		view.dispose();
		System.exit(0);
	}

	@Override
	public void action(Object source) {
		if (source == view.getCmbTranslate()) {
			try {
				File trlFile = (File) view.getCmbTranslate().getSelectedItem();
				config.set("app.trl", trlFile.getName());
				config.save();
				translate.load(trlFile);
				logInfo("info.trl_loaded", "Translate loaded");
				initTrl();
			} catch (Exception e) {
				logError("error.error_loading_trl", "Error loading translate", e);
			}

		} else if (source == view.getLstLicenses()) {
			licSelected = view.getLstLicenses().getSelectedValue();
			logInfo("info.lic_loaded", "License loaded", licSelected);

			view.getLblValueName().setText(licSelected.getName());
			view.getLblValueUrl().setText(licSelected.getUrl(), licSelected.getUrl());
			view.getLblValueCode().setText(licSelected.getCode());
			parametersModelTable.setLicense(licSelected);
		} else if (source == view.getBtnClose() || source == view.getMenuClose()) {
			closeView();
		} else if (source == view.getMenuAbout()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new ViewAbout();
				}
			});
		} else if (source == view.getMenuShowConfig()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new ViewConfig();
				}
			});
		} else if (source == view.getBtnSelectPath()) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			fileChooser.showSaveDialog(view);
			File path = fileChooser.getSelectedFile();

			if (path != null) {
				view.getTxtPath().setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		} else if (source == view.getBtnSave()) {
			String path = view.getTxtPath().getText();
			if (licSelected != null && path != null && !path.isEmpty()) {
				Thread t = new Thread(this);
				waitState();
				t.start();
			} else {
				logError("error.incomplete_data", "Incomplete data");
			}
		}
	}

	private void save(String path) throws Exception {
		logInfo("info.loading_lic", "Loading license: ", licSelected.toFullString());

		boolean writeHeaders = view.getChkWriteHeaders().isSelected();
		boolean writeLicense = view.getChkWriteLicense().isSelected();

		if (writeLicense) {
			licSelected.writeLicence(path);
		}

		if (writeHeaders) {
			addHeader(path);
		}
	}

	private void addHeader(String path) throws Exception {
		boolean writeFileName = view.getChkWriteFileName().isSelected();
		boolean filter = view.getChkFilter().isSelected();
		String strFilter = view.getTxtFilter().getText();

		File[] files = null;
		if (filter)
			files = utilFile.files(path, strFilter);
		else
			files = utilFile.files(path);

		for (File file : files) {
			for (Language lang : lstLanguagesModel) {
				if (lang.getExtensions().contains(utilFile.getExtension(file))) {
					logInfo("info.updating_file", "Updating file: ", file);
					licSelected.writeHeader(file, lang, writeFileName);
					break;
				}
			}
		}

		File[] directories = utilFile.directories(path);
		for (File directory : directories) {
			addHeader(directory.getAbsolutePath());
		}
	}

	public class ParametersModelTable extends AbstractTableModel {

		private static final long serialVersionUID = -2221773384058703929L;

		private License license;
		private JTable table;
		private List<String> titles;

		public void setLicense(License license) {
			this.license = license;
			fireTableDataChanged();
		}

		public void setTitles(List<String> titles) {
			this.titles = titles;
			fireTableStructureChanged();
			this.table.getColumnModel().getColumn(0).setMaxWidth(20);
		}

		public ParametersModelTable(License license, JTable table) {
			this.license = license;
			this.table = table;
			titles = new ArrayList<String>();
			titles.add("#");
			titles.add("");
			titles.add("");
			this.table.setModel(this);
			this.table.getColumnModel().getColumn(0).setMaxWidth(20);
		}

		@Override
		public int getRowCount() {
			if (license == null)
				return 0;
			return license.getParameters().size();
		}

		@Override
		public int getColumnCount() {
			return titles.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (license == null)
				return null;
			switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return license.getParameters().get(rowIndex).getName();
			case 2:
				return license.getParameters().get(rowIndex).getValue();
			}
			return null;
		}

		@Override
		public String getColumnName(int column) {
			return titles.get(column);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex == 2;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (license != null) {
				license.getParameters().get(rowIndex).setValue(aValue.toString());
			}
			super.setValueAt(aValue, rowIndex, columnIndex);
		}

	}

	private void waitState() {
		view.getTxtFilter().setEnabled(false);
		view.getTxtPath().setEnabled(false);
		view.getBtnSave().setEnabled(false);
		view.getBtnSelectPath().setEnabled(false);
		view.getCmbTranslate().setEnabled(false);
		view.getChkFilter().setEnabled(false);
		view.getChkWriteFileName().setEnabled(false);
		view.getChkWriteHeaders().setEnabled(false);
		view.getChkWriteLicense().setEnabled(false);
		view.getLstLicenses().setEnabled(false);
		view.getTblParameters().setEnabled(false);
	}

	private void normalState() {
		view.getTxtFilter().setEnabled(true);
		view.getTxtPath().setEnabled(true);
		view.getBtnSave().setEnabled(true);
		view.getBtnSelectPath().setEnabled(true);
		view.getCmbTranslate().setEnabled(true);
		view.getChkFilter().setEnabled(true);
		view.getChkWriteFileName().setEnabled(true);
		view.getChkWriteHeaders().setEnabled(true);
		view.getChkWriteLicense().setEnabled(true);
		view.getLstLicenses().setEnabled(true);
		view.getTblParameters().setEnabled(true);
	}

	@Override
	public void run() {
		String path = view.getTxtPath().getText();
		try {
			save(path);
		} catch (Exception e) {
			logError("error.executing_action", "Error executing action", e);
		}
		normalState();
		logInfo("info.successfully_saved", "Successfully saved");
	}

}
