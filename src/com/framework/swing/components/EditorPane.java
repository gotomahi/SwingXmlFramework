package com.framework.swing.components;

import java.util.Map;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.w3c.dom.Node;

import com.framework.html.HTMLWrapper;
import com.framework.reflect.ReflectionInvoker;
import com.framework.util.ExpressionUtil;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 17 Mar 2012
 */
public class EditorPane extends JEditorPane implements IComponent, HyperlinkListener {
	private String validator;
	private String enableExpr;
	private String disableExpr;
	private String template;

	public EditorPane() {

	}

	@Override
	public void init(Node node, Object curPage, Map expData) throws Exception {
		this.addHyperlinkListener(this);
		this.setContentType("text/html");
		this.setEditable(XML.getAttributeBoolean(node, "editable"));
		this.template = XML.getAttribute(node, "template");
		setEnableExpr(XML.getAttribute(node, "enable_expr"));
		setDisableExpr(XML.getAttribute(node, "disable_expr"));
		renderData(null, null);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {

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
		if (this.isVisible() && StringUtil.isEmpty(template)) {
			Object compVal = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
			this.setText(compVal != null ? String.valueOf(compVal) : "");
		} else if (this.isVisible() && StringUtil.isNotEmpty(template)) {
			this.setText(HTMLWrapper.getInstance().processContent(template, dataObj));
		}

	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		return null;
	}

	@Override
	public void reset() {

	}

	public String getValidator() {
		return validator;
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

}
