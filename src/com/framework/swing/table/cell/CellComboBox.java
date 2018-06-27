package com.framework.swing.table.cell;

import java.awt.Component;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.MutableComboBoxModel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.log4j.Logger;

import com.framework.reflect.ReflectionInvoker;
import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 20 Aug 2011
 */
public class CellComboBox extends JComboBox {
	private Logger LOG = Logger.getLogger(CellComboBox.class);
	private String defVal;
	private String labelProperty;
	private Vector data;

	public CellComboBox(Vector data, String labelProperty, String defVal) {
		super(data);
		this.data = data;
		this.labelProperty = labelProperty;
		this.defVal = defVal;
		this.setRenderer(new ComboBoxRenderer());
	}

	@Override
	public void setSelectedItem(Object anObject) {
		super.setSelectedItem(anObject);
	}

	public void addAll(Vector data) {
		for (Object obj : data) {
			this.addItem(obj);
		}
	}

	public class ComboBoxModel extends AbstractListModel implements MutableComboBoxModel {
		Object selectedObj;
		Vector data;

		public ComboBoxModel() {

		}

		public ComboBoxModel(Vector data) {
			this.data = data;
		}

		@Override
		public Object getElementAt(int index) {
			if (data != null && !data.isEmpty() && index < getSize() && index > 0) {
				return data.elementAt(index - 1);
			}
			return null;
		}

		@Override
		public int getSize() {
			return defVal != null ? data.size() + 1 : data.size();
		}

		@Override
		public Object getSelectedItem() {
			for (Object obj : data) {
				if (obj.equals(selectedObj))
					return obj;
			}
			return null;
		}

		@Override
		public void setSelectedItem(Object anItem) {
			if (anItem != null && defVal != null && !anItem.equals(selectedObj)) {
				selectedObj = anItem;
				fireContentsChanged(this, -1, -1);
			} else {
				selectedObj = null;
			}
		}

		@Override
		public void addElement(Object object) {
			data.addElement(object);

		}

		@Override
		public void insertElementAt(Object object, int index) {
			data.add(index, object);
		}

		@Override
		public void removeElement(Object object) {
			if (!(defVal != null && defVal.equals(object)))
				data.remove(object);
		}

		@Override
		public void removeElementAt(int index) {
			if (!(defVal != null && index == 0))
				data.remove(index);

		}

		public void removeAll() {
			for (int i = 0; i < data.size(); i++) {
				removeElementAt(i);
			}
		}

	}

	public class ComboBoxRenderer extends BasicComboBoxRenderer {

		ComboBoxRenderer() {
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean hasFocus) {
			try {
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}
				if (value != null) {
					if (!StringUtil.isEmpty(labelProperty)) {
						this.setText((String) ReflectionInvoker.getProperty(value, labelProperty));
					} else {
						this.setText(String.valueOf(value));
					}
				} else {
					this.setText(defVal);
				}
			} catch (Exception e) {
				LOG.error("Unable to get value for property " + labelProperty, e);
			}
			return this;
		}

	}
}
