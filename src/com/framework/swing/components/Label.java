package com.framework.swing.components;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.w3c.dom.Node;

import com.ResourceLoader;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.ui.Dimension;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 22 Apr 2011
 */
public class Label extends JLabel implements IComponent {
	private String label;
	private String property;

	public Label() {
	}

	public Label(String label) {
		super(label);
		this.label = label;
	}

	public Label(String name, String label) {
		super(name);
		this.label = label;
	}

	@Override
	public void init(Node property, Object curPage, Map expData) throws Exception {
		String value = XML.getAttribute(property, "property", null);
		label = ResourceLoader.getInstance().getLabelValue("label." + XML.getAttribute(property, "label", ""),
				new Locale("EN"));
		setText(label);
		setProperty(value);
		String border = XML.getAttribute(property, "border");
		if ("true".equalsIgnoreCase(border)) {
			setBorder(BorderFactory.createLineBorder(Color.black));
		}
		String size = XML.getAttribute(property, "size");
		if (!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public void renderData(Object dataObj, Map exprData) throws Exception {
		if (StringUtil.isNotEmpty(property)) {
			Object compVal = ReflectionInvoker.getProperty(dataObj, property, false);
			this.setText(compVal != null ? String.valueOf(compVal) : "");
		} else {
			this.setText(label != null ? label : "");
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
