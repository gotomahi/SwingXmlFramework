package com.framework.swing.components;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTree;

import org.apache.commons.lang.math.RandomUtils;

/**
 * 
 * @author mahendra
 * 
 * @date 17 Jan 2012
 */
public class Tree extends JTree {

	private long treeId;
	private PopupMenu popupMenu;

	private Map data = new HashMap(1);

	public Tree(DefaultTreeNode treeNode, Map data) {
		super(treeNode);
		if (data != null && !data.isEmpty()) {
			this.data.putAll(data);
		}
		this.treeId = RandomUtils.nextLong();
		MousePopupListener mousePopupListener = new MousePopupListener();
		addMouseListener(mousePopupListener);
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public long getTreeId() {
		return this.treeId;
	}

	public PopupMenu getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(PopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}
	
	class MousePopupListener extends MouseAdapter {

		private void showPopupMenu(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupMenu.show(Tree.this, e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}


}
