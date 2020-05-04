package com.example.photoapp08.model;

import java.io.Serializable;

/**
 * Tag class that holds tag name and value
 *
 * @author Daniel Egladyous
 * @author Manpreet Singh
 */
public class Tag implements Serializable{

	/**
	 * Tag name - Either Person or Location
	 */
	private String name;

	/**
	 * Tag value
	 */
	private String value;

	/**
	 * Class Constructor
	 * @param name tag name
	 * @param value tag value
	 */
	public Tag(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @param name tag name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param value tag value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return tag name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return tag value
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tag [key=" + name + ", value=" + value + "]";
	}
}
