package com.framework.swing.action;

import java.awt.event.ActionEvent;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.components.DataGrid;
import com.framework.swing.ui.EventController;
import com.framework.swing.ui.GUIConstants;
import com.framework.swing.ui.Page;

/**
 * 
 * @author mahendra
 * 
 * @date 18 May 2011
 */
public class DataGridAction extends Action {
	private static Logger LOG = Logger.getLogger(DataGridAction.class);
	private DataGrid dataGrid;
	private Object curPage;

	public DataGridAction() {
	}

	public DataGridAction(String name, String image, boolean delete, boolean edit, DataGrid dataGrid, Object curPage)
			throws Exception {
		super(image, name, delete, edit);
		this.dataGrid = dataGrid;
		this.curPage = curPage;
	}

	public DataGridAction(String name, boolean delete, boolean edit, DataGrid dataGrid, Object curPage)
			throws Exception {
		super(name, delete, edit);
		this.dataGrid = dataGrid;
		this.curPage = curPage;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Page page = (Page) curPage;
		Object actionObj = page.getPageProperty(GUIConstants.ACTION);
		if ("Next".equals(e.getActionCommand()) || "Previous".equals(e.getActionCommand())) {
			try {
				Object[] params = { "Next".equals(e.getActionCommand()) };
				Class[] types = { Boolean.class };
				ReflectionInvoker.invoke(actionObj, "setPageIndex", params, types);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else if ("Last".equals(e.getActionCommand()) || "First".equals(e.getActionCommand())) {
			try {
				Object[] params = { "First".equals(e.getActionCommand()) ? 0 : -1 };
				Class[] types = { Boolean.class };
				ReflectionInvoker.invoke(actionObj, "setPageIndex", params, types);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			Vector selObj = dataGrid.getSelectedRows("Delete".equalsIgnoreCase(e.getActionCommand()) ? true : false);
			updateObjStatus(selObj);
			page.getPageProps().put("selectedObj", selObj);
		}
		EventController pe = new EventController();
		pe.processAction(curPage, e.getActionCommand(), e.getSource());
	}

	private void updateObjStatus(Vector selObj) {
		if (selObj != null && !selObj.isEmpty()) {
			for (Object obj : selObj) {
				try {
					int status = (Integer) ReflectionInvoker.getProperty(obj, "status");
					if (status == 50000 && (isDelete() || isEdit()))
						ReflectionInvoker.setProperty(obj, "status", isEdit() ? 20000 : 30000);
				} catch (Exception ex) {
					LOG.error("", ex);
				}
			}
		}
	}
}
