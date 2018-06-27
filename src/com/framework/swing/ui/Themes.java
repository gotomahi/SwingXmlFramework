package com.framework.swing.ui;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.ResourceLoader;

/**
 * http://www.randelshofer.ch/quaqua/guide/index.html use this link for quaqua styles
 * @author mahendra
 * 
 * @date 1 Jun 2011
 */
public class Themes {

	private final static Themes themes = new Themes();
	public final static int THEME_APPLE_OS = 1;
	public final static int THEME_NIMBUS = 2;
	public final static int THEME_SYNTH = 3;
	private int currentTheme = 2;

	private Themes() {

	}

	public static Themes getThemes() {
		return themes;
	}

	public void setTheme(int themeId) throws Exception {
		currentTheme = themeId;
		switch (themeId) {
		case THEME_APPLE_OS:
			System.setProperty("Quaqua.tabLayoutPolicy", "wrap");
			System.setProperty("Quaqua.design", "tiger");
			System.setProperty("Quaqua.sizeStyle", "medium");
			UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
			break;
		case THEME_NIMBUS:
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			break;
		case THEME_SYNTH:
			SynthLookAndFeel lookAndFeel = new SynthLookAndFeel();
			try {
				lookAndFeel.load(ResourceLoader.getInstance().getInputStream("synth.xml"), ResourceLoader.class);
				UIManager.setLookAndFeel(lookAndFeel);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	public void setClientProperties(JComponent comp) {
		Map<String, Object> props = new HashMap<String, Object>();
		switch (currentTheme) {
		case THEME_APPLE_OS:
			if (comp instanceof JToolBar) {
				props.put("Quaqua.ToolBar.style", "plain");
			} else if (comp instanceof JTree) {
				props.put("Quaqua.Tree.style", "striped");
			} else if (comp instanceof JButton) {
				props.put("Quaqua.Button.style", "placard");
			} else if (comp instanceof JButton) {
				props.put("Quaqua.Table.style", "striped");
			}

		}
		for (String property : props.keySet()) {
			comp.putClientProperty(property, props.get(property));
		}
	}
}
