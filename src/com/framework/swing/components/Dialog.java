package com.framework.swing.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import org.w3c.dom.Node;

import com.framework.swing.action.DialogKeyAction;
import com.framework.swing.ui.Dimension;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 22 Apr 2011
 */
public class Dialog extends JDialog implements ActionListener, WindowListener {
	private String layoutType;

	public Dialog() {
		super();
		init();
	}

	public Dialog(JFrame frame) {
		super(frame);
		init();
	}

	public Dialog(JFrame frame, Node node) {
		super(frame);
		try {
			layoutType = XML.getAttribute(node, "layout", "Grid");
			if ("Grid".equalsIgnoreCase(layoutType)) {
				this.setLayout(new GridBagLayout());
			} else if ("Border".equalsIgnoreCase(layoutType)) {
				this.setLayout(new BorderLayout());
			} else if ("Flow".equalsIgnoreCase(layoutType)) {
				this.setLayout(new FlowLayout());
			}
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init(){
		this.addWindowListener(this);
		new DialogKeyAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "DialogESCClose");
	}

	public void addComponent(JComponent comp, Node node) throws Exception {
		Object constraints = null;
		if ("Grid".equalsIgnoreCase(layoutType)) {
			Dimension dim = new Dimension(XML.getAttribute(node, "dimension", ""));
			constraints = new GridBagConstraints(dim.getGridx(), dim.getGridy(), 1, 1, 1.0, 1.0, dim.getAnchor(),
					dim.getFill(), new Insets(dim.getTop(), dim.getLeft(), dim.getBottom(), dim.getRight()), 0, 0);

		} else if ("Border".equalsIgnoreCase(layoutType)) {
			constraints = XML.getAttribute(node, "position");
		}
		this.getContentPane().add(comp, constraints);
	}

	public String getLayoutType() {
		return layoutType;
	}

	public void setLayoutType(String layoutType) {
		this.layoutType = layoutType;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.dispose();
		this.setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
