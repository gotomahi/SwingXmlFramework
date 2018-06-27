package com.framework.swing.action;

import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.ResourceLoader;
import com.framework.swing.ui.GUIConstants;

/**
 * 
 * @author mahendra
 * 
 * @date 18 May 2011
 */
public abstract class Action extends AbstractAction {
	private Map props;
	private String name;
	private boolean delete;
	private boolean edit;
	private String nextPage;

	public Action() {
	}

	public Action(String image, String name, boolean delete, boolean edit) throws Exception {
		super(name, new ImageIcon(ResourceLoader.getInstance().getResource(image)));
		this.name = name;
		this.delete = delete;
		this.edit = edit;
	}

	public Action(String name, boolean delete, boolean edit) throws Exception {
		super(name);
		this.name = name;
		this.delete = delete;
		this.edit = edit;
	}

	public Action(String image, String name) throws Exception {
		super(name, new ImageIcon(ResourceLoader.getInstance().getResource(image)));
		this.name = name;
	}

	public Action(String name) throws Exception {
		super(name);
		this.name = name;
	}

	public Map getProps() {
		return props;
	}

	public void setProps(Map props) {
		this.props = props;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public String getNextPage() {
		return nextPage;
	}

	public void setNextPage(String nextPage) {
		this.nextPage = nextPage;
	}

}
