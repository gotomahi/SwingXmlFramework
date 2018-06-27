package com.framework.swing.components;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JTextField;

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
public class TextField extends JTextField implements IComponent, KeyListener {
	private String validator;
	private String enableExpr;
	private String disableExpr;
	
	@Override
	public void init(Node property, Object curPage, Map expData)throws Exception{
		setName(ExpressionUtil.evaluateExp(XML.getAttribute(property, GUIConstants.NAME), expData));
		setValidator(XML.getAttribute(property, GUIConstants.VALIDATORS));
		setEnableExpr(XML.getAttribute(property, "enable_expr"));
		setDisableExpr(XML.getAttribute(property, "disable_expr"));
		if (XML.getAttributeBoolean(property, "upper")) {
		}
		String size = XML.getAttribute(property, "size");
		if(!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
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
		if (this.isVisible()) {
			Object compVal = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
			this.setText(compVal != null ? String.valueOf(compVal) : "");
		}
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		String data = this.getText();
		if (validate)
			validateComp(errors, data);
		return data;
	}

	@Override
	public void reset() {
		this.setText("");
	}

	private void validateComp(Vector errors, Object data) {
		if (!StringUtil.isEmpty(validator)) {
			List errorList = Validator.validate(this.getName(), validator, data);
			if (errorList != null && !errorList.isEmpty()) {
				errors.addAll(errorList);
				this.setBorder(errors.isEmpty() ? Validator.getErrorBorder() : Validator.getEmptyBorder());
			}

		}
	}

	private void processEnable() {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.setText(this.getText().toUpperCase());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.setText(this.getText().toUpperCase());
	}

	@Override
	public void keyTyped(KeyEvent e) {
		this.setText(this.getText().toUpperCase());
	}
}
