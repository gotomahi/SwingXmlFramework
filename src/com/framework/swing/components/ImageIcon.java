package com.framework.swing.components;

import java.awt.Toolkit;

/**
 * 
 * @author mahendra
 * 
 * @date 18 May 2011
 */
public class ImageIcon extends javax.swing.ImageIcon {
	public ImageIcon() {
	}

	public ImageIcon(String name) throws Exception {
		//super(ImageIcon.class.getResource("com/gui/images/"+name));
		super(Toolkit.getDefaultToolkit().getImage(name));
	}
}
