package com.framework.swing.components;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTML.Tag;

public class HTMLParser extends HTMLEditorKit.ParserCallback {
	private StringBuffer resultHtml = new StringBuffer();

	public void handleText(char[] data, int pos) {
		System.out.println(data);
	}

	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		super.handleStartTag(t, a, pos);
	}
	

	@Override
	public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos) {
		super.handleSimpleTag(t, a, pos);
	}

	@Override
	public void handleEndTag(Tag t, int pos) {
		super.handleEndTag(t, pos);
	}

	public String getResultHtml() {
		return "<html><head></head><body><font color=red>Sample <b>text</font></body></html>";
	}
}
