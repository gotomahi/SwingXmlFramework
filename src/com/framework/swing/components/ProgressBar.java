package com.framework.swing.components;

import javax.swing.JProgressBar;

import org.w3c.dom.Node;

import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.xml.XML;

public class ProgressBar extends JProgressBar {

	public void init(Node progressBar, Object curPage)throws Exception{
		setIndeterminate(false);
		setName(XML.getAttribute(progressBar, GUIConstants.NAME));
		setSize(Dimension.getDimension(XML.getAttribute(progressBar, "size", "100,20")));
	}
}
