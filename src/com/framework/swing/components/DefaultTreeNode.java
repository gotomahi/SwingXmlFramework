package com.framework.swing.components;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.w3c.dom.Node;

/**
 * 
 * @author mahendra
 * 
 * @date 16 May 2011
 */
public class DefaultTreeNode extends DefaultMutableTreeNode {
	private String displayText;
	private String image;
	private PopupMenu popupMenu;

	public DefaultTreeNode(String str) {
		super(str);
	}

	public DefaultTreeNode(String str, String image) {
		super(str);
		this.image = image;		
	}

	public DefaultTreeNode(Object obj) {
		super(obj);
	}
	
	public void preparePopup(Panel panel, Node popupNode,Object obj){
		if(popupNode != null){
			popupMenu = new PopupMenu(panel, popupNode, obj);			
		}
	}
	
	public PopupMenu getPopupMenu(){
		return this.popupMenu;
	}

	public DefaultTreeNode() {

	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
