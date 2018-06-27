package com.framework.swing.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.DefaultTreeNode;
import com.framework.swing.components.Panel;
import com.framework.swing.components.Tree;
import com.framework.util.StringUtil;
import com.framework.xml.XML;
import com.l2fprod.common.swing.JTaskPane;

/**
 * 
 * @author mahendra
 * 
 * @date 16 May 2011
 */
public class TreePane extends JSplitPane implements ActionListener, TreeSelectionListener, MouseListener {
	private final Logger LOG = Logger.getLogger(this.getClass());
	private JTaskPane taskPane = new JTaskPane();
	private List<TreeItem> treeItems = new ArrayList<TreeItem>();
	private Panel defaultPanel = new Panel();
	private Panel leftPanel = new Panel();
	public Object treePage;
	private int dividerLocation;
	public String initialPage;

	public TreePane() {
	}

	public void init(Node top, Object curPage, Map expData) throws Exception {
		this.treePage = curPage;
		this.dividerLocation = XML.getAttributeInteger(top, GUIConstants.DIVIDER_LOCATION);		
		Node treeNode = XML.getChildNode(top, GUIConstants.NODE);
		List<Node> treeNodes = new ArrayList<Node>();
		treeNodes.add(treeNode);
		treeItems = getTreeItems(treeNodes);
		this.setLeftComponent(leftPanel);
		addTree(new HashMap());
		setDivider();
	}

	/**
	 * This will add new tree. While adding new tree, check if existing tree's
	 * right component is visible. If so update page data to action. If tree is
	 * loading from search, tree will have initial data for each screen, so
	 * populate all pages/actions with data. Finally add tree id to page.
	 * 
	 * @param data
	 */
	public void addTree(Map data)throws Exception {
		Tree tree = new Tree(getTreeNode(treeItems, data), data);
		tree.addTreeSelectionListener(this);
		tree.addMouseListener(this);
		initializeTree(treeItems, tree);
		tree.setSelectionRow(0);
		DefaultTreeNode treeNode = nodeAtTreePosition(tree, 0);
		PanelPage page = getComponent(treeNode.toString());
		page.setPageId(tree.getTreeId());
		setRightComponent(page);
		//tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);		
		leftPanel.add(tree, new GridBagConstraints(0, 0, 0, 0, 0.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
	}

	private void initializeTree(List<TreeItem> treeItems, Tree tree) {
		for (TreeItem treeItem : treeItems) {
			PanelPage page = (PanelPage) SwingConfig.getSwingConfig().getPage(treeItem.getScreen());
			if (page == null) {
				continue;
			}
			// As new tree is started with no initial data, reset page
			try {
				// CompDataHelper.resetData(page, data, page.getComponents());
				CompDataHelper.renderData(page, tree.getData(), page.getComponents(), false, null);
				TreePath treePath = new TreePath(treeItem.getName());
				tree.setLeadSelectionPath(treePath);
			} catch (Exception e) {

			}
			//this.setRightComponent(page);
			//this.setName(treeItem.getName());
			if (treeItem.getSubTree() != null && !treeItem.getSubTree().isEmpty()) {
				// Initialize sub trees
				initializeTree(treeItem.getSubTree(), tree);
			}
		}
	}

	private List<TreeItem> getTreeItems(List<Node> treeNodes) throws Exception {
		List<TreeItem> treeItems = new ArrayList<TreeItem>();
		for (Node node : treeNodes) {
			TreeItem treeItem = new TreeItem();
			treeItem.setName(XML.getAttribute(node, GUIConstants.NAME));
			treeItem.setScreen(XML.getAttribute(node, GUIConstants.NEXT_PAGE));
			treeItem.setImage(XML.getAttribute(node, GUIConstants.IMAGE));
			treeItem.setDiableExpr(XML.getAttribute(node, GUIConstants.DISABLE_EXPR));
			treeItem.setEnableExpr(XML.getAttribute(node, GUIConstants.ENABLE_EXPR));
			treeItem.setSubTreeData(XML.getAttribute(node, "sub_tree_data"));
			treeItem.setActionService(XML.getAttribute(node,"action_service"));
			treeItem.setPopupMenu(XML.getChildNode(node, "popup_menu"));
			treeItems.add(treeItem);
			List<Node> subTree = XML.getChildNodes(node, GUIConstants.NODE);
			if (subTree != null && !subTree.isEmpty()) {
				treeItem.setSubTree(getTreeItems(subTree));
			}
		}
		return treeItems;
	}

	private DefaultTreeNode getTreeNode(List<TreeItem> treeItems, Map initialData) {
		DefaultTreeNode treeNode = null;
		for (TreeItem treeItem : treeItems) {
			treeNode = new DefaultTreeNode(treeItem.getName(), treeItem.getImage());
			treeNode.preparePopup(null,treeItem.getPopupMenu(),this);
			if(treeItem.getActionService() != null && !"".equals(treeItem.getActionService())){
				EventController ec = new EventController();
				ec.processPlugin(treeItem.getActionService(), initialData);
			}
			if (treeItem.getSubTree() != null && !treeItem.getSubTree().isEmpty()) {
				addSubTree(treeItem.getSubTree(), treeNode, initialData);
				break;
			}
		}
		return treeNode;
	}

	private DefaultTreeNode addSubTree(List<TreeItem> treeItems, DefaultTreeNode tree, Map initialData) {
		for (TreeItem treeItem : treeItems) {
			// TODO Dont display tree item if disabled or not enabled
			if (treeItem.getSubTree() != null && !treeItem.getSubTree().isEmpty()) {
				if (StringUtil.isNotEmpty(treeItem.getSubTreeData()) && initialData != null) {
					List subTreeData = (List) ReflectionInvoker.getProperty(initialData, treeItem.getSubTreeData(),
							false);
					if (subTreeData != null) {
						for (int i = 0; i < subTreeData.size(); i++) {
							Object object = subTreeData.get(i);
							Object value = ReflectionInvoker.getProperty(object, treeItem.getName(), false);
							DefaultTreeNode subTree = new DefaultTreeNode(String.valueOf(value));
							if(treeItem.getActionService() != null && !"".equals(treeItem.getActionService())){
								EventController ec = new EventController();
								ec.processPlugin(treeItem.getActionService(), initialData);
							}
							addSubTree(treeItem.getSubTree(), subTree, initialData);
							subTree.preparePopup(null,treeItem.getPopupMenu(),this);
							tree.add(subTree);
						}
					}
				} else {
					DefaultTreeNode subTree = new DefaultTreeNode(treeItem.getName(), treeItem.getImage());
					if(treeItem.getActionService() != null && !"".equals(treeItem.getActionService())){
						EventController ec = new EventController();
						ec.processPlugin(treeItem.getActionService(), initialData);
					}
					addSubTree(treeItem.getSubTree(), subTree, initialData);
					subTree.preparePopup(null,treeItem.getPopupMenu(), this);
					tree.add(subTree);
				}
			} else {
				DefaultTreeNode defaultTreeNode = new DefaultTreeNode(treeItem.getName(), treeItem.getImage());
				defaultTreeNode.preparePopup(null,treeItem.getPopupMenu(), this);
				if(treeItem.getActionService() != null && !"".equals(treeItem.getActionService())){
					EventController ec = new EventController();
					ec.processPlugin(treeItem.getActionService(), initialData);
				}
				tree.add(defaultTreeNode);
			}
		}
		return tree;
	}

	/**
	 * Get page name for selected node.
	 * 
	 * @param map
	 * @param nodeName
	 * @return
	 */
	private String getScreen(List<TreeItem> treeItems, String nodeName) {
		for (TreeItem treeItem : treeItems) {
			if (nodeName.equals(treeItem.getName())) {
				// top tree will map of child nodes
				return treeItem.getScreen();
			} else if (treeItem.getSubTree() != null && !treeItem.getSubTree().isEmpty()) {
				// top tree will map of child nodes
				return getScreen(treeItem.getSubTree(), nodeName);
			}
		}
		return null;
	}

	public void removeGroup(TaskPaneGroup group) {
		taskPane.remove(group);
		this.setRightComponent(defaultPanel);
		this.setDividerLocation(dividerLocation);
		taskPane.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		try {
			Tree tree = (Tree) e.getSource();
			if (e.getNewLeadSelectionPath() != null) {
				Object[] newPath = e.getNewLeadSelectionPath().getPath();
				DefaultTreeNode treeNode = (DefaultTreeNode) newPath[newPath.length - 1];
				PanelPage page = getComponent(treeNode.toString());
				// Update current page data to the page context before rendering
				// with new node
				EventController eventCon = new EventController();
				eventCon.populatePageContext(tree.getData(), page.getComponents(), new Vector(), true);
				// Render new node page
				CompDataHelper.renderData(page, tree.getData(), page.getComponents(), false, null);
				setRightComponent(page);
				page.setPageId(tree.getTreeId());
				setDivider();
			}
			deselectNodeSelection(tree);
		} catch (Exception ex) {
			LOG.error("", ex);
		}
	}

	/**
	 * Store the page data in tree.
	 * 
	 * @param page
	 * @extractDataFromTreeNodeon
	 */
	void extractDataFromTreeNode(Page page) throws Exception {
		if (page != null && !StringUtil.isEmpty(page.getPageProperty(GUIConstants.TREE_ID))) {
			long treeId = (Long) page.getPageProperty(GUIConstants.TREE_ID);
			for (Component comp : taskPane.getComponents()) {
				TaskPaneGroup group = (TaskPaneGroup) comp;
				if (group.getTree().getTreeId() == treeId) {
					Object actionObj = page.getPageProperty(GUIConstants.ACTION);
					Map map = (Map) ReflectionInvoker.getProperty(actionObj, GUIConstants.TREE_MAP);
					if (map != null) {
						group.getTree().getData().putAll(map);
					}
				}
			}
		}
	}

	/**
	 * Before saving update all tree page data to tree
	 * 
	 * @throws Exception
	 */
	public void updateAllPagesData(TaskPaneGroup group) throws Exception {
		for (TreeItem treeItem : treeItems) {
			PanelPage page = (PanelPage) SwingConfig.getSwingConfig().renderPage(treeItem.getScreen());
			if (page != null && !StringUtil.isEmpty(page.getPageProperty(GUIConstants.TREE_ID))) {
				long treeId = (Long) page.getPageProperty(GUIConstants.TREE_ID);
				if (group.getTree().getTreeId() == treeId) {
					EventController eventCon = new EventController();
					// eventCon.pop(page);
					Object actionObj = page.getPageProperty(GUIConstants.ACTION);
					Map map = (Map) ReflectionInvoker.getProperty(actionObj, GUIConstants.TREE_MAP);
					if (map != null) {
						group.getTree().getData().putAll(map);
					}
				}
			}
		}
		Page tpage = (Page) treePage;
		Object actionObj = tpage.getPageProperty(GUIConstants.ACTION);
		ReflectionInvoker.setProperty(actionObj, GUIConstants.TREE_MAP, group.getTree().getData());
	}

	/**
	 * Deselect the tree node selection
	 * 
	 * @param tree
	 * @throws Exception
	 */
	private void deselectNodeSelection(Tree tree) throws Exception {
		for (Component comp : taskPane.getComponents()) {
			TaskPaneGroup group = (TaskPaneGroup) comp;
			if (group.getTree() != tree) {
				group.getTree().setSelectionPath(null);
			}
		}
	}

	/**
	 * Get the component panel for node
	 * 
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	private PanelPage getComponent(String nodeName) throws Exception {
		String nextPageName = getScreen(treeItems, nodeName);
		PanelPage page = (PanelPage) SwingConfig.getSwingConfig().getPage(nextPageName);
		page.setVisible(true);
		return page;
	}
	
	private DefaultTreeNode nodeAtTreePosition(Tree tree, int row){
		tree.setSelectionRow(row);
		Object[] nodePath = tree.getSelectionPath().getPath();
		DefaultTreeNode treeNode = (DefaultTreeNode) nodePath[nodePath.length - 1];
		return treeNode;
	}
	
	public Map getTreeData(long treeId){
		Panel leftComp = (Panel)this.getLeftComponent();
		for(Component comp : leftComp.getComponents()){
			if(comp instanceof Tree && ((Tree)comp).getTreeId()==treeId){
				return ((Tree)comp).getData();
			}
		}
		return null;
	}
	
	public void setDivider(){
		this.setDividerLocation(dividerLocation);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (SwingUtilities.isRightMouseButton(e)) {
			Tree tree = (Tree)e.getSource();
			int row = tree.getClosestRowForLocation(e.getX(), e.getY());
			DefaultTreeNode treeNode = nodeAtTreePosition(tree, row);
			if(treeNode.getPopupMenu() != null)
			treeNode.getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public Object getTreePage() {
		return this.treePage;
	}

}
