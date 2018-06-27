package com.framework.swing.ui;

import java.awt.Component;
import java.util.Map;

import javax.swing.JComponent;

import org.w3c.dom.Node;

/**
 * 
 * @author mahendra
 * 
 * @date 25 Sep 2011
 */
public interface Page {
	void init(Node page) throws Exception;

	void addPageProperty(String key, String value);

	Object getPageProperty(String property);

	void setVisible(boolean visible);

	Map getPropertyMap();

	Component[] getComponents();

	Map getPageProps();

	MainFrame getMainFrame();

	void setMainFrame(MainFrame mainFrame);

	void addComponent(JComponent comp, Node node) throws Exception;

}
