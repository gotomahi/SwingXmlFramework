package com.framework.swing.ui;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.framework.BeanLocator;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.Dialog;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 27 Apr 2011
 */
public class DialogPage extends Dialog implements Page {
	private static final Logger LOG = Logger.getLogger(DialogPage.class);
	private Map<String, Object> pageProps = new HashMap();
	private Map<String, String> propertyMap = new HashMap();
	private MainFrame mainFrame;

	public DialogPage(JFrame frame) {
		super(frame);
	}

	public DialogPage(JFrame frame, Node node, Map<String, Object> data) {
		super(frame, node);
		pageProps.putAll(data);
	}

	public void init(Node page) throws Exception {
		setLayout(new GridBagLayout());
		this.setSize(600, 500);
		if (page != null) {
			pageProps.put(GUIConstants.PAGE_NAME, XML.getAttribute(page, GUIConstants.NAME));
			pageProps.put("ExternalAtts", XML.getAttributeBoolean(page, GUIConstants.EXTERNAL_ATTS, false));
			List<Node> childs = XML.getChilds(page);
			if (childs != null) {
				for (Node child : childs) {
					Object obj = BeanLocator.getInstance().getBean(child.getNodeName());
					if (obj != null) {
						ReflectionInvoker.invokeMethod(obj, "init", new Object[] { child, this, new HashMap() });
						addComponent((JComponent) obj, child);
					}
				}
			}
		}		
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		EventController pe = new EventController();
		pageProps.put(GUIConstants.PROPERTY_MAP, propertyMap);
		pe.processAction(this, e.getActionCommand(), e.getSource());
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

	public Map getPropertyMap() {
		return propertyMap;
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
	}

	public Map<String, Object> getPageProps() {
		return pageProps;
	}

	public void setPageProps(Map<String, Object> pageProps) {
		this.pageProps = pageProps;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

}
