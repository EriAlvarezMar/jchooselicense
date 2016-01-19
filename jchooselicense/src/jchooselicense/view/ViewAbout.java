/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 */

package jchooselicense.view;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jchooselicense.app.AppConfig;
import net.miginfocom.swing.MigLayout;

public class ViewAbout extends JDialog {

	private static final long serialVersionUID = -3296703600845483586L;
	private AppConfig config;

	public ViewAbout() {
		config = AppConfig.getInstance();

		setSize(400, 170);
		setTitle(config.get("app.name"));

		JPanel panel = new JPanel(new MigLayout());
		add(panel);

		panel.add(new JLabel(config.get("app.name")), "width 300, wrap");
		panel.add(new JLabel(config.get("app.license")), "grow, wrap");
		panel.add(new JLabel(config.get("app.copyright")), "grow, wrap");

		setLocationRelativeTo(this);
		setModal(true);
		setVisible(true);
	}

}
