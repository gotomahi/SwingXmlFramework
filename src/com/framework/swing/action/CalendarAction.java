package com.framework.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import com.framework.swing.components.DatePicker;

public class CalendarAction extends Action {
	private DatePicker datePicker;

	public CalendarAction(String image, String name, DatePicker datePicker) throws Exception {
		super(image, name);
		this.datePicker = datePicker;

	}

	@Override
	public void actionPerformed(ActionEvent e) {		
		int x = ((JButton)e.getSource()).getX();
		int y = ((JButton)e.getSource()).getY();
		datePicker.showCalendarDialog(x, y);
	}

}
