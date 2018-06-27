package com.framework.swing.table.cell;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;


public class ComboBoxCellEditor extends DefaultCellEditor {
	public ComboBoxCellEditor(final CellComboBox comboBox, boolean dynamicData, String rendererData) {
		super(comboBox, rendererData, dynamicData);
		comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
		delegate = new EditorDelegate() {
			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}

			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}

			public boolean shouldSelectCell(EventObject anEvent) {
				if (anEvent instanceof MouseEvent) {
					MouseEvent e = (MouseEvent) anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}

			public boolean stopCellEditing() {
				if (comboBox.isEditable()) {
					// Commit edited value.
					comboBox.actionPerformed(new ActionEvent(this, 0, ""));
				}
				return super.stopCellEditing();
			}
		};
		comboBox.addActionListener(delegate);
		setClickCountToStart(2);
	}

	@Override
	public void setClickCountToStart(int count) {
		super.setClickCountToStart(count);
	}

}
