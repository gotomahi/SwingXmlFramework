package com.framework.swing.table.cell;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 19 Feb 2012
 */
public class NumericCellEditor extends DefaultCellEditor {
	public NumericCellEditor(final JTextField textField) {
		super(textField);
		delegate = new EditorDelegate() {
			public void setValue(Object value) {
				if (StringUtil.isNotEmpty(value)) {
					textField.setText(String.valueOf(value));
				} else {
					textField.setText("");
				}
			}

			public Object getCellEditorValue() {
				return textField.getText();
			}
		};
	}
}
