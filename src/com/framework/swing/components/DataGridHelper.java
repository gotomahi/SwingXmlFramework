package com.framework.swing.components;

import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.w3c.dom.Node;

import com.framework.reflect.ReflectionInvoker;
import com.framework.swing.action.DataGridAction;
import com.framework.swing.table.cell.CellComboBox;
import com.framework.swing.table.cell.TableColumn;
import com.framework.swing.ui.Dimension;
import com.framework.swing.ui.GUIConstants;
import com.framework.swing.ui.Page;
import com.framework.swing.ui.RegisterListener;
import com.framework.util.StringUtil;
import com.framework.xml.XML;

/**
 * 
 * @author mahendra
 * 
 * @date 19 Feb 2012
 */
public class DataGridHelper {

	/**
	 * 
	 * @param dataGrid
	 * @param toolbars
	 * @return
	 * @throws Exception
	 */
	public ToolBar getToolBar(DataGrid dataGrid, Node toolbars) throws Exception {
		ToolBar toolBar = new ToolBar();
		toolBar.setLayout(new BorderLayout());
		Panel leftPanel = new Panel();
		Panel rightPanel = new Panel();
		List<Node> toolBarItems = XML.getChildNodes(toolbars, GUIConstants.TOOLBAR);
		Dimension dim = new Dimension();
		dim.setPosition(XML.getAttribute(toolbars, GUIConstants.POSITION, ""), false);
		if (toolBarItems != null && !toolBarItems.isEmpty()) {
			for (Node toolBarItem : toolBarItems) {
				String name = XML.getAttribute(toolBarItem, GUIConstants.NAME);
				String image = XML.getAttribute(toolBarItem, GUIConstants.IMAGE);
				boolean deleteAction = XML.getAttributeBoolean(toolBarItem, GUIConstants.DELETE, false);
				boolean editAction = XML.getAttributeBoolean(toolBarItem, GUIConstants.EDIT, false);
				String nextPage = XML.getAttribute(toolBarItem, GUIConstants.NEXT_PAGE);
				JButton button = getToolBarButton(dataGrid, name, image, deleteAction, editAction, nextPage);
				leftPanel.add(button);
			}
		}
		toolBar.add(leftPanel, BorderLayout.WEST);
		if (dataGrid.pagination) {
			dataGrid.firstButton = getToolBarButton(dataGrid, "First", "gui/images/first.png", false, false, null);
			rightPanel.add(dataGrid.firstButton);
			dataGrid.previousButton = getToolBarButton(dataGrid, "Previous", "gui/images/previous.png", false, false,
					null);
			rightPanel.add(dataGrid.previousButton);
			dataGrid.nextButton = getToolBarButton(dataGrid, "Next", "gui/images/next.png", false, false, null);
			rightPanel.add(dataGrid.nextButton);
			dataGrid.lastButton = getToolBarButton(dataGrid, "Last", "gui/images/last.png", false, false, null);
			rightPanel.add(dataGrid.lastButton);
		}
		toolBar.add(rightPanel, BorderLayout.EAST);
		return toolBar;
	}

	/**
	 * 
	 * @param dataGrid
	 * @param name
	 * @param image
	 * @param deleteAction
	 * @param editAction
	 * @param nextPage
	 * @return
	 * @throws Exception
	 */
	public JButton getToolBarButton(DataGrid dataGrid, String name, String image, boolean deleteAction,
			boolean editAction, String nextPage) throws Exception {
		Button button = new Button();
		DataGridAction action = null;
		if (!StringUtil.isEmpty(image)) {
			action = new DataGridAction(name, image, deleteAction, editAction, dataGrid, dataGrid.curPage);
		} else {
			action = new DataGridAction(name, deleteAction, editAction, dataGrid, dataGrid.curPage);
		}
		action.setEdit(editAction);
		if (editAction) {
			dataGrid.doubleClickAction = action;
		}
		action.setNextPage(nextPage);
		button.setPreferredSize(new Dimension(20, 20));
		button.setAction(action);
		button.setHorizontalAlignment(JButton.HORIZONTAL);
		button.setHorizontalTextPosition(JButton.HORIZONTAL);
		button.setToolTipText(name);
		return button;
	}

	/**
	 * 
	 * @param dataGrid
	 * @param scrollPane
	 * @param popupMenuNode
	 * @return
	 * @throws Exception
	 */
	public PopupMenu getPopupMenu(DataGrid dataGrid, JScrollPane scrollPane, Node popupMenuNode) throws Exception {
		PopupMenu popupMenu = null;
		if (popupMenuNode != null) {
			popupMenu = new PopupMenu(dataGrid, popupMenuNode, dataGrid);
			dataGrid.table.add(popupMenu);
			scrollPane.add(popupMenu);
		}
		return popupMenu;
	}

	/**
	 * 
	 * @param dataGrid
	 * @param dataObj
	 * @param curPage
	 * @throws Exception
	 */
	public void setRenderers(DataGrid dataGrid, Object curPage) throws Exception {
		for (int i = 0; i < dataGrid.model.getColumns().size(); i++) {
			TableColumn tcol = (TableColumn) dataGrid.model.getColumns().get(i);
			TableCellEditor cellEditor = null;
			TableCellRenderer cellRenderer = null;
			javax.swing.table.TableColumn column = dataGrid.table.getColumnModel().getColumn(i);
			if (GUIConstants.COMBOBOX_RENDERER.equals(tcol.getRenderer())) {
				Vector list = null;
				if (!tcol.isDynamicData()){
					Page page = (Page)curPage;
					Object obj = ReflectionInvoker.getProperty(page.getPageProps(), tcol.getRendererData());
					if(obj instanceof List)
					list = new Vector((List)obj);
				}else{
					list = new Vector();
				}
				cellRenderer = RendererFactory.getFactory().getCellComboboxRenderer(list, tcol.getRenderLabel(),
						tcol.getProperty(), tcol.isDynamicData(), tcol.getRendererData());

				if (list == null)
					list = new Vector();
				CellComboBox comboBox = new CellComboBox(list, tcol.getRenderLabel(), "");
				comboBox.setActionCommand(tcol.getProperty());
				comboBox.addItemListener(dataGrid);
				RegisterListener.getInstance().registerListeners(tcol.getListeners(), comboBox, dataGrid);
				cellEditor = RendererFactory.getFactory().getComboboxCellEditor(comboBox, tcol.isDynamicData(),
						tcol.getRendererData());
			} else if (GUIConstants.CHECKBOX_RENDERER.equalsIgnoreCase(tcol.getRenderer())) {
				JCheckBox checkBox = new JCheckBox();
				cellEditor = RendererFactory.getFactory().getCheckBoxCellEditor(checkBox);
				cellRenderer = RendererFactory.getFactory().getCellCheckBoxRenderer();
			} else if (GUIConstants.DATEPICKER_RENDERER.equalsIgnoreCase(tcol.getRenderer())) {
				DatePicker datePicker = new DatePicker();
				cellEditor = RendererFactory.getFactory().getDatePickerCellEditor(datePicker);
				cellRenderer = RendererFactory.getFactory().getDatePickerRender(datePicker);
			} else if (GUIConstants.NUMERIC_RENDERER.equalsIgnoreCase(tcol.getRenderer())) {
				cellEditor = RendererFactory.getFactory().getNumericCellEditor(new JTextField());
				cellRenderer = RendererFactory.getFactory().getNumericRenderer();
			}
			if (cellEditor != null) {
				column.setCellEditor(cellEditor);
			}
			if (cellRenderer != null) {
				column.setCellRenderer(cellRenderer);
			}
		}
	}
}
