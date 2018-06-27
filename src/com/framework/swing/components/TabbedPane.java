package com.framework.swing.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;

import org.w3c.dom.Node;

import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.Page;
import com.framework.swing.ui.PanelPage;
import com.framework.swing.ui.SwingConfig;
import com.framework.xml.XML;

public class TabbedPane extends JTabbedPane {

	public void init(Node tabbedPane, Object curPage)throws Exception{
		JTabbedPane tabPane = new JTabbedPane();
		List<Node> tabs = XML.getChildNodes(tabbedPane, "tab");
		for (Node tab : tabs) {
			String tabScreen = XML.getAttribute(tab, "screen");
			PanelPage panelPage = (PanelPage) SwingConfig.getSwingConfig().renderPage(tabScreen);
			tabPane.addTab(XML.getAttribute(tab, "name"), panelPage);
			Dimension dim = new Dimension(XML.getAttribute(tabbedPane, "dimension", ""));
			tabPane.setSize(dim.getDimension());
			Map pageProps = (Map) ((Page) curPage).getPropertyMap();
			List dependants = (List) pageProps.get("Dependants");
			if (dependants == null) {
				dependants = new ArrayList();
				pageProps.put("Dependants", dependants);
			}
			dependants.add(tabScreen);
		}
	}
}
