package com.framework.swing.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.MutableComboBoxModel;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.framework.Validator;
import com.framework.config.repositories.ActionConfigRepository.Service;
import com.framework.config.repositories.ActionConfigRepository.Variable;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.swing.ui.Page;
import com.framework.swing.ui.RegisterListener;
import com.framework.util.ExpressionUtil;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 20 Aug 2011
 */
public class ComboBox extends JComboBox implements IComponent {
	private Logger LOG = Logger.getLogger(ComboBox.class);
	private String defVal = "";
	private String labelProperty;
	private Vector data = new Vector();
	private boolean samePage = true;
	private String dataList;
	private ComboBoxModel comboBoxModel;
	private String validator;
	private String itemAction;
	private String enableExpr;
	private String disableExpr;
	private Service service;

	public ComboBox() {
	}

	public ComboBox(Vector items) {
		addAll(items);
	}

	public void setDefVal(String defVal) {
		this.defVal = defVal;
	}

	public void setLabelProperty(String labelProperty) {
		this.labelProperty = labelProperty;
	}

	@Override
	public void init(Node property, Object curPage, Map expData) throws Exception {
		this.labelProperty = XML.getAttribute(property, "labelProperty");
		String name = ExpressionUtil.evaluateExp(XML.getAttribute(property, GUIConstants.NAME), expData);
		setName(name);
		this.defVal = XML.getAttribute(property, "default_value");
		String list = XML.getAttribute(property, "list");
		String size = XML.getAttribute(property, "size");
		if (!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
		Page page = (Page) curPage;
		// TODO sometimes list should be have expression
		Object dataObj = ReflectionInvoker.getProperty(page.getPageProps(), list, false);
		if (dataObj != null) {
			this.data = dataObj instanceof Vector ? (Vector) dataObj : new Vector((ArrayList) dataObj);
		}
		String listeners = XML.getAttribute(property, "listeners");
		String actionName = XML.getAttribute(property, "actionName");
		if (!StringUtil.isEmpty(actionName)) {
			setActionCommand(actionName);
			setItemAction(actionName);
		} else {
			setActionCommand(name);
			setItemAction(name);
		}
		setDataList(list);
		RegisterListener.getInstance().registerListeners(listeners, this, curPage);
		setEnableExpr(XML.getAttribute(property, "enable_expr"));
		setDisableExpr(XML.getAttribute(property, "disable_expr"));
		comboBoxModel = new ComboBoxModel(this.data);
		this.setModel(comboBoxModel);
		this.setRenderer(new ComboBoxRenderer());
		this.setEditor(new ComboBoxEditor());

	}

	public Service getService(Node node) throws Exception {
		Service service = new Service();
		Node serviceNode = XML.getChildNode(node, "service");
		service.setMethod(XML.getAttribute(serviceNode, "method"));
		service.setName(XML.getAttribute(serviceNode, "name"));
		Node argsNode = XML.getChildNode(serviceNode, "arguements");
		List<Node> args = XML.getChildNodes(argsNode, "var");
		List<Variable> vars = new ArrayList<Variable>();
		for (Node arg : args) {
			Variable var = new Variable();
			var.setName(XML.getAttribute(arg, "name"));
			var.setType(XML.getAttribute(arg, "type"));
			var.setScope(XML.getAttribute(arg, "scope"));
			var.setProperty(XML.getAttribute(arg, "property"));
			var.setValue(XML.getAttribute(arg, "value"));
			var.setOut(XML.getAttributeBoolean(arg, "out"));
			vars.add(var);
		}
		service.setVariables(vars);
		return service;
	}

	@Override
	public void setSelectedItem(Object anObject) {
		comboBoxModel.setSelectedItem(anObject);
	}

	public void addAll(Vector items) {
		data = items;
		if (comboBoxModel == null) {
			comboBoxModel = new ComboBoxModel(items);
			this.setModel(comboBoxModel);
		}
		comboBoxModel.setItems(items);
	}

	public void setValidator(String validator) {
		this.validator = validator;
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
			if (data != null && !data.isEmpty() && index < getSize() && index >= 0) {
				return data.elementAt(index > 0? index - 1:index);
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
			if (anItem != null) {
				selectedObj = anItem;
				fireContentsChanged(this, -1, -1);
			} else {
				selectedObj = defVal;
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
				data.remove(i);
			}
		}

		public void setItems(Vector items) {
			data = items;
			if (!items.contains(selectedObj)) {
				// setSelectedItem(defVal);
				selectedObj = defVal;
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
					if (StringUtil.isEmpty(labelProperty)) {
						this.setText(value.toString());
					} else {
						this.setText(String.valueOf(ReflectionInvoker.getProperty(value, labelProperty)));
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

	public class ComboBoxEditor extends BasicComboBoxEditor {

	}

	@Override
	public void renderData(Object dataObj, Map exprData) throws Exception {
		if (StringUtil.isNotEmpty(disableExpr)) {
			this.setEnabled(ExpressionUtil.excuteExpression(disableExpr, dataObj));
			this.repaint();
		} else if (StringUtil.isNotEmpty(enableExpr)) {
			this.setEnabled(ExpressionUtil.excuteExpression(enableExpr, dataObj));
			this.repaint();
		}
		if (this.isEnabled()) {
			Object compVal = ReflectionInvoker.getProperty(dataObj,
					ExpressionUtil.evaluateExp(this.getDataList(), exprData), true);
			if (compVal == null) {
				reset();
			} else {
				this.addAll(compVal instanceof List ? new Vector((List) compVal) : (Vector) compVal);
				Object val = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
				this.setSelectedItem(val);
			}
		}
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		Object data = this.getSelectedItem();
		if (validate)
			validateComp(errors, data);
		return data;
	}

	@Override
	public void reset() {
		this.addAll(new Vector());
	}

	private void validateComp(Vector errors, Object data) {
		if (!StringUtil.isEmpty(validator)) {
			List errorList = Validator.validate(this.getName(), validator, data);
			if (errorList != null && !errorList.isEmpty()) {
				errors.addAll(errorList);
				this.setBorder(Validator.getErrorBorder());
			}

		}
	}

	public boolean isSamePage() {
		return samePage;
	}

	public void setSamePage(boolean samePage) {
		this.samePage = samePage;
	}

	public String getDataList() {
		return dataList;
	}

	public void setDataList(String dataList) {
		this.dataList = dataList;
	}

	public String getItemAction() {
		return itemAction;
	}

	public void setItemAction(String itemAction) {
		this.itemAction = itemAction;
	}

	public String getEnableExpr() {
		return enableExpr;
	}

	public void setEnableExpr(String enableExpr) {
		this.enableExpr = enableExpr;
	}

	public String getDisableExpr() {
		return disableExpr;
	}

	public void setDisableExpr(String disableExpr) {
		this.disableExpr = disableExpr;
	}

}
