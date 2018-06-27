package com.framework.swing.table.cell;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 19 Feb 2012
 */
public class NumericRenderer extends JTextField implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		if (StringUtil.isNotEmpty(value)) {
			this.setText(String.valueOf(value));
		} else {
			this.setText("");
		}

		return this;
	}
}
