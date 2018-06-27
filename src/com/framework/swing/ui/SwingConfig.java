package com.framework.swing.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import com.framework.BeanLocator;
import com.framework.config.repositories.Repositories;
import com.framework.reflect.ReflectionInvoker;
import com.framework.util.StaticData;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 22 Apr 2011
 */
public class SwingConfig {
	private static final SwingConfig swingConfig = new SwingConfig();
	private Map<String, PageInfo> framePages = new HashMap<String, PageInfo>();
	private Map<String, MainFrame> frameMap = new HashMap<String, MainFrame>();
	private MainFrame mainFrame;
	private Map session = new HashMap();
	private Map<String, String> components = new HashMap<String, String>();

	private SwingConfig() {

	}

	/**
	 * 
	 * @param framePath
	 * @throws Exception
	 */
	public void init(String framePath) throws Exception {
		Themes.getThemes().setTheme(Themes.THEME_NIMBUS);
		StaticData.init();
		Repositories.getInstance().initActionConfig("action-config.xml");
		Node top = XML.fileToNode(framePath);
		List<Node> frames = XML.getChilds(top);
		for (Node frame : frames) {
			PageInfo pageInfo = new PageInfo();
			pageInfo.setFilePath(XML.getAttribute(frame, GUIConstants.FILE));
			pageInfo.setName(XML.getAttribute(frame, GUIConstants.NAME));
			pageInfo.setDefaultPage(XML.getAttributeBoolean(frame,
					GUIConstants.DEFAULT));
			framePages.put(pageInfo.getName(), pageInfo);
		}
		for (String key : framePages.keySet()) {
			PageInfo pageInfo = framePages.get(key);
			if (pageInfo.isDefaultPage()) {
				renderFrame(pageInfo.getName());
			}
		}
	}

	public Page renderFrame(String frameName) {
		PageInfo pageInfo = framePages.get(frameName);
		Page page = pageInfo.getPage();
		if (pageInfo.getPage() == null) {
			mainFrame = new MainFrame();
			try {
				Node frame = XML.fileToNode(pageInfo.getFilePath());
				mainFrame.init(frame);
			} catch (Exception e) {
				e.printStackTrace();
			}
			page = renderPage(null, true, new HashMap());
			frameMap.put(frameName, mainFrame);
		}
		mainFrame.setVisible(true);
		return page;
	}

	public Page renderPage(String pageName) {
		return renderPage(pageName, new HashMap());
	}

	public Page renderPage(String pageName, Map<String, Object> data) {
		return renderPage(pageName, false, data);
	}

	private Page renderPage(String pageName, boolean defaultPage,
			Map<String, Object> data) {
		Page page = null;
		if (framePages.get(pageName) == null) {
			page = mainFrame.getPage(pageName, defaultPage, data);
			if (page != null) {
				mainFrame.showComponent(pageName, page);
				page.setVisible(true);
				mainFrame.setVisible(true);
			}
		} else {
			page = renderFrame(pageName);
		}
		return page;
	}

	public static void processChildComponents(Node comp) {
		try {
			List<Node> childs = XML.getChilds(comp);
			if (childs != null) {
				for (Node child : childs) {
					Object obj = BeanLocator.getInstance().getBean(
							child.getNodeName());
					if (obj != null) {
						ReflectionInvoker.invokeMethod(obj, "init",
								new Object[] { child });
					}
				}
			}
		} catch (Exception e) {

		}
	}

	public Page getPage(String pageName) {
		return mainFrame.getPage(pageName, false, new HashMap());
	}

	public void add(Object key, Object value) {
		session.put(key, value);
	}

	public Object get(Object key) {
		return session.get(key);
	}

	public Map getSession() {
		return session;
	}

	public Map getFrameMap() {
		return framePages;
	}

	public static SwingConfig getSwingConfig() {
		return swingConfig;
	}
}
