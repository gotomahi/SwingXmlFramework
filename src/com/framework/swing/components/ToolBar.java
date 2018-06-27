package com.framework.swing.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.MainFrame;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 13 Nov 2011
 */
public class ToolBar extends JToolBar {
	private static final Logger LOG = Logger.getLogger(ToolBar.class);

	public ToolBar() {

	}

	public void init(Node toolBarNode, Object curPage) throws Exception {
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setSize((int) getSize().getWidth(), 25);
		
		List<Node> childs = XML.getChilds(toolBarNode);
		this.setLayout(new GridBagLayout());
		int i = 0;
		for (Node prop : childs) {
			if (i > 0)
				this.addSeparator();
			Dimension dim = new Dimension(XML.getAttribute(prop, "dimension"));
			GridBagConstraints constraints = new GridBagConstraints(dim.getGridx(), dim.getGridy(), dim.getGridwidth(),
					dim.getGridheight(), dim.getWeightx(), dim.getWeighty(), dim.getAnchor(), dim.getFill(),
					new Insets(dim.getTop(), dim.getLeft(), dim.getBottom(), dim.getRight()), 0, 0);

			i++;
		}
	}

}
