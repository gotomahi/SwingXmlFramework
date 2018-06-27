package com.framework.swing.ui;

import com.framework.util.StringUtil;

/**
 * 
 * @author mahendra
 * 
 * @date 12 May 2011
 */
public class Dimension extends java.awt.Dimension {
	private int gridx;
	private int gridy;
	private int gridwidth = 1;
	private int gridheight = 1;
	private double weightx = 1.0;
	private double weighty = 1.0;
	private int anchor = 18;
	private int fill = 1;
	private int top = 5;
	private int left = 5;
	private int bottom = 5;
	private int right = 5;

	public Dimension() {
	}

	public Dimension(int width, int height) {
		super(width, height);
	}

	public Dimension(java.awt.Dimension dim) {
		super(dim);
	}

	public void setPosition(String position, boolean property) {
		if (!StringUtil.isEmpty(position)) {
			String[] dims = position.split(SwingConstants.COMMA);
			int cnt = 0;
			gridx = Integer.parseInt(dims[cnt++]);
			gridy = Integer.parseInt(dims[cnt++]);
			if (property) {
				weightx = 0.0;
				weighty = 0.0;
			} else {
				weightx = 1.0;
				weighty = 1.0;
			}
		}
	}

	public Dimension(String dim) {
		if (!StringUtil.isEmpty(dim)) {
			String[] dims = dim.split(SwingConstants.COMMA);
			int cnt = -1;
			gridx = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				gridy = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				width = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				height = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				weightx = Double.parseDouble(dims[++cnt]);
			if (dims.length > cnt)
				weighty = Double.parseDouble(dims[++cnt]);
			if (dims.length > cnt)
				anchor = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				fill = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				top = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				left = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				bottom = Integer.parseInt(dims[++cnt]);
			if (dims.length > cnt)
				right = Integer.parseInt(dims[++cnt]);
		}
	}

	public Dimension getDimension() {
		return new Dimension(width, height);
	}

	public static Dimension getDimension(String size) {
		Dimension dim = null;
		if (!StringUtil.isEmpty(size)) {
			String[] dims = size.split(SwingConstants.COMMA);
			int cnt = 0;
			dim = new Dimension(Integer.parseInt(dims[cnt++]), Integer.parseInt(dims[cnt++]));
		}
		return dim;
	}

	public int getGridx() {
		return gridx;
	}

	public void setGridx(int gridx) {
		this.gridx = gridx;
	}

	public int getGridy() {
		return gridy;
	}

	public void setGridy(int gridy) {
		this.gridy = gridy;
	}

	public int getGridwidth() {
		return gridwidth;
	}

	public void setGridwidth(int gridwidth) {
		this.gridwidth = gridwidth;
	}

	public int getGridheight() {
		return gridheight;
	}

	public void setGridheight(int gridheight) {
		this.gridheight = gridheight;
	}

	public double getWeightx() {
		return weightx;
	}

	public void setWeightx(double weightx) {
		this.weightx = weightx;
	}

	public double getWeighty() {
		return weighty;
	}

	public void setWeighty(double weighty) {
		this.weighty = weighty;
	}

	public int getAnchor() {
		return anchor;
	}

	public void setAnchor(int anchor) {
		this.anchor = anchor;
	}

	public int getFill() {
		return fill;
	}

	public void setFill(int fill) {
		this.fill = fill;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}
}
