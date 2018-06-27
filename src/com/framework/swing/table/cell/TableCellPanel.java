package com.framework.swing.table.cell;

import java.util.Date;

import javax.swing.JPanel;

import com.framework.swing.components.DatePicker;

/**
 * 
 * @author mahendra
 * 
 * @date 29 Nov 2011
 */
public class TableCellPanel extends JPanel {
	private DatePicker datePicker;

	public TableCellPanel(DatePicker datePicker) {
		this.datePicker = datePicker;
		this.add(datePicker);
	}

	public TableCellPanel() {

	}

	public DatePicker getDatePicker() {
		return datePicker;
	}

	public void setDatePicker(Date date) {
		this.datePicker.setDate(date);
	}

}
