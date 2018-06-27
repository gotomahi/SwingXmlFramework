package com.framework.swing.components;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.w3c.dom.Node;

import com.framework.MenuItem;
import com.framework.MenuManager;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 2 May 2011
 */
public class MenuBar extends JMenuBar {
	public MenuBar() {

	}

	public List<MenuItem> init(Node menuNode, Object obj) throws Exception {
		MenuManager.getInstance().init(XML.getAttribute(menuNode, "file"));
		List<MenuItem> menu = MenuManager.getInstance().getTopMenu();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		for (MenuItem menuItem : menu) {
			if (menuItem.getSubMenuItem() != null && !menuItem.getSubMenuItem().isEmpty()) {
				this.add(createMenu(menuItem.getSubMenuItem(), menuItem, (ActionListener) obj));
			} else {				
				this.add(getSMenuItem(menuItem, obj));
			}
		}
		return menu;
	}

	/**
	 * 
	 * @param menuMap
	 * @param menuName
	 * @param al
	 * @return
	 */
	private JMenu createMenu(List<MenuItem> subMenu, MenuItem mainMenuItem, ActionListener al) {
		JMenu menu = new JMenu(mainMenuItem.getItemName());
		menu.setName(mainMenuItem.getRole());
		for (MenuItem menuItem : subMenu) {
			if (menuItem.getSubMenuItem() != null && !menuItem.getSubMenuItem().isEmpty()) {
				menu.add(createMenu(menuItem.getSubMenuItem(), menuItem, al));
			} else {				
				menu.add(getSMenuItem(menuItem, al));
			}
		}
		return menu;
	}
	
	private SMenuItem getSMenuItem(MenuItem menuItem, Object obj){
		SMenuItem jmenuItem = new SMenuItem(menuItem.getItemName());
		jmenuItem.setName(menuItem.getRole());
		jmenuItem.setToolTipText(menuItem.getItemName());
		jmenuItem.setActionCommand(menuItem.getScreen());
		jmenuItem.addActionListener((ActionListener) obj);
		jmenuItem.setActionService(menuItem.getActionService());
		return jmenuItem;
	}

	public void enableMenuItems(String roles) {
		for (int i = 0; i < this.getMenuCount(); i++) {
			if (this.getMenu(i) != null) {
				this.getMenu(i).setEnabled(hasRole(this.getMenu(i).getName(), roles));
				if (this.getMenu(i).isEnabled()) {
					enableDisableMenu(this.getMenu(i).getPopupMenu().getComponents(), roles);
				}
			}
		}

	}

	private void enableDisableMenu(Component[] comp, String role) {
		if (comp != null && comp.length > 0) {
			for (int i = 0; i < comp.length; i++) {
				if (comp[i] instanceof JMenu) {
					JMenu menu = (JMenu) comp[i];
					menu.setEnabled(hasRole(menu.getName(), role));
					if (menu.isEnabled())
						enableDisableMenu(menu.getPopupMenu().getComponents(), role);
				} else if (comp[i] instanceof JMenuItem) {
					JMenuItem menuItem = (JMenuItem) comp[i];
					menuItem.setEnabled(hasRole(menuItem.getName(), role));
				}
			}
		}
	}

	private boolean hasRole(String menuItemRoles, String userRoles) {
		boolean hasRole = true;
		if (!StringUtil.isEmpty(menuItemRoles)) {
			hasRole = false;
			String[] role = userRoles.split(",");
			for (int i = 0; i < role.length; i++) {
				String[] menuRole = menuItemRoles.split(",");
				for (int j = 0; j < menuRole.length; j++) {
					if (role[i].equalsIgnoreCase(menuRole[j])) {
						hasRole = true;
						break;
					}
				}
			}
		}
		return hasRole;
	}

}
