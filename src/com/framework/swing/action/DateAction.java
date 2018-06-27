package com.framework.swing.action;

import java.awt.event.ActionEvent;

/**
 * 
 * @author mahendra
 * 
 * @date 6 Nov 2011
 */
public class DateAction extends Action {
	private Object curPage;

	public DateAction(String image, String name, boolean delete, boolean edit, Object curPage)
			throws Exception {
		super(image, name, delete, edit);
		this.curPage = curPage;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
