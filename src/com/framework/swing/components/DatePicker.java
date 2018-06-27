package com.framework.swing.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.w3c.dom.Node;

import com.framework.Validator;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.action.CalendarAction;
import com.framework.swing.ui.GUIConstants;
import com.framework.util.DateUtil;
import com.framework.util.ExpressionUtil;
import com.framework.util.StaticData;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 19 Nov 2011
 */
public class DatePicker extends JComponent implements IComponent {
	private Panel panel = new Panel();
	private JTextField textField = new JTextField();
	private JButton button = new JButton();
	private Frame parent;
	private CalendarDialog calendarDialog;
	private String dateFromat = GUIConstants.DEFAULT_DATE_FORMAT;
	private Date date;
	private int selectedDay;
	String validator;
	private String enableExpr;
	private String disableExpr;

	public DatePicker() {
	}

	@Override
	public void init(Node datePicker, Object curPage, Map expData) throws Exception {
		String size = XML.getAttribute(datePicker, "size", "100,20");
		setName(XML.getAttribute(datePicker, "name"));
		setEnableExpr(XML.getAttribute(datePicker, "enable_expr"));
		setDisableExpr(XML.getAttribute(datePicker, "disable_expr"));
		panel.setLayout(new GridBagLayout());
		Dimension dim = com.framework.swing.ui.Dimension.getDimension(size);
		panel.setSize(dim);
		try {
			CalendarAction action = new CalendarAction("gui/images/calendar.png", GUIConstants.CALENDAR, this);
			button.setAction(action);
			button.setPreferredSize(new Dimension(15, 20));
			textField.setPreferredSize(new Dimension((int) dim.getWidth() - 20, (int) dim.getHeight()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		calendarDialog = new CalendarDialog();
		calendarDialog.setLocationRelativeTo(button);
		calendarDialog.setModal(true);
		calendarDialog.pack();
		panel.add(textField, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
		panel.add(button, new GridBagConstraints(1, 0, 0, 0, 0.0, 0.0, 18, 0, new Insets(0, 0, 0, 0), 0, 0));
		this.add(panel);
	}

	public void showCalendarDialog(int x, int y) {
		calendarDialog.showCalendar(date == null ? new Date() : date);
		calendarDialog.setBounds(this.getX() + textField.getWidth() + textField.getX(),
				this.getY() + textField.getHeight(), 200, 200);
		calendarDialog.setVisible(true);
	}

	public String getDateFromat() {
		return dateFromat;
	}

	public void setDateFromat(String dateFromat) {
		this.dateFromat = dateFromat;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		try {
			if (!StringUtil.isEmpty(date)) {
				textField.setText(DateUtil.toString(date, dateFromat));
			} else {
				textField.setText("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getEnableExpr() {
		return enableExpr;
	}

	public void setEnableExpr(String enableExpr) {
		this.enableExpr = enableExpr;
	}

	public String getDisableExpr() {
		return disableExpr;
	}

	public void setDisableExpr(String disableExpr) {
		this.disableExpr = disableExpr;
	}

	@Override
	public void renderData(Object dataObj, Map exprData) throws Exception {
		if (StringUtil.isNotEmpty(disableExpr)) {
			this.setEnabled(ExpressionUtil.excuteExpression(disableExpr, dataObj));
			this.repaint();
		} else if (StringUtil.isNotEmpty(enableExpr)) {
			this.setEnabled(ExpressionUtil.excuteExpression(enableExpr, dataObj));
			this.repaint();
		}
		if (this.isEnabled()) {
			Object compVal = ReflectionInvoker.getProperty(dataObj, this.getName(), false);
			this.setDate((Date) compVal);
		}
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		Date data = this.getDate();
		if (validate)
			validateComp(errors, data);
		return data;
	}

	@Override
	public void reset() {
		this.setDate(null);

	}

	private void validateComp(Vector errors, Object data) {
		if (!StringUtil.isEmpty(validator)) {
			List errorList = Validator.validate(this.getName(), validator, data);
			if (errorList != null && !errorList.isEmpty()) {
				errors.addAll(errorList);
				this.setBorder(Validator.getErrorBorder());
			}

		}
	}

	public class CalendarDialog extends Dialog implements ActionListener, MouseListener {
		JSpinner year = new JSpinner();
		ComboBox months = new ComboBox(new Vector(StaticData.getStaticData(GUIConstants.MONTHS)));
		JLabel[] weekDays = { new JLabel(GUIConstants.WEEK_DAY_SUN), new JLabel(GUIConstants.WEEK_DAY_MON),
				new JLabel(GUIConstants.WEEK_DAY_TUE), new JLabel(GUIConstants.WEEK_DAY_WED),
				new JLabel(GUIConstants.WEEK_DAY_THU), new JLabel(GUIConstants.WEEK_DAY_FRI),
				new JLabel(GUIConstants.WEEK_DAY_SAT) };
		JLabel[][] days = {
				{ new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() },
				{ new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() },
				{ new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() },
				{ new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() },
				{ new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() },
				{ new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel(), new JLabel() } };
		JPanel calendarPanel = new JPanel();

		public CalendarDialog() {
			super(parent);
			this.setLayout(new GridBagLayout());
			this.setUndecorated(true);
			year.setMinimumSize(new Dimension(80, 25));
			year.setPreferredSize(new Dimension(80, 25));
			year.setMaximumSize(new Dimension(80, 25));
			months.setMinimumSize(new Dimension(80, 25));
			months.setPreferredSize(new Dimension(120, 25));
			months.setMaximumSize(new Dimension(180, 25));
			months.addActionListener(this);
			calendarPanel.setLayout(new GridBagLayout());
			calendarPanel.setBackground(Color.WHITE);
			this.getContentPane().add(months,
					new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
			this.getContentPane().add(year,
					new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 18, 1, new Insets(0, 2, 0, 0), 0, 0));

			for (int i = 0; i < weekDays.length; i++) {
				weekDays[i].setSize(new Dimension(40, 40));
				weekDays[i].setBackground(Color.GRAY);
				weekDays[i].setHorizontalTextPosition(JLabel.CENTER);
				calendarPanel.add(weekDays[i], new GridBagConstraints(i, 0, 1, 1, 1.0, 1.0, 18, 1, new Insets(0, 2, 0,
						0), 0, 0));
			}
			for (int i = 0; i < days.length; i++) {
				for (int j = 0; j < days[i].length; j++) {
					days[i][j].setSize(new Dimension(40, 40));
					days[i][j].addMouseListener(this);
					calendarPanel.add(days[i][j], new GridBagConstraints(j, i + 1, 1, 1, 1.0, 1.0, 18, 1, new Insets(0,
							2, 0, 0), 0, 0));
				}
			}
			calendarPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 4));
			this.getContentPane().add(calendarPanel,
					new GridBagConstraints(0, 1, 2, 2, 1.0, 1.0, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
		}

		public void showCalendar(Date date) {
			int dayOfWeek = getFirstDayOfMonth(date);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			selectedDay = c.get(Calendar.DATE);
			months.setSelectedIndex(c.get(Calendar.MONTH) + 1);
			year.setValue(c.get(Calendar.YEAR));

			int dayOfMonth = c.getActualMaximum(Calendar.DATE);
			int day = 1;
			for (int i = 0; i < days.length; i++) {
				for (int j = 0; j < days[i].length; j++) {
					if ((i == 0 && (j + 1) < dayOfWeek) || day > dayOfMonth) {
						days[i][j].setEnabled(false);
						days[i][j].setText(null);
					} else {
						days[i][j].setEnabled(true);
						days[i][j].setText(String.valueOf(day));
						days[i][j].setHorizontalTextPosition(JLabel.CENTER);
						day++;
					}
					if (selectedDay == day) {
						days[i][j].setBackground(Color.BLUE);
					}
				}
			}
		}

		private int getFirstDayOfMonth(Date date) {
			int firstDayOfWeek = -1;
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DATE, 1);
			firstDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			return firstDayOfWeek;
		}

		private void setColor(int day, Color color) {
			for (int i = 0; i < days.length; i++) {
				for (int j = 0; j < days[i].length; j++) {
					if (days[i][j].isEnabled() && days[i][j].getText().equals(String.valueOf(day))) {
						days[i][j].setBackground(color);
					}
				}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof ComboBox) {
				int month = months.getSelectedIndex();
				int year = (Integer) this.year.getValue();
				try {
					setColor(selectedDay, Color.WHITE);
					date = DateUtil.getDate(selectedDay, month - 1, year, dateFromat);
					setColor(selectedDay, Color.ORANGE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				showCalendar(date);
			} else if (e.getSource() instanceof JButton) {

			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			int day = Integer.parseInt(((JLabel) e.getSource()).getText());
			int month = months.getSelectedIndex();
			int year = (Integer) this.year.getValue();
			try {
				date = DateUtil.getDate(day, month - 1, year, dateFromat);
				textField.setText(DateUtil.toString(date, dateFromat));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			this.setVisible(false);

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel label = (JLabel) e.getSource();
			if (label.isEnabled())
				label.setBorder(BorderFactory.createLineBorder(Color.RED));

		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel label = (JLabel) e.getSource();
			if (label.isEnabled())
				label.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}
}
