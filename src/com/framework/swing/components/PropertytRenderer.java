package com.framework.swing.components;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;

import org.w3c.dom.Node;

import com.ResourceLoader;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.action.GeneralAction;
import com.framework.swing.ui.DialogPage;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.swing.ui.RegisterListener;
import com.framework.util.ExpressionUtil;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 12 May 2011
 */
public class PropertytRenderer {
//	private String name;
//
//	public PropertytRenderer() {
//
//	}
//
//	public void addProperty(Panel panel, Node property, Object curPage, Object dataObj, Map exprData) throws Exception {
//		panel.addComponent(getComponent(property, curPage, dataObj, exprData), property);
//	}
//
//	public JComponent getComponent(Node property, Object curPage, Object dataObj, Map exprData) throws Exception {
//		JComponent comp = null;
//		name = XML.getAttribute(property, "name");
//		String type = XML.getAttribute(property, "type");
//		if ("TextField".equalsIgnoreCase(type)) {
//			comp = getTextField(property, dataObj);
//		} else if ("PasswordField".equalsIgnoreCase(type)) {
//			comp = getPasswordField(property, dataObj);
//		} else if ("TextArea".equalsIgnoreCase(type)) {
//			comp = getTextArea(property, dataObj);
//		} else if ("CheckBox".equalsIgnoreCase(type)) {
//			comp = getCheckBox(property, dataObj, curPage);
//		} else if ("Button".equalsIgnoreCase(type)) {
//			comp = getButton(property, curPage);
//		} else if ("Label".equalsIgnoreCase(type)) {
//			comp = getLabel(property, dataObj);
//		} else if ("RadioButton".equalsIgnoreCase(type)) {
//			comp = getRadioButton(property, dataObj, curPage);
//		} else if ("ComboBox".equalsIgnoreCase(type)) {
//			comp = getComboBox(property, dataObj, curPage, exprData);
//		} else if ("Action".equalsIgnoreCase(type)) {
//			comp = getAction(property, curPage);
//		} else if ("ProgressBar".equalsIgnoreCase(type)) {
//			comp = getProgressBar(property);
//		} else if ("DatePicker".equalsIgnoreCase(type)) {
//			comp = getDatePicker(property);
//		} else if ("File".equalsIgnoreCase(type)) {
//			comp = getFile(property);
//		} else if ("EditorPane".equalsIgnoreCase(type)) {
//			comp = getEditorPane(property);
//		}
//		if (!"Label".equalsIgnoreCase(type)) {
//			comp.setName(ExpressionUtil.evaluateExp(name, exprData));
//		}
//		String size = XML.getAttribute(property, "size", null);
//		if (!StringUtil.isEmpty(size))
//			comp.setPreferredSize(Dimension.getDimension(size));
//		return comp;
//	}
//
//	private JComponent getTextArea(Node property, Object dataObj) throws Exception {
//		int rows = XML.getAttributeInteger(property, "rows");
//		int cols = XML.getAttributeInteger(property, "cols");
//		TextArea comp = new TextArea(rows, cols);
//		return comp;
//	}
//
//	private JComponent getTextField(Node property, Object dataObj) throws Exception {
//		TextField comp = new TextField();
//		comp.setValidator(XML.getAttribute(property, GUIConstants.VALIDATORS));
//		comp.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		comp.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		if (XML.getAttributeBoolean(property, "upper")) {
//			comp.addKeyListener(comp);
//		}
//		return comp;
//	}
//
//	private JComponent getPasswordField(Node property, Object dataObj) throws Exception {
//		PasswordField comp = new PasswordField();
//		comp.setValidator(XML.getAttribute(property, GUIConstants.VALIDATORS));
//		return comp;
//	}
//
//	public JComponent getLabel(Node property, Object dataObj) throws Exception {
//		String value = XML.getAttribute(property, "property", null);
//		String label = XML.getAttribute(property, "label", "");
//		Label comp = new Label(ResourceLoader.getInstance().getLabelValue("label." + label, new Locale("EN")));
//		comp.setProperty(value);
//		String border = XML.getAttribute(property, "border");
//		if ("true".equalsIgnoreCase(border)) {
//			comp.setBorder(BorderFactory.createLineBorder(Color.black));
//		}
//		return comp;
//	}
//
//	private JComponent getCheckBox(Node property, Object dataObj, Object curPage) throws Exception {
//		CheckBox comp = new CheckBox();
//
//		comp.addActionListener((ActionListener) curPage);
//		return comp;
//	}
//
//	private JComponent getButton(Node property, Object curPage) throws Exception {
//		Button button = new Button(name);
//		button.setNextPage(XML.getAttribute(property, "next_page"));
//		button.addActionListener((ActionListener) curPage);
//		boolean defaultButton = XML.getAttributeBoolean(property, "default", false);
//		button.setValidate(XML.getAttributeBoolean(property, "validate", false));
//		button.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		button.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		button.setActionCommand(XML.getAttribute(property, "action_command"));
//		if (defaultButton) {
//			button.setDefaultButton(true);
//			if (curPage instanceof DialogPage)
//				((DialogPage) curPage).getRootPane().setDefaultButton(button);
//		}
//		return button;
//	}
//
//	private JComponent getAction(Node property, Object curPage) throws Exception {
//		Button button = (Button) getButton(property, curPage);
//		String image = XML.getAttribute(property, "image");
//		GeneralAction action = new GeneralAction(image, name, curPage);
//		button.setAction(action);
//		button.setActionCommand(name);
//		button.setHorizontalAlignment(JButton.HORIZONTAL);
//		button.setText("");
//		button.setToolTipText(name);
//		button.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		button.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		button.setPreferredSize(Dimension.getDimension(XML.getAttribute(property, "size", "20,20")));
//		return button;
//	}
//
//	private JComponent getRadioButton(Node property, Object dataObj, Object curPage) throws Exception {
//		JRadioButton comp = new JRadioButton();
//		String label = XML.getAttribute(property, "label");
//		String group = XML.getAttribute(property, "group");
//		comp.setText(label);
//		comp.setSelected(false);
//		return comp;
//	}
//
//	private JComponent getComboBox(Node property, Object dataObj, Object curPage, Map exprData) throws Exception {
//		String list = XML.getAttribute(property, "list");
//		Object data = ReflectionInvoker.getProperty(dataObj, ExpressionUtil.evaluateExp(list, exprData), false);
//		Vector dataVect = null;
//		if (data == null) {
//			dataVect = new Vector();
//		} else {
//			dataVect = data instanceof Vector ? (Vector) data : new Vector((ArrayList) data);
//		}
//		String labelProp = XML.getAttribute(property, "labelProperty");
//		String defVal = XML.getAttribute(property, "default_value");
//		ComboBox comboBox = new ComboBox(dataVect, labelProp, defVal);
//		String listeners = XML.getAttribute(property, "listeners");
//		String actionName = XML.getAttribute(property, "actionName");
//		if (!StringUtil.isEmpty(actionName)) {
//			comboBox.setActionCommand(actionName);
//			comboBox.setItemAction(actionName);
//		} else {
//			comboBox.setActionCommand(name);
//			comboBox.setItemAction(name);
//		}
//		comboBox.setDataList(list);
//		RegisterListener.getInstance().registerListeners(listeners, comboBox, curPage);
//		comboBox.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		comboBox.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		return comboBox;
//	}
//
//	public JComponent getProgressBar(Node property) throws Exception {
//		JProgressBar progressBar = new JProgressBar(0, 10);
//		progressBar.setIndeterminate(false);
//		progressBar.setSize(Dimension.getDimension(XML.getAttribute(property, "size", "100,20")));
//		return progressBar;
//	}
//
//	public JComponent getDatePicker(Node property) throws Exception {
//		DatePicker datePicker = new DatePicker();
//		datePicker.setName(name);
//		datePicker.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		datePicker.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		return datePicker;
//	}
//
//	public JFile getFile(Node property) throws Exception {
//		JFile file = new JFile(property);
//		file.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		file.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		return file;
//	}
//
//	public EditorPane getEditorPane(Node property) throws Exception {
//		EditorPane editorPane = new EditorPane(property);
//		editorPane.setEnableExpr(XML.getAttribute(property, "enable_expr"));
//		editorPane.setDisableExpr(XML.getAttribute(property, "disable_expr"));
//		return editorPane;
//	}
}
