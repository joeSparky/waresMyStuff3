package com.forms;

public class SelectIdObject {
	int id;

	public enum ACTION {
		SELECT, DELETE, DELETETEST
	};

	ACTION action;

	public SelectIdObject(ACTION action, int id) {
		this.action = action;
		this.id = id;
	}
	public ACTION getAction() {
		return action;
	}
	public int getId() {
		return id;
	}

}
