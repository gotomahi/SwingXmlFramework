package com.framework.swing.components;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.w3c.dom.Node;

import com.ResourceLoader;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 29 Apr 2011
 */
public class PopupMenu extends JPopupMenu {
	Panel panel;

	public PopupMenu() {
	}

	public PopupMenu(Panel panel, Node popupMenu, Object obj) {
		try {
			this.panel = panel;
			init(popupMenu, obj);
			this.addPopupMenuListener(new PopupPrintListener());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init(Node popupMenu, Object obj) throws Exception {
		List<Node> menuItemNodes = XML.getChildNodes(popupMenu, "menu-item");
		for (Node menuItemNode : menuItemNodes) {
			Node subMenu = XML.getChildNode(menuItemNode, "popup_menu");
			if (subMenu != null) {
				init(subMenu, obj);
			} else {
				String name = XML.getAttribute(menuItemNode, "name");
				String path = XML.getAttribute(menuItemNode, "image", null);
				SMenuItem menuItem = null;
				if (path != null)
					menuItem = new SMenuItem(name, new javax.swing.ImageIcon(ResourceLoader.getInstance().getResource(
							path)));
				else
					menuItem = new SMenuItem(name);
				menuItem.setActionCommand(XML.getAttribute(menuItemNode, "action_command"));
				menuItem.setRecordName(XML.getAttribute(menuItemNode, "record_name"));
				menuItem.setHorizontalTextPosition(JMenuItem.RIGHT);
				menuItem.addActionListener((ActionListener) obj);
				this.add(menuItem);
			}
		}
	}

	class PopupPrintListener implements PopupMenuListener {
		public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		}

		public void popupMenuCanceled(PopupMenuEvent e) {
		}
	}
}
