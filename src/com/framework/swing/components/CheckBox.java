package com.framework.swing.components;

import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;

import org.w3c.dom.Node;

import com.framework.Validator;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.util.ExpressionUtil;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 22 Apr 2011
 */
public class CheckBox extends JCheckBox implements IComponent {
	private String validator;
	private String enableExpr;
	private String disableExpr;

	public CheckBox() {
	}
	
	@Override
	public void init(Node property, Object curPage, Map expData) throws Exception {
		setName(ExpressionUtil.evaluateExp(XML.getAttribute(property, GUIConstants.NAME), expData));
		String size = XML.getAttribute(property, "size");
		if(!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
		this.addActionListener((ActionListener) curPage);
	}

	public void setValidator(String validator) {
		this.validator = validator;
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

	@Override
	public void renderData(Object dataObj, Map exprData) throws Exception {
		if (StringUtil.isNotEmpty(disableExpr)) {
			this.setVisible(ExpressionUtil.excuteExpression(disableExpr, dataObj));
			this.repaint();
		} else if (StringUtil.isNotEmpty(enableExpr)) {
			this.setVisible(ExpressionUtil.excuteExpression(enableExpr, dataObj));
			this.repaint();
		}
		Object value = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
		if (StringUtil.isNotEmpty(value))
			this.setText((String) value);
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		boolean data = this.isSelected();
		if (validate)
			validateComp(errors, data);
		String value = null;
		if (this.isSelected()) {
			value = this.getText();
		}
		return value;
	}

	@Override
	public void reset() {
		this.setSelected(false);
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
}
