package com.framework.swing.components;

import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * 
 * @author mahendra
 * 
 * @date 11 Oct 2011
 */
public class SMenuItem extends JMenuItem {
	private String role;
	private String actionService;
	private String nextPage;
	private String recordName;
	
	public SMenuItem(String name){
		super(name);
	}
	public SMenuItem(String name, Icon icon){
		super(name,icon);
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getActionService() {
		return actionService;
	}
	public void setActionService(String actionService) {
		this.actionService = actionService;
	}
	public String getNextPage() {
		return nextPage;
	}
	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}

}
