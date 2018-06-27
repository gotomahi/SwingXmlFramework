package com.framework.swing.components;

import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.framework.swing.table.cell.CellComboBox;
import com.framework.swing.table.cell.CheckBoxCellEditor;
import com.framework.swing.table.cell.CheckBoxRenderer;
import com.framework.swing.table.cell.ComboBoxCellEditor;
import com.framework.swing.table.cell.ComboBoxRenderer;
import com.framework.swing.table.cell.DatePickerCellEditor;
import com.framework.swing.table.cell.DatePickerRender;
import com.framework.swing.table.cell.NumericCellEditor;
import com.framework.swing.table.cell.NumericRenderer;
import com.framework.swing.table.cell.TableCellPanel;

/**
 * 
 * @author mahendra
 * 
 * @date 26 May 2011
 */
public class RendererFactory {
	private final static RendererFactory rendFact = new RendererFactory();
	private final static Logger LOG = Logger.getLogger(RendererFactory.class);

	private RendererFactory() {

	}

	public static RendererFactory getFactory() {
		return rendFact;
	}

	public ComboBoxRenderer getCellComboboxRenderer(Vector items, String labelProperty, String property,
			boolean dynamicData, String rendererData) {
		ComboBoxRenderer comboRenderer = new ComboBoxRenderer(items, labelProperty, property, dynamicData, rendererData);
		return comboRenderer;
	}

	public ComboBoxCellEditor getComboboxCellEditor(CellComboBox comboBox, boolean dynamicData, String rendererData) {
		ComboBoxCellEditor editor = new ComboBoxCellEditor(comboBox, dynamicData, rendererData);
		return editor;
	}

	public CheckBoxCellEditor getCheckBoxCellEditor(JCheckBox checkBox) {
		return new CheckBoxCellEditor(checkBox);
	}

	public CheckBoxRenderer getCellCheckBoxRenderer() {
		return new CheckBoxRenderer();
	}

	public DatePickerRender getDatePickerRender(DatePicker datePicker) {
		return new DatePickerRender(datePicker);
	}

	public DatePickerCellEditor getDatePickerCellEditor(DatePicker datePicker) {
		return new DatePickerCellEditor(datePicker);
	}

	public NumericRenderer getNumericRenderer() {
		return new NumericRenderer();
	}

	public NumericCellEditor getNumericCellEditor(JTextField textField) {
		return new NumericCellEditor(textField);
	}

}
