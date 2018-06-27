package com.framework.swing.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.w3c.dom.Node;

import com.ResourceLoader;
import com.framework.Validator;
import com.framework.bom.Persist;
import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.action.DataGridAction;
import com.framework.swing.table.cell.TableColumn;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.EventController;
import com.framework.swing.ui.GUIConstants;
import com.framework.swing.ui.Page;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 28 Apr 2011
 */
public class DataGrid extends Panel implements ActionListener, ItemListener, IComponent {
	protected JTable table = new JTable();
	protected TableModel model = new TableModel();
	protected JToolBar toolBar = new JToolBar();
	protected PopupMenu popupMenu;
	protected Object curPage;
	protected DataGridAction doubleClickAction;
	protected boolean pagination;
	protected JButton firstButton;
	protected JButton nextButton;
	protected JButton previousButton;
	protected JButton lastButton;

	public DataGrid() {
	}

	@Override
	public void init(Node dataGridNode, final Object curPage, Map expData) throws Exception {
		this.curPage = curPage;
		table.setName(XML.getAttribute(dataGridNode, GUIConstants.NAME));
		this.setName(XML.getAttribute(dataGridNode, GUIConstants.NAME));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		pagination = XML.getAttributeBoolean(dataGridNode, "pagination");
		int rowHeight = XML.getAttributeInteger(dataGridNode, GUIConstants.ROW_HEIGHT, -1);
		if (rowHeight != -1)
			table.setRowHeight(rowHeight);
		List<Node> columnNodes = XML.getChildNodes(dataGridNode, GUIConstants.COLUMN);
		for (Node columnNode : columnNodes) {
			TableColumn column = new TableColumn();
			String headerName = ResourceLoader.getInstance().getLabelValue(
					"label." + XML.getAttribute(columnNode, GUIConstants.NAME));
			column.setHeaderName(headerName);
			column.setProperty(XML.getAttribute(columnNode, GUIConstants.PROPERTY));
			column.setEditable(XML.getAttributeBoolean(columnNode, GUIConstants.EDITABLE));
			column.setRenderer(XML.getAttribute(columnNode, GUIConstants.RENDERER));
			column.setRendererData(XML.getAttribute(columnNode, GUIConstants.RENDERER_DATA));
			column.setRenderLabel(XML.getAttribute(columnNode, GUIConstants.RENDERER_LABEL));
			column.setListeners(XML.getAttribute(columnNode, GUIConstants.LISTENERS));
			column.setDynamicData(XML.getAttributeBoolean(columnNode, GUIConstants.DYNAMIC_DATA));
			column.setValidators(XML.getAttribute(columnNode, GUIConstants.VALIDATORS));
			model.addColumn(column);
		}
		table.setModel(model);
		table.setAutoCreateRowSorter(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(table);
		Dimension dim = null;
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		DataGridHelper dataGridHelper = new DataGridHelper();

		dataGridHelper.setRenderers(this, curPage);

		Node toolbars = XML.getChildNode(dataGridNode, GUIConstants.TOOLBARS);
		if (toolbars != null) {
			toolBar = dataGridHelper.getToolBar(this, toolbars);
		}
		this.add(toolBar, BorderLayout.SOUTH);

		// Initialize popup menu
		popupMenu = dataGridHelper.getPopupMenu(this, scrollPane,
				XML.getChildNode(dataGridNode, GUIConstants.POPUP_MENU));
		MousePopupListener mousePopupListener = new MousePopupListener();
		table.addMouseListener(mousePopupListener);
		scrollPane.addMouseListener(mousePopupListener);
	}

	public void setData(Vector data) {
		model.setModelData(data);
		model.fireTableDataChanged();
	}

	public TableModel getModel() {
		return (TableModel) table.getModel();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Page page = (Page) curPage;	
		String recordName = GUIConstants.SELECTED_OBJ;
		if(e.getSource() instanceof SMenuItem){
			SMenuItem menuItem = (SMenuItem)e.getSource();
			recordName = menuItem.getRecordName();
		}
		page.getPageProps().put(recordName, model.getModelData().get(table.getSelectedRow()));
		EventController eventController = new EventController();
		eventController.processAction(curPage, e.getActionCommand(), e.getSource());
	}

	public Vector getSelectedRows(boolean deleteSelNewRows) {
		Vector result = new Vector();
		int[] rows = table.getSelectedRows();
		for (int i = 0; i < rows.length; i++) {
			for (int j = i; j < rows.length; j++) {
				if (rows[i] < rows[j]) {
					int temp = rows[i];
					rows[i] = rows[j];
					rows[j] = temp;
				}
			}
		}
		Vector data = model.getModelData();
		if (data != null && !data.isEmpty()) {
			for (int i = 0; i < rows.length; i++) {
				result.add(data.get(rows[i]));
				if (deleteSelNewRows)
					data.remove(rows[i]);
			}
		}
		if (deleteSelNewRows)
			model.fireTableDataChanged();
		return result;
	}

	@Override
	public void renderData(Object dataObj, Map exprData) throws Exception {
		Object compVal = ReflectionInvoker.getProperty(dataObj, this.table.getName(), false);
		TableModel model = this.getModel();
		Vector val = null;
		if (compVal instanceof List) {
			val = new Vector((List) compVal);
		} else if (compVal instanceof Vector) {
			val = (Vector) compVal;
		} else {
			val = new Vector();
		}
		boolean enableNext = false;
		boolean enablePrevious = false;
		if (val != null && !val.isEmpty()) {
			enableNext = pagination && val.size() > 25;
			int pageIndex = (Integer) ReflectionInvoker.getProperty(dataObj, "pageIndex", false);
			enablePrevious = pagination && pageIndex > 0;
			model.setModelData(val);
			model.fireTableDataChanged();
		}
		if (this.pagination) {
			// Enable/Disable pagination buttons
			this.nextButton.setEnabled(enableNext);
			this.lastButton.setEnabled(enableNext);
			this.previousButton.setEnabled(enablePrevious);
			this.firstButton.setEnabled(enablePrevious);
		}
	}

	@Override
	public Object readData(Vector errors, boolean validate) {
		Object data = this.getModel().getModelData();
		if (validate)
			validateComp(errors);
		return data;
	}

	@Override
	public void reset() {
		model.setModelData(new Vector());
	}

	private void validateComp(Vector errors) {
		if (model.getModelData() != null && !model.getModelData().isEmpty()) {
			for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
				Persist persist = (Persist) model.getRow(rowIndex);
				if (persist.getStatus() == Persist.MODIFIED || persist.getStatus() == Persist.NEW) {
					for (int columnIndex = 0; columnIndex < model.getColumnCount(); columnIndex++) {
						Object value = model.getValueAt(rowIndex, columnIndex);
						String validators = model.getValidator(columnIndex);
						if (StringUtil.isNotEmpty(validators)) {
							List errorList = Validator
									.validate(model.getColumnProperty(columnIndex), validators, value);
							if (errorList != null && !errorList.isEmpty()) {
								errors.add(errorList);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {

	}

	class MousePopupListener extends MouseAdapter {

		private void showPopupMenu(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popupMenu.show(DataGrid.this, e.getX(), e.getY());
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			int row = table.rowAtPoint(e.getPoint());
			if (SwingUtilities.isRightMouseButton(e)) {
				if(row > -1)
					table.setRowSelectionInterval(row, row);
				showPopupMenu(e);
			}
			if (!StringUtil.isEmpty(doubleClickAction) && e.getClickCount() == 2) {
				Persist persist = (Persist)model.getModelData().get(table.getSelectedRow());
				persist.setStatus(Persist.MODIFIED);
				if(doubleClickAction.getNextPage() != null){
					EventController pe = new EventController();
					pe.processAction(curPage, doubleClickAction.getNextPage(), doubleClickAction);
				}
			}

		}
	}

}
