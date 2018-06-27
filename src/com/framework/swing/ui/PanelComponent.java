package com.framework.swing.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Node;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.Panel;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 24 Sep 2011
 */
public class PanelComponent extends Panel {
	//TODO need to remove xml node
	private Node compNode;
	private String list;
	private Dimension dim;

	public PanelComponent() {

	}

	/**
	 * 
	 * @param node
	 * @param curPage
	 * @param dataObj
	 * @throws Exception
	 */
	public void init(Node node, Object curPage, Map expData) throws Exception {
		compNode = node;
		list = XML.getAttribute(node, "list");
		dim = new Dimension(XML.getAttribute(node, "dimension"));		
		addPanelComponents(curPage, ((Page)curPage).getPageProps(),expData);
	}

	/**
	 * 
	 * @param curPage
	 * @param dataObj
	 * @throws Exception
	 */
	public void addPanelComponents(Object curPage, Object dataObj, Map expData) throws Exception {
		Object data = ReflectionInvoker.getProperty(dataObj, list, true);
		removeComps(curPage, dataObj);
		if (data instanceof Map) {
			Map dataMap = (Map) data;
			Iterator itr = dataMap.keySet().iterator();
			for (int i = 0; itr.hasNext();) {
				Object key = itr.next();
				expData.put("productType", key);
				PanelComponent comp = existPanelComponent((String) key);
				if (comp == null) {
					comp = new PanelComponent(); 
					comp.setName((String) key);
					addComponents(comp, XML.getChilds(compNode), curPage, expData);
					GridBagConstraints constraint = new GridBagConstraints(0, i++, 1, 1, 1.0, 0.0,
							GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(dim.getTop(),
									dim.getLeft(), dim.getBottom(), dim.getRight()), 0, 0);
					this.add(comp, constraint);
				}

				Component[] panelComps = { comp };
				CompDataHelper.renderData(curPage, dataObj, panelComps, true, expData);
			}
		}
	}

	/**
	 * Removes the panel component if one doesn't exists in the data list.
	 * otherwise update panel component
	 * 
	 * @param curPage
	 * @param dataObj
	 * @throws Exception
	 */
	public void removeComps(Object curPage, Object dataObj) throws Exception {
		if (dataObj instanceof Map) {
			Map data = (Map) dataObj;
			for (Component comp : this.getComponents()) {
				boolean found = false;
				for (Object obj : data.keySet()) {
					if (comp.getName().equals((String) obj)) {
						found = true;
					}
				}
				if (!found) {
					this.remove(comp);
				}
			}
		}
	}

	private PanelComponent existPanelComponent(String key) {
		PanelComponent c = null;
		for (Component comp : this.getComponents()) {
			if (!StringUtil.isEmpty(key) && comp.getName().equals(key)) {
				c = (PanelComponent)comp;
				break;
			}
		}
		return c;
	}

	public Node getCompNode() {
		return compNode;
	}

	public void setCompNode(Node compNode) {
		this.compNode = compNode;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

}
