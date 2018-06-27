package com.framework.swing.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.JButton;

import org.w3c.dom.Node;

import com.framework.swing.components.Button;
import com.framework.swing.ui.DialogPage;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.MainFrame;
import com.framework.swing.ui.PanelPage;
import com.framework.xml.XML;

public class GeneralAction extends Action {
	private Object curPage;

	public GeneralAction(String image, String name, Object curPage) throws Exception {
		super(image, name);
		this.curPage = curPage;

	}

	public GeneralAction(String name, Object curPage) throws Exception {
		super(name);
		this.curPage = curPage;

	}
	
	public void init(Node property, Object curPage) throws Exception {
		Button button =  new Button();
		button.init(property, curPage,new HashMap());
		String image = XML.getAttribute(property, "image");
		String name = XML.getAttribute(property, "name");
		GeneralAction action = new GeneralAction(image, name, curPage);
		button.setAction(action);
		button.setActionCommand(name);
		button.setHorizontalAlignment(JButton.HORIZONTAL);
		button.setText("");
		button.setToolTipText(name);
		button.setEnableExpr(XML.getAttribute(property, "enable_expr"));
		button.setDisableExpr(XML.getAttribute(property, "disable_expr"));
		button.setPreferredSize(Dimension.getDimension(XML.getAttribute(property, "size", "20,20")));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (curPage instanceof PanelPage)
			((PanelPage) curPage).actionPerformed(e);
		else if (curPage instanceof DialogPage)
			((DialogPage) curPage).actionPerformed(e);
		else if (curPage instanceof MainFrame)
			((MainFrame) curPage).actionPerformed(e);
	}
}
