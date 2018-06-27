package com.framework.swing.components;

import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import org.w3c.dom.Node;

import com.ResourceLoader;
import com.framework.swing.action.GeneralAction;
import com.framework.swing.ui.Dimension;
import com.framework.xml.XML;

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

}
