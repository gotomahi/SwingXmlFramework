package com.framework.swing.components;

import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.table.cell.TableColumn;

/**
 * 
 * @author mahendra
 * 
 * @date 28 Apr 2011
 */
public class TableModel extends AbstractTableModel implements TableModelListener {
	private static final Logger LOG = Logger.getLogger(TableModel.class);
	private Vector columns = new Vector();
	private Vector data = new Vector();

	@Override
	public int getColumnCount() {
		return columns.size();
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Object val = null;
		try {
			TableColumn tableColumn = getTableColumn(column);
			val = ReflectionInvoker.getProperty(data.elementAt(row), tableColumn.getProperty());
		} catch (Exception e) {
			LOG.error("", e);
		}
		return val;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return getTableColumn(columnIndex).getHeaderName();
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		Object obj = data.get(0);
		try {
			Class colClass = ReflectionInvoker.getPropertyType(obj, getTableColumn(columnIndex).getProperty());
			if (colClass == null)
				colClass = String.class;
			return colClass;
		} catch (Exception e) {
			LOG.error("", e);
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return getTableColumn(columnIndex).isEditable();
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		TableColumn tableColumn = getTableColumn(columnIndex);
		Object model = data.get(rowIndex);
		try {
			ReflectionInvoker.setProperty(model, tableColumn.getProperty(), value);
		} catch (Exception e) {
			LOG.error("", e);
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		Object obj = e.getSource();
		int row = e.getFirstRow();
	}

	public Object getRow(int row) {
		if (data != null && !data.isEmpty())
			return data.get(row);
		return null;
	}

	private TableColumn getTableColumn(int columnIndex) {
		return (TableColumn) columns.get(columnIndex);
	}

	public String getValidator(int column) {
		TableColumn tableColumn = getTableColumn(column);
		return tableColumn.getValidators();
	}

	public String getColumnProperty(int column) {
		TableColumn tableColumn = getTableColumn(column);
		return tableColumn.getProperty();
	}

	public void addColumn(TableColumn column) {
		this.columns.add(column);
	}

	public Vector getModelData() {
		return data;
	}

	public void setModelData(Vector modelData) {
		this.data = modelData;
	}

	public Vector getColumns() {
		return columns;
	}

}
