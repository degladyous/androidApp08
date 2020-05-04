package com.example.photoapp08.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Photo class that holds a list of tags for a given photo
 *
 * @author Daniel Egladyous
 * @author Manpreet Singh
 */
public class Photo implements Serializable{

	/**
	 * Photo file
	 */
	private File photo;

	/**
	 * Photo path
	 */
	private String path;

	/**
	 * Photo caption
	 */
	private String caption;

	/**
	 * List of tags
	 */
	private ArrayList<Tag> tagsList;
	/**
	 * Class Constructor
	 * @param file photo file
	 */

	public Photo(File file) {
		this.photo = file;
		this.path = photo.getAbsolutePath();
		this.tagsList = new ArrayList<Tag>();
		this.caption = file.getName();
	}

	/**
	 * Gets Photo file
	 * @return Photo file
	 */
	public File getPhotoFile() {
		return photo;
	}

	/**
	 * Gets photo path
	 * @return photo path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets photo path
	 * @param path Photo path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets file name
	 * @return file name
	 */
	public String getFileName() {
		return photo.getName();
	}

	/**
	 * Gets Photo caption
	 * @return photo caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * Sets photo caption
	 * @param caption photo caption
	 */
	public void setCaption (String caption) {
		this.caption = caption;
	}

	/**
	 * Adds a new tag to tags list
	 * @param tag new tag to be added to tags list
	 */
	public void addTag(Tag tag) {
		tagsList.add(tag);
	}

	/**
	 * Removes a tag from tags list
	 * @param tag tag to be removed from tags list
	 */
	public void removeTag(Tag tag) {
		tagsList.remove(tag);
	}

	/**
	 * Gets array list of tags for photo
	 * @return array list of tags for photo
	 */
	public ArrayList<Tag> getTags(){
		return tagsList;
	}
}
