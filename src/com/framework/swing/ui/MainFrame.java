package com.framework.swing.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JProgressBar;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.framework.swing.components.Frame;
import com.framework.swing.components.MenuBar;
import com.framework.swing.components.Panel;
import com.framework.swing.components.SMenuItem;
import com.framework.swing.components.ToolBar;
import com.framework.util.ExpressionUtil;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 27 Apr 2011
 */
public class MainFrame extends Frame {
	private static final Logger LOG = Logger.getLogger(MainFrame.class);
	private Map<String, List<PageInfo>> moduleMap = new HashMap();
	private CardLayout cardLayout = new CardLayout();
	private Panel content = new Panel();
	private List framePanels = new ArrayList();
	private boolean start;
	private MenuBar menuBar;

	/**
	 * 
	 * @param frame
	 * @throws Exception
	 */
	public void init(Node frame) throws Exception {
		this.setLayout(new BorderLayout());
		setTitle(XML.getAttribute(frame, GUIConstants.TITLE));
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setMinimumSize(new Dimension(XML.getAttributeInteger(frame,
				GUIConstants.WIDTH), XML.getAttributeInteger(frame,
				GUIConstants.HEIGHT)));
		start = XML.getAttributeBoolean(frame, GUIConstants.START, false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setName(XML.getAttribute(frame, GUIConstants.NAME));		

		List<Node> childs = XML.getChilds(frame);
		for (Node child : childs) {
			if ("module".equalsIgnoreCase(child.getNodeName())) {
				Node moduleScreens = XML.fileToNode(XML.getAttribute(child,
						"path"));
				List<Node> screens = XML.getChildNodes(moduleScreens, "screen");
				List<PageInfo> pageList = new ArrayList<PageInfo>();
				for (Node screen : screens) {
					PageInfo pageInfo = new PageInfo();
					pageInfo.setFilePath(XML.getAttribute(screen,
							GUIConstants.FILE));
					pageInfo.setName(XML
							.getAttribute(screen, GUIConstants.NAME));
					pageInfo.setDefaultPage(XML
							.getAttributeBoolean(screen, GUIConstants.DEFAULT));
					pageList.add(pageInfo);
				}
				moduleMap.put(XML.getAttribute(child, GUIConstants.NAME),
						pageList);
			}else if ("ToolBar".equalsIgnoreCase(child.getNodeName())) {
				ToolBar toolBar = new ToolBar();
				toolBar.init(child, this);
				this.getContentPane().add(toolBar, XML.getAttribute(child, GUIConstants.POSITION));
			}else if ("Menu".equalsIgnoreCase(child.getNodeName())) {
				menuBar = new MenuBar();
				menuBar.init(child, this);
				setJMenuBar(menuBar);
			}
		}
		
		content.setLayout(cardLayout);		
		this.getContentPane().add(content, BorderLayout.CENTER);
		this.addWindowListener(this);
	}

	public Page getPage(String pageName, boolean defaultPage, Map<String, Object> data) {
		for (String key : moduleMap.keySet()) {
			for (PageInfo pageInfo : moduleMap.get(key)) {
				if (defaultPage || pageName.equals(pageInfo.getName())) {
					if (pageInfo.getPage() == null) {
						pageInfo.setPage(createPage(pageInfo, data));
					}
					return pageInfo.getPage();
				}
			}
		}
		return null;
	}
	
	

	/**
	 * Creates a page with xml input
	 * 
	 * @param pageNode
	 * @return
	 */
	public Page createPage(PageInfo pageInfo, Map<String, Object> data) {
		Page page = null;
		try {
			// Menu is visible after login. menu items will be displayed based
			// on the user roles
			Map exprData = new HashMap();
			exprData.put(GUIConstants.CLIENT_ID, SwingConfig.getSwingConfig()
					.get(GUIConstants.CLIENT_ID));
			String filePath = ExpressionUtil.evaluateExp(
					pageInfo.getFilePath(), exprData);
			Node pageNode = XML.fileToNode(filePath);
			String menuVisible = XML.getAttribute(pageNode,
					GUIConstants.MENU_VISIBLE);
			if (!StringUtil.isEmpty(menuVisible)) {
				String role = (String) SwingConfig.getSwingConfig().get(
						GUIConstants.ROLE);
				if (!StringUtil.isEmpty(role))
					menuBar.enableMenuItems(role);
				menuBar.setVisible(Boolean.parseBoolean(menuVisible));
			}
			// Create page or dialoge page based on the page dialoge property
			boolean dialog = XML.getAttributeBoolean(pageNode,
					GUIConstants.DIALOG, false);
			if (dialog) {
				DialogPage dialogPage = new DialogPage(this, pageNode, data);
				dialogPage.init(pageNode);
				page = dialogPage;
			} else {
				PanelPage panelPage = new PanelPage(data);
				panelPage.init(pageNode);
				page = panelPage;
			}
			page.setMainFrame(this);
		} catch (Exception e) {
			LOG.error("", e);
		}
		return page;
	}

	public boolean isStartFrame() {
		return start;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof SMenuItem) {
			SMenuItem menuItem = (SMenuItem) e.getSource();
			Map<String, Object> data = new HashMap<String, Object>();
			if (StringUtil.isNotEmpty(menuItem.getActionService())) {
				EventController ec = new EventController();
				ec.processPlugin(menuItem.getActionService(), data);
			}
			SwingConfig.getSwingConfig().renderPage(
					menuItem.getActionCommand(), data);
		}
	}

	/**
	 * 
	 * @param panelName
	 * @param panel
	 */
	public void showComponent(String panelName, Page page) {
		if (page instanceof DialogPage) {
			DialogPage dp = (DialogPage) page;
		} else {
			PanelPage panelPage = (PanelPage) page;
			this.getRootPane().setDefaultButton(
					CompDataHelper.getPageDefButton(panelPage));
			if (!framePanels.contains(panelName)) {
				content.add(panelName, (PanelPage) page);
				framePanels.add(panelName);
			}
			cardLayout.show(content, panelName);
		}
	}

	public void startProgressBar() {
		ToolBar footer = getFooter();
		for (Component comp : footer.getComponents()) {
			if (comp instanceof JProgressBar) {
				JProgressBar pbar = (JProgressBar) comp;
				pbar.setIndeterminate(true);
			}
		}
	}

	public void stopProgressBar() {
		ToolBar footer = getFooter();
		for (Component comp : footer.getComponents()) {
			if (comp instanceof JProgressBar) {
				JProgressBar pbar = (JProgressBar) comp;
				pbar.setIndeterminate(false);
			}
		}
	}
	
	private ToolBar getFooter(){
		ToolBar footer = null;
		for(Component comp : getComponents()){
			if(comp instanceof ToolBar){
				footer = (ToolBar)comp;
			}
		}
		return footer;
	}
}
