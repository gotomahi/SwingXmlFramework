package com.framework.swing.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * 
 * @author mahendra
 * 
 * @date 21 Nov 2011
 */
public class MessageFactory {

	public static int showErrorMessage(Component parent, String message, String title) {
		return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.OK_OPTION);
	}

	public static void showMessage(Component parent, String message, String title) {
		JOptionPane.showMessageDialog(parent, message, title, JOptionPane.OK_OPTION);
	}

	public static void showProgressMessage(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message);
	}

	public static int showConfirmMessage(Component parent, String message, String title) {
		return JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.OK_CANCEL_OPTION);
	}

}
