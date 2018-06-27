package com.framework.swing.table.cell;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * 
 * @author mahendra
 * 
 * @date 1 May 2011
 */
public class ComboCellEditor extends AbstractCellEditor implements TableCellEditor {
	JComponent component = new JComboBox();

	@Override
	public Component getTableCellEditorComponent(JTable table, Object obj, boolean arg2, int arg3, int arg4) {
		
		return null;
	}

	@Override
	public Object getCellEditorValue() {
		return null;
	}

}
