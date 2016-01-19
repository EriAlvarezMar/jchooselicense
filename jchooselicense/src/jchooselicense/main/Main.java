package jchooselicense.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import jchooselicense.app.AppConfig;
import jchooselicense.app.AppLog;
import jchooselicense.app.AppTranslate;
import jchooselicense.app.AppLog.UtilLogLevel;
import jchooselicense.ctrl.CtrlViewLicenses;

public class Main {

	public static void main(String[] args) {
		AppConfig config = AppConfig.getInstance();
		AppLog log = AppLog.getInstance();
		AppTranslate translate = AppTranslate.getInstance();

		try {
			File configFile = new File("conf/app");
			if (!configFile.exists())
				throw new FileNotFoundException("Application config file not found, path: \"config/app\"");
			config.load(configFile);
			log.setLevel(UtilLogLevel.valueOf(config.get("app.log_level", UtilLogLevel.DEVEL.toString()).toUpperCase()));
			log.info(Main.class, "Config loaded");

			translate.load(Paths.get(config.get("app.trl_path", "trl"), config.get("app.trl", "en")).toString());
			log.info(Main.class, "Translate loaded");

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CtrlViewLicenses();
			}
		});

	}
}
