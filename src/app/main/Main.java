package app.main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import app.gui.AppController;
import app.util.UtilConfig;
import app.util.UtilLog;
import app.util.UtilTranslate;
import app.util.UtilLog.UtilLogLevel;

public class Main {

	public static void main(String[] args) {
		UtilConfig config = UtilConfig.getInstance();
		UtilLog log = UtilLog.getInstance();
		UtilTranslate translate = UtilTranslate.getInstance();
		try {
			config.load("CONFIG.props");
			config.save();
			log.setLevel(UtilLogLevel.valueOf(config.get("log.level")));
			translate.load(config.get("app.translate"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getClass().getName() + "\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
		log.info(Main.class, translate.get("log.init"), log.getLevel());

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new AppController();
			}
		});
	}

}
