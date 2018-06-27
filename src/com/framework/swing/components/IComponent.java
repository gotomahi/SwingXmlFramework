package com.framework.swing.components;

import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Node;

/**
 * 
 * @author mahendra
 * 
 * @date 4 Feb 2012
 */
public interface IComponent {
	
	void init(Node property, Object curPage, Map expData)throws Exception;
	
	void renderData(Object dataObj, Map exprData) throws Exception;

	Object readData(Vector errors, boolean validate);

	void reset();
}
