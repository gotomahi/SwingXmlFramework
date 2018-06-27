package com.framework.swing.ui;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.framework.swing.components.ComboBox;
import com.framework.swing.table.cell.CellComboBox;

/**
 * 
 * @author mahendra
 * 
 * @date 7 Jun 2011
 */
public class RegisterListener {
	private static RegisterListener registerListener;

	private RegisterListener() {

	}

	public static RegisterListener getInstance() {
		if (registerListener == null) {
			registerListener = new RegisterListener();
		}
		return registerListener;
	}

	public void registerListeners(String listeners, JComponent comp, Object curPage) {
		if (listeners != null) {
			StringTokenizer tokens = new StringTokenizer(listeners);
			while (tokens.hasMoreTokens()) {
				String listener = tokens.nextToken();
				if (comp instanceof CellComboBox) {
					((CellComboBox) comp).addActionListener((ActionListener) curPage);
				}else if (comp instanceof JComboBox) {
					if ("ActionListener".equalsIgnoreCase(listener)) {
						((ComboBox) comp).addActionListener((ActionListener) curPage);
					} else if ("ItemListener".equalsIgnoreCase(listener)) {
						((ComboBox) comp).addItemListener((ItemListener) curPage);
					}
				}
			}
		}
	}
}
