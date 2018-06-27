package com.framework.swing.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFileChooser;

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
 * @date 21 Jan 2012
 */
public class JFile extends Panel implements ActionListener, IComponent {

	private JFileChooser fileChooser = new JFileChooser();
	private TextField text = new TextField();
	private Button button = new Button("Browse");
	private String validator;
	private String enableExpr;
	private String disableExpr;

	public JFile() throws Exception {
		this.setLayout(new GridBagLayout());		
	}
	
	@Override
	public void init(Node property, Object curPage, Map expData)throws Exception{
		setName(XML.getAttribute(property, GUIConstants.NAME));
		Dimension dim1 = Dimension.getDimension(XML.getAttribute(property, "size"));
		Dimension dim2 = Dimension.getDimension(XML.getAttribute(property, "button_size", "10,25"));
		String size = XML.getAttribute(property, "size");
		if(!StringUtil.isEmpty(size))
			this.setPreferredSize(Dimension.getDimension(size));
		text.setMinimumSize(dim1);
		text.setPreferredSize(dim1);
		text.setMaximumSize(dim1);
		button.setMinimumSize(dim2);
		button.setPreferredSize(dim2);
		button.setMaximumSize(dim2);
		this.add(text, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 12, 1, new Insets(0, 0, 0, 0), 0, 0));
		this.add(button, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 12, 1, new Insets(1, 1, 1, 1), 0, 0));
		button.addActionListener(this);
		setEnableExpr(XML.getAttribute(property, "enable_expr"));
		setDisableExpr(XML.getAttribute(property, "disable_expr"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			int retval = fileChooser.showDialog(this, "OK");
			if (JFileChooser.APPROVE_OPTION == retval) {
				text.setText(fileChooser.getSelectedFile().getPath());
			}
		}
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
			this.setEnabled(ExpressionUtil.excuteExpression(disableExpr, dataObj));
			this.repaint();
		} else if (StringUtil.isNotEmpty(enableExpr)) {
			this.setEnabled(ExpressionUtil.excuteExpression(enableExpr, dataObj));
			this.repaint();
		}
		if (this.isEnabled()) {
			Object compVal = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
			if (compVal != null) {
				this.text.setText((String) compVal);
			}
		}
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		return this.text.getText();
	}

	@Override
	public void reset() {
		text.setText("");
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
