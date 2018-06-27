package com.framework.swing.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import com.framework.swing.action.TreeAction;
import com.framework.swing.components.Button;
import com.framework.swing.components.Panel;
import com.framework.swing.components.Tree;
import com.l2fprod.common.swing.JTaskPaneGroup;

/**
 * 
 * @author mahendra
 * 
 * @date 21 May 2011
 */
public class TaskPaneGroup extends JTaskPaneGroup implements ActionListener {
	private Panel panel = new Panel();
	private Tree tree;

	public TaskPaneGroup() {
	}

	public void addAction(List<Map<String, Object>> treeActions, TreePane treePane) throws Exception {
		panel.setLayout(new FlowLayout());
		for (Map<String, Object> actionMap : treeActions) {
			String image = (String) actionMap.get(GUIConstants.IMAGE);
			String name = (String) actionMap.get(GUIConstants.NAME);
			String nextPage = (String) actionMap.get(GUIConstants.NEXT_PAGE);
			TreeAction action = new TreeAction(image, name, treePane, this);
			Button button = new Button();
			button.setPreferredSize(new Dimension(20, 20));
			button.setAction(action);
			button.setActionCommand(name);
			button.setNextPage(nextPage);
			button.setHorizontalAlignment(JButton.HORIZONTAL);
			button.setHorizontalTextPosition(JButton.HORIZONTAL);
			button.setToolTipText(name);
			button.setEnableExpr((String) actionMap.get(GUIConstants.ENABLE_EXPR));
			button.setDisableExpr((String) actionMap.get(GUIConstants.DISABLE_EXPR));
			button.renderData(tree.getData(), null);
			panel.add(button);
		}
		this.add(panel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public Component add(Tree tree) {
		this.tree = tree;
		this.tree.setCellRenderer(new com.framework.swing.components.TreeCellRenderer());
		return super.add(tree);
	}

	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

}
