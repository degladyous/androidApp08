package com.example.photoapp08.model;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * AlbumsList class that holds a list of albums. It reads and writes albums to albums.dat file
 *
 * @author Daniel Egladyous
 * @author Manpreet Singh
 */
public class AlbumsList {

	/**
	 * List of albums
	 */
	public static ArrayList<Album> albums = new ArrayList<Album>();

	/**
	 * Current activity_albumslistview selected
	 */
	public static Album currentAlbum;
	public static Photo selectedPhoto;

	/**
	 * ObjectInputStream
	 */
	private static ObjectInputStream ois = null;

	/**
	 * ObjectOutputStream
	 */
	private static ObjectOutputStream oos = null;

	/**
	 * Store file
	 */
	public static final String storeFile = "album.dat";
	//public static final String storeDir = "dat";
	/**
	 * Writes albums list into albums.dat
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void writeAlbums(Context context) {
		try{
			oos = new ObjectOutputStream(context.openFileOutput(storeFile, Context.MODE_PRIVATE));
			oos.writeObject(albums);
			oos.flush();
			oos.close();
		} catch (FileNotFoundException e){
			System.out.println("FILE NOT FOUND WHEN WRITING!!!!!!");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reads albums list from activity_albumslistview.dat
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static void readAlbums(Context context) {
//		ois = new ObjectInputStream(new FileInputStream(storeFile));
//		albums = (ArrayList<Album>) ois.readObject();
		try{
			ois = new ObjectInputStream(context.openFileInput(storeFile));

		} catch (FileNotFoundException e){
			System.out.println("FILE NOT FOUND WHEN READING!!!!!!");
			albums = new ArrayList<Album>();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (ois != null) {
			try {
				albums = (ArrayList<Album>) ois.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Sets current album when album is selected
	 * @param album current album selected
	 */
	public static void setCurrentAlbum(Album album) {
		currentAlbum = albums.get(albums.indexOf(album));
	}

	/**
	 * @return current album selected
	 */
	public static Album getCurrentAlbum() {
		return currentAlbum;
	}

	public static void setSelectedPhoto(Photo photo){
		selectedPhoto = currentAlbum.getAlbum().get(currentAlbum.getAlbum().indexOf(photo));
	}
	public static Photo getSelectedPhoto(){ return selectedPhoto; }
}
