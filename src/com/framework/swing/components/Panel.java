package com.framework.swing.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.w3c.dom.Node;

import com.framework.BeanLocator;
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
public class Panel extends JPanel {
	protected String layoutType;

	public Panel() {
		this.setLayout(new GridBagLayout());
		layoutType = GUIConstants.GRID;
	}

	public void setLayout(Node panelNode) throws Exception {
		layoutType = XML.getAttribute(panelNode, GUIConstants.LAYOUT, GUIConstants.GRID);
		if (GUIConstants.GRID.equalsIgnoreCase(layoutType)) {
			this.setLayout(new GridBagLayout());
		} else if (GUIConstants.BORDER.equalsIgnoreCase(layoutType)) {
			this.setLayout(new BorderLayout());
		} else if (GUIConstants.FLOW.equalsIgnoreCase(layoutType)) {
			this.setLayout(new FlowLayout());
		}
	}

	/**
	 * Set the layout manager, Grid layout default if none specified. Panel
	 * height, border color also will sets here.
	 * 
	 * @param panelNode
	 */
	public void init(Node panelNode, Object curPage, Map expData) throws Exception {
		initPanel(panelNode, curPage, expData);
		
	}

	public void initPanel(Node panelNode, Object curPage, Map expData) throws Exception {
		String title = XML.getAttribute(panelNode, GUIConstants.BORDER_LET_TITLE);
		if (!StringUtil.isEmpty(title)) {
			this.setBorder(BorderFactory.createTitledBorder(title));
		}
		setLayout(panelNode);
		int height = XML.getAttributeInteger(panelNode, GUIConstants.HEIGHT, -1);
		if (height != -1)
			this.setSize((int) getSize().getWidth(), height);
		String border = XML.getAttribute(panelNode, GUIConstants.BORDER);
		if (GUIConstants.TRUE.equalsIgnoreCase(border)) {
			this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		}		
		addComponents(this, XML.getChilds(panelNode), curPage, expData);		
	}
	
	public void addComponents(Panel panel, List<Node> childs, Object curPage, Map expData)throws Exception{
		if (childs != null) {
			for (Node child : childs) {
				Object obj = BeanLocator.getInstance().getBean(child.getNodeName());
				if (obj != null) {
					ReflectionInvoker.invokeMethod(obj, "init", new Object[] { child, curPage, expData });
					panel.addComponent((JComponent) obj, child);
				}
			}
		}
	}

	/**
	 * Adds the component based on the panel layout.
	 * 
	 * @param comp
	 * @param node
	 * @throws Exception
	 */
	public void addComponent(JComponent comp, Node node) throws Exception {
		Object constraints = null;
		if (GUIConstants.GRID.equalsIgnoreCase(layoutType)) {
			Dimension dim = null;
			if (XML.getAttribute(node, GUIConstants.POSITION, null) != null) {
				dim = new Dimension();
				dim.setPosition(XML.getAttribute(node, GUIConstants.POSITION, null), true);
			} else {
				dim = new Dimension(XML.getAttribute(node, GUIConstants.DIMENSION, null));
			}
			constraints = new GridBagConstraints(dim.getGridx(), dim.getGridy(), dim.getGridwidth(),
					dim.getGridheight(), dim.getWeightx(), dim.getWeighty(), dim.getAnchor(), dim.getFill(),
					new Insets(dim.getTop(), dim.getLeft(), dim.getBottom(), dim.getRight()), 0, 0);

			this.add(comp, constraints);
		} else if (GUIConstants.BORDER.equalsIgnoreCase(layoutType)) {
			constraints = XML.getAttribute(node, GUIConstants.POSITION);
			this.add(comp, constraints);
		} else if (GUIConstants.FLOW.equalsIgnoreCase(layoutType)) {
			this.add(comp, XML.getAttributeInteger(node, GUIConstants.POSITION, FlowLayout.RIGHT));
		}
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

}
