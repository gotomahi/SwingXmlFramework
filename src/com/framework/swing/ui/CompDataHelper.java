package com.framework.swing.ui;

import java.awt.Component;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.framework.swing.components.Button;
import com.framework.swing.components.IComponent;
import com.framework.swing.components.Panel;

/**
 * This will renders the data for components
 * 
 * @author mahendra
 * 
 * @date 27 Apr 2011
 */
public class CompDataHelper {

	public static void init(Object curPage, Component[] panelComps) throws Exception {
		Page page = (Page)curPage;
		renderData(curPage,page.getPageProps(), panelComps, false, null);
	}

	/**
	 * Render page data
	 * 
	 * @param curPage
	 * @param dataObj
	 * @param panelComps
	 * @param skipPanelComponent
	 * @param exprData
	 * @throws Exception
	 */
	public static void renderData(Object curPage, Object dataObj, Component[] panelComps, boolean skipPanelComponent,
			Map exprData) throws Exception {
		if (dataObj != null) {
			for (int j = 0; j < panelComps.length; j++) {
				if (panelComps[j] instanceof IComponent) {
					IComponent icomp = (IComponent) panelComps[j];
					icomp.renderData(dataObj, exprData);
				} else if (panelComps[j] instanceof JScrollPane) {
					JScrollPane sp = (JScrollPane) panelComps[j];
					renderData(curPage, dataObj, sp.getComponents(), skipPanelComponent, exprData);
				} else if (panelComps[j] instanceof JViewport) {
					JViewport vp = (JViewport) panelComps[j];
					renderData(curPage, dataObj, vp.getComponents(), skipPanelComponent, exprData);
				} else if (panelComps[j] instanceof PanelComponent && !skipPanelComponent) {
					PanelComponent pc = (PanelComponent) panelComps[j];
					pc.addPanelComponents(curPage, dataObj,exprData);
				} else if (panelComps[j] instanceof JPanel) {
					renderData(curPage, dataObj, ((JPanel) panelComps[j]).getComponents(), skipPanelComponent, exprData);
				}
			}
		}
	}

	/**
	 * Render page data
	 * 
	 * @param curPage
	 * @param dataObj
	 * @param panelComps
	 * @param skipPanelComponent
	 * @param exprData
	 * @throws Exception
	 */
	public static void resetData(Object curPage, Object dataObj, Component[] panelComps) throws Exception {
		if (dataObj != null) {
			for (int j = 0; j < panelComps.length; j++) {
				if (panelComps[j] instanceof IComponent) {
					IComponent icomp = (IComponent) panelComps[j];
					icomp.reset();
				} else if (panelComps[j] instanceof JScrollPane) {
					JScrollPane sp = (JScrollPane) panelComps[j];
					resetData(curPage, dataObj, sp.getComponents());
				} else if (panelComps[j] instanceof JViewport) {
					JViewport vp = (JViewport) panelComps[j];
					resetData(curPage, dataObj, vp.getComponents());
				} else if (panelComps[j] instanceof PanelComponent) {
					PanelComponent pc = (PanelComponent) panelComps[j];
					pc.removeComps(curPage, dataObj);
				} else if (panelComps[j] instanceof JPanel) {
					resetData(curPage, dataObj, ((JPanel) panelComps[j]).getComponents());
				}
			}
		}
	}

	/**
	 * Get page default button
	 * 
	 * @param panel
	 * @return
	 */
	public static Button getPageDefButton(Panel panel) {
		for (Component comp : panel.getComponents()) {
			if (comp instanceof Button) {
				Button b = (Button) comp;
				if (b.isDefaultButton())
					return (Button) comp;
			} else if (comp instanceof Panel) {
				Button but = getPageDefButton((Panel) comp);
				if (but != null)
					return but;
			}
		}
		return null;
	}
}
