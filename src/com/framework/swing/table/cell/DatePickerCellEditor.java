package com.framework.swing.table.cell;

import java.util.Date;

import com.framework.swing.components.DatePicker;

public class DatePickerCellEditor extends DefaultCellEditor {
	public DatePickerCellEditor(final DatePicker datePicker) {
		super(datePicker);
		delegate = new EditorDelegate() {
			public void setValue(Object value) {
				((DatePicker) editorComponent).setDate((Date) value);
			}

			public Object getCellEditorValue() {
				return ((DatePicker) editorComponent).getDate();
			}
		};
	}

}