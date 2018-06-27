package com.framework.swing.table.cell;

/**
 * 
 * @author mahendra
 * 
 * @date 28 Apr 2011
 */
public class TableColumn {

	private String property;
	private String headerName;
	private boolean editable;
	private String renderer;
	private String rendererData;
	private String renderLabel;
	private String listeners;
	private boolean dynamicData;
	private String validators;

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public String getRendererData() {
		return rendererData;
	}

	public void setRendererData(String rendererData) {
		this.rendererData = rendererData;
	}

	public String getRenderLabel() {
		return renderLabel;
	}

	public void setRenderLabel(String renderLabel) {
		this.renderLabel = renderLabel;
	}

	public String getListeners() {
		return listeners;
	}

	public void setListeners(String listeners) {
		this.listeners = listeners;
	}

	public boolean isDynamicData() {
		return dynamicData;
	}

	public void setDynamicData(boolean dynamicData) {
		this.dynamicData = dynamicData;
	}

	public String getValidators() {
		return validators;
	}

	public void setValidators(String validators) {
		this.validators = validators;
	}
}
