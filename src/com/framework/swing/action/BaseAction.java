package com.framework.swing.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.framework.BaseError;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.ui.SwingConfig;

/**
 * 
 * @author mahendra
 * 
 * @date 27 Apr 2011
 */
public class BaseAction {
	private static final Logger LOG = Logger.getLogger(BaseAction.class);
	private String pageName;
	protected Vector selectedObj;
	protected Vector deletedObj = new Vector();
	protected Map screenParamMap = new HashMap();
	protected String actionCommand;
	private Map actionMap = new HashMap();
	protected Map treeMap;
	private int pageIndex;
	private int pageSize;

	public Object actionPerformed(String actionCommand) {
		try {
			actionMap.clear();
			executeAction(actionCommand);
		} catch (Exception ex) {
			LOG.error("", ex);
		}
		return actionMap;
	}

	protected void executeAction(String actionCommand) throws Exception{
		
	}

	public void init(Map data) throws Exception {

	}

	public boolean validate(ActionEvent e) {
		return true;
	}

	public void addError(BaseError error) {
		Vector<BaseError> errors = (Vector<BaseError>) actionMap.get("ActionErrors");
		if (errors == null) {
			errors = new Vector<BaseError>();
			actionMap.put("ActionErrors", errors);
		}
		if (!errors.contains(error)) {
			errors.add(error);
		}
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public long getClientId() {
		Object obj = getSessionValue("User");
		Long clientId = null;
		try {
			clientId = (Long) ReflectionInvoker.getProperty(obj, "organisation.client.clientId", false);
		} catch (Exception e) {

		}
		return clientId;
	}

	public void setSessionValue(Object key, Object value) {
		SwingConfig.getSwingConfig().add(key, value);
	}

	public Object getSessionValue(Object key) {
		return SwingConfig.getSwingConfig().get(key);
	}

	public Vector getSelectedObj() {
		return selectedObj;
	}

	public void setSelectedObj(Vector selectedObj) {
		this.selectedObj = selectedObj;
	}

	public Vector getDeletedObj() {
		return deletedObj;
	}

	public void setDeletedObj(Vector deletedObj) {
		this.deletedObj = deletedObj;
	}

	public Map getScreenParamMap() {
		return screenParamMap;
	}

	public void setScreenParamMap(Map screenParamMap) {
		this.screenParamMap = screenParamMap;
	}

	public String getActionCommand() {
		return actionCommand;
	}

	public void setActionCommand(String actionCommand) {
		this.actionCommand = actionCommand;
	}

	public Map getTreeMap() {
		return treeMap;
	}

	public void setTreeMap(Map treeMap) {
		this.treeMap = treeMap;
	}

	public void addActionData(String key, Object value) {
		Map ret = (Map) actionMap.get("Result");
		if (ret == null) {
			ret = new HashMap();
			actionMap.put("Result", ret);
		}
		ret.put(key, value);
	}

	public void addActionData(Map data) {
		Map ret = (Map) actionMap.get("Result");
		if (ret == null) {
			ret = new HashMap();
			actionMap.put("Result", ret);
		}
		ret.putAll(data);
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setPageIndex(boolean next) {
		if (next) {
			this.pageSize = this.pageSize + 1;
		} else {
			this.pageSize = this.pageSize - 1;
		}
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
