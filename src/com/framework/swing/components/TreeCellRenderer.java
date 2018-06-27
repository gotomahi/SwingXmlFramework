package com.framework.swing.components;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ResourceLoader;
import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 20 Feb 2012
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		DefaultTreeNode treeNode = (DefaultTreeNode) value;
		if (value != null && StringUtil.isNotEmpty(treeNode.getImage())) {
			this.setIcon(new ImageIcon(ResourceLoader.getInstance().getResource(treeNode.getImage())));
		}
		return this;
	}
}
