package org.ece.owngallery.model;

import org.ece.owngallery.R;

public class SlideData {
	private String name = "";
	private int icon = R.drawable.nophotos;
	public int state = 0;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
