package com.framework.swing.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.framework.BeanLocator;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.ComboBox;
import com.framework.swing.components.Panel;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 23 Apr 2011
 */
public class PanelPage extends Panel implements ActionListener, ItemListener, Page {
	private static final Logger LOG = Logger.getLogger(PanelPage.class);
	private Map<String, Object> pageProps = new HashMap();
	private Map<String, String> propertyMap = new HashMap();
	private MainFrame mainFrame;
	private long pageId;

	public PanelPage(Map<String, Object> data) {
		pageProps.putAll(data);
	}
	
	public PanelPage(){
		
	}

	/**
	 * Read page properties and invoke action class init method to initiliased.
	 * Render all page components. Map of data will be initialize the action
	 * object
	 */
	public void init(Node page) throws Exception {
		if (page != null) {
			pageProps.put("pageName", XML.getAttribute(page, "name"));
			pageProps.put("ExternalAtts", XML.getAttributeBoolean(page, "external_atts", false));
			setLayout(page);
			List<Node> childs = XML.getChilds(page);
			if (childs != null) {
				Map expData = new HashMap();
				for (Node child : childs) {
					Object obj = BeanLocator.getInstance().getBean(child.getNodeName());
					if (obj != null) {
						ReflectionInvoker.invokeMethod(obj, "init", new Object[] { child, this, expData });
						addComponent((JComponent) obj, child);
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EventController pe = new EventController();
		pageProps.put("PropertyMap", propertyMap);
		pe.processAction(this, e.getActionCommand(), e.getSource());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		EventController pe = new EventController();
		pageProps.put("PropertyMap", propertyMap);
		String action = null;
		if (e.getSource() instanceof ComboBox) {
			action = ((ComboBox) e.getSource()).getItemAction();
		}
		pe.processAction(this, action, e.getSource());

	}

	public Object getPageProperty(String property) {
		return pageProps.get(property);
	}

	public void addPageProperty(String key, String value) {
		pageProps.put(key, value);
	}

	public void addProperty(String property, String type) {
		this.propertyMap.put(property, type);
	}

	public Map getPropertMap() {
		return propertyMap;
	}

	@Override
	public void setVisible(boolean aFlag) {
		super.setVisible(aFlag);
	}

	public Map<String, Object> getPageProps() {
		return pageProps;
	}

	public void setPageProps(Map<String, Object> pageProps) {
		this.pageProps = pageProps;
	}

	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}

}
