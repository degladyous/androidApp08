package com.example.photoapp08.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Album class that holds a list of photos for a given user
 *
 * @author Daniel Egladyous
 * @author Manpreet Singh
 */
public class Album implements Serializable{

	/**
	 * Album Name
	 */
	private String albumName;

	/**
	 * Photos List
	 */
	private ArrayList<Photo> photosList;


	/**
	 * Class Constructor
	 * @param albumName Album Name
	 */
	public Album(String albumName) {
		this.albumName = albumName;
		photosList = new ArrayList<Photo>();
	}

	/**
	 * Sets Album Name
	 * @param albumName Album Names
	 */
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	/**
	 * Gets Album Name
	 * @return Album Name
	 */
	public String getAlbumName() {
		 return albumName;
	}

	/**
	 * Adds a new photo
	 * @param photo New photo
	 */
	public void addPhoto(Photo photo) {
		photosList.add(photo);
	}

	/**
	 * Deletes a photo
	 * @param photo Photo to be deleted
	 */
	public void deletePhoto(Photo photo) {
		photosList.remove(photo);
	}

	/**
	 * Gets photos list
	 * @return List of photos
	 */
	public ArrayList<Photo> getAlbum(){
		return photosList;
	}

	/**
	 * Gets number of photos
	 * @return Number of photos
	 */
	public int getNumberOfPhotos() {
		return photosList.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return albumName;
	}
}
