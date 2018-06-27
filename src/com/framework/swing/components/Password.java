package com.framework.swing.components;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPasswordField;

import org.w3c.dom.Node;

import com.framework.Validator;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 22 Apr 2011
 */
public class Password extends JPasswordField implements IComponent {
	private String validator;
	
	@Override
	public void init(Node property, Object curPage, Map expData)throws Exception{
		setName(XML.getAttribute(property, GUIConstants.NAME));
		setValidator(XML.getAttribute(property, GUIConstants.VALIDATORS));
		String size = XML.getAttribute(property, "size");
		if(!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
	}

	public String validateComp() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	@Override
	public void renderData(Object dataObj, Map exprData) throws Exception {
		Object compVal = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
		this.setText(compVal != null ? String.valueOf(compVal) : "");
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
				this.setBorder(Validator.getErrorBorder());
			}

		}
	}
}
