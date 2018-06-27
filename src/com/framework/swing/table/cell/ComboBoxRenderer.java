package com.framework.swing.table.cell;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.Panel;
import com.framework.util.StringUtil;

public class ComboBoxRenderer extends Panel implements TableCellRenderer {
	private String labelProperty;
	private Vector items;
	private JTextField text = new JTextField();
	private boolean dynamic;
	private String rendererData;

	public ComboBoxRenderer(Vector items, String labelProperty, String property, boolean dynamicData,
			String rendererData) {
		this.items = items;
		this.labelProperty = labelProperty;
		this.dynamic = dynamicData;
		this.rendererData = rendererData;
		this.setLayout(new GridBagLayout());
		this.add(text, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		if (value != null) {
			try {
				if (!StringUtil.isEmpty(labelProperty)) {
					Object val = ReflectionInvoker.getProperty(value, labelProperty);
					if (val != null)
						text.setText(String.valueOf(val));
					else
						text.setText("");
				} else {
					text.setText(String.valueOf(value));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			text.setText("");
		}
		return this;
	}
}