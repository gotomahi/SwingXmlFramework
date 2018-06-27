package com.framework.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class DialogKeyAction extends Action {
	private JDialog window;

	public DialogKeyAction() {
	}

	public DialogKeyAction(JDialog window, KeyStroke keyStroke, String actionName) {
		this.window = window;
		JRootPane root = window.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
		root.getActionMap().put(actionName, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
	}

}
