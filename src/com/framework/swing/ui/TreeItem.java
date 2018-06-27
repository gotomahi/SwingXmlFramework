package com.framework.swing.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * 
 * @author mahendra
 * 
 * @date 18 Feb 2012
 */
public class TreeItem {

	private String name;
	private String screen;
	private String image;
	private int order;
	private boolean defaultScree;
	private String diableExpr;
	private String enableExpr;
	private String subTreeData;
	private String actionService;
	private Node popupMenu;
	private List<TreeItem> subTree = new ArrayList<TreeItem>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public boolean isDefaultScree() {
		return defaultScree;
	}

	public void setDefaultScree(boolean defaultScree) {
		this.defaultScree = defaultScree;
	}

	public String getDiableExpr() {
		return diableExpr;
	}

	public void setDiableExpr(String diableExpr) {
		this.diableExpr = diableExpr;
	}

	public String getEnableExpr() {
		return enableExpr;
	}

	public void setEnableExpr(String enableExpr) {
		this.enableExpr = enableExpr;
	}

	public String getSubTreeData() {
		return subTreeData;
	}

	public void setSubTreeData(String subTreeData) {
		this.subTreeData = subTreeData;
	}

	public List<TreeItem> getSubTree() {
		return subTree;
	}

	public void setSubTree(List<TreeItem> subTree) {
		this.subTree = subTree;
	}

	public Node getPopupMenu() {
		return popupMenu;
	}

	public void setPopupMenu(Node popupMenu) {
		this.popupMenu = popupMenu;
	}

	public String getActionService() {
		return actionService;
	}

	public void setActionService(String actionService) {
		this.actionService = actionService;
	}

}
