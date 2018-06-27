package com.framework.swing.table.cell;

import javax.swing.JCheckBox;


public class CheckBoxCellEditor extends DefaultCellEditor {
	public CheckBoxCellEditor(final JCheckBox checkBox) {
		super(checkBox);
		delegate = new EditorDelegate() {
			public void setValue(Object value) {
				boolean selected = false;
				if (value instanceof Boolean) {
					selected = ((Boolean) value).booleanValue();
				} else if (value instanceof String) {
					selected = value.equals("true");
				}
				checkBox.setSelected(selected);
			}

			public Object getCellEditorValue() {
				return Boolean.valueOf(checkBox.isSelected());
			}
		};
		checkBox.addActionListener(delegate);
		checkBox.setRequestFocusEnabled(false);
	}
}
