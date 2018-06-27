package com.framework.swing.table.cell;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.framework.swing.components.DatePicker;
import com.framework.swing.ui.Dimension;
import com.framework.util.DateUtil;
import com.framework.util.StringUtil;

public class DatePickerRender extends JPanel implements TableCellRenderer {
	private JTextField text = new JTextField();
	private String dateFormat = "dd/MM/yyyy";

	public DatePickerRender(DatePicker datePicker) {
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
		if (!StringUtil.isEmpty(value)) {
			try {
				text.setText(DateUtil.toString((Date) value, dateFormat));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this;
	}
}
