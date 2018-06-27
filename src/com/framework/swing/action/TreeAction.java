package com.framework.swing.action;

import java.awt.event.ActionEvent;
import java.util.Map;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.ui.EventController;
import com.framework.swing.ui.GUIConstants;
import com.framework.swing.ui.Page;
import com.framework.swing.ui.SwingConfig;
import com.framework.swing.ui.TaskPaneGroup;
import com.framework.swing.ui.TreePane;
import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 11 Mar 2012
 */
public class TreeAction extends Action {
	private TreePane treePane;
	private TaskPaneGroup group;

	public TreeAction(String image, String name, TreePane treePane, TaskPaneGroup group) throws Exception {
		super(image, GUIConstants.EMPTY_STR);
		this.treePane = treePane;
		this.group = group;
	}

	public TreeAction(String image, String name, TreePane treePane) throws Exception {
		super(image, GUIConstants.EMPTY_STR);
		this.treePane = treePane;
	}

	public TreeAction(String name, TreePane treePane) throws Exception {
		super(name);
		this.treePane = treePane;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Page page = (Page) treePane.treePage;
		if (GUIConstants.ADD_TREE.equals(e.getActionCommand())) {
			if (StringUtil.isNotEmpty(treePane.initialPage)) {				
				try {
					Map map = (Map)ReflectionInvoker.getProperty(page.getPageProperty(GUIConstants.ACTION),
							GUIConstants.TREE_MAP);
					SwingConfig.getSwingConfig().renderPage(treePane.initialPage);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				try{
				treePane.addTree(null);
				}catch(Exception ex){}
				treePane.repaint();
			}
		} else if (GUIConstants.CLOSE.equals(e.getActionCommand())) {
			treePane.removeGroup(group);
		} else {
			try {
				treePane.updateAllPagesData(group);
				page.getPageProps().put("data", group.getTree().getData());
				EventController eventController = new EventController();
				eventController.processAction(treePane.getTreePage(), e.getActionCommand(), e.getSource());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
