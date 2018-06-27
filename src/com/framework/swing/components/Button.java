package com.framework.swing.components;

import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;

import org.w3c.dom.Node;

import com.framework.swing.ui.DialogPage;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.util.ExpressionUtil;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

public class Button extends JButton implements IComponent {
	private String nextPage;
	private boolean defaultButton;
	private boolean validate;
	private String disableExpr;
	private String enableExpr;

	public Button() {

	}

	public Button(String name) {
		super(name);		
	}
	
	@Override
	public void init(Node property, Object curPage, Map expData) throws Exception {
		setName(XML.getAttribute(property, GUIConstants.NAME));
		setText(XML.getAttribute(property, GUIConstants.NAME));
		setNextPage(XML.getAttribute(property, "next_page"));
		addActionListener((ActionListener) curPage);
		boolean defaultButton = XML.getAttributeBoolean(property, "default", false);
		setValidate(XML.getAttributeBoolean(property, "validate", false));
		setEnableExpr(XML.getAttribute(property, "enable_expr"));
		setDisableExpr(XML.getAttribute(property, "disable_expr"));
		setActionCommand(XML.getAttribute(property, "action_command"));
		String size = XML.getAttribute(property, "size");
		if(!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
		if (defaultButton) {
			setDefaultButton(true);
			if (curPage instanceof DialogPage)
				((DialogPage) curPage).getRootPane().setDefaultButton(this);
		}
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

	public boolean isDefaultButton() {
		return defaultButton;
	}

	public void setDefaultButton(boolean defaultButton) {
		this.defaultButton = defaultButton;
	}

	public String getDisableExpr() {
		return disableExpr;
	}

	public void setDisableExpr(String disableExpr) {
		this.disableExpr = disableExpr;
	}

	public String getEnableExpr() {
		return enableExpr;
	}

	public void setEnableExpr(String enableExpr) {
		this.enableExpr = enableExpr;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
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
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		return null;
	}

	@Override
	public void reset() {
	}

}
