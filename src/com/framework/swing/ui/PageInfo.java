package com.framework.swing.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author mahendra
 * 
 * @date 12 Feb 2012
 */
public class PageInfo {

	private String name;
	private String filePath;
	private boolean defaultPage;
	private Page page;
	private Map atts = new HashMap(1);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(boolean defaultPage) {
		this.defaultPage = defaultPage;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public void addParam(String name, Object value) {
		atts.put(name, value);
	}

	public Map getAtts() {
		return atts;
	}

	public void setAtts(Map atts) {
		this.atts = atts;
	}
}
