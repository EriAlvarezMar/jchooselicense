/**
 * Copyright (c) 2016 Saúl Piña <sauljabin@gmail.com>.
 * 
 * This file is part of JChooseLicense.
 * 
 * JChooseLicense is licensed under The MIT License.
 * For full copyright and license information please see the LICENSE.txt file.
 */

package jchooselicense.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import jchooselicense.app.AppConfig;
import jchooselicense.app.AppTranslate;

public class ViewConfig extends JDialog {

	private static final long serialVersionUID = 6795269672488995634L;
	private AppConfig config;
	private AppTranslate translate;

	public ViewConfig() {
		config = AppConfig.getInstance();
		translate = AppTranslate.getInstance();

		setSize(500, 300);
		setTitle(config.get("app.name"));
		JScrollPane panel = new JScrollPane();
		add(panel);
		JTable table = new JTable();
		panel.setViewportView(table);
		new ConfigModelTable(table);
		setLocationRelativeTo(this);
		setModal(true);
		setVisible(true);
	}

	public class ConfigModelTable extends AbstractTableModel {

		private static final long serialVersionUID = -8276891304790814037L;
		private List<String> titles;
		private List<String> keys;
		private JTable table;

		public ConfigModelTable(JTable table) {
			titles = new ArrayList<String>();
			titles.add("#");
			titles.add(translate.get("gui.name"));
			titles.add(translate.get("gui.value"));
			keys = config.getKeys();
			Collections.sort(keys);

			this.table = table;
			this.table.setModel(this);
			this.table.getColumnModel().getColumn(0).setMaxWidth(20);
		}

		@Override
		public int getColumnCount() {
			return titles.size();
		}

		@Override
		public int getRowCount() {
			return keys.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return keys.get(rowIndex);
			case 2:
				return config.get(keys.get(rowIndex));
			}
			return null;
		}

		@Override
		public String getColumnName(int column) {
			return titles.get(column);
		}

	}
}
