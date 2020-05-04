package com.example.photoapp08;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.example.photoapp08.model.Album;
import com.example.photoapp08.model.AlbumsList;
import com.example.photoapp08.model.Photo;
import com.example.photoapp08.model.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailViewActivity extends AppCompatActivity {
    final Context context = this;
    //TextView textView;
    FloatingActionButton addPhotoButton;
    FloatingActionButton removePhotoButton;
    FloatingActionButton searchAlbumButton;
    //ImageView thumbnailImageView;
    GridView gridView;
    private Photo selectedPhoto;    // used for AlbumsList.setSelectedPhoto()
    private Album currentAlbum = AlbumsList.getCurrentAlbum();
    private ArrayList<Bitmap> bitmapList;

    //ADDED FOR SEARCH FUNCTIONALITY
    Spinner searchTypeSpinner;
    EditText searchEditText;
    GridView searchResult;
    private ArrayList<Bitmap> searchResultBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_view);
        addPhotoButton = findViewById(R.id.addPhotoButton);
        removePhotoButton = findViewById(R.id.removePhotoButton);
        searchAlbumButton = findViewById(R.id.searchAlbumButton);
        gridView = findViewById(R.id.gridView);


        loadGridview();

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.create(ThumbnailViewActivity.this) // Activity or Fragment
                        .start();

            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPhoto = currentAlbum.getAlbum().get(position);
                removePhotoButton.setVisibility(View.VISIBLE);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPhoto = currentAlbum.getAlbum().get(position);
                AlbumsList.setSelectedPhoto(selectedPhoto);
                Intent intent = new Intent(ThumbnailViewActivity.this, ImageViewActivity.class);
                //intent.putExtra("selected_photo_object", selectedPhoto);
                startActivity(intent);
            }
        });
        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Remove Photo");
                alert.setMessage("Are you sure you want to remove this photo?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentAlbum.deletePhoto(selectedPhoto);
                        AlbumsList.writeAlbums(context);
                        removePhotoButton.setVisibility(View.INVISIBLE);
                        loadGridview();
                        Toast.makeText(ThumbnailViewActivity.this, "Photo removed", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
        //ToDO
        //  Search button to search photo by tag
        searchAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a search bar
                View popup = getLayoutInflater().inflate(R.layout.search_bar_popup,null);
                AlertDialog.Builder searchAlbumBuilder = new AlertDialog.Builder(context);
                searchAlbumBuilder.setTitle("Search By Tag");
                searchTypeSpinner = popup.findViewById(R.id.searchTypeSpinner);
                searchEditText = popup.findViewById(R.id.searchEditText);
                searchAlbumBuilder.setView(popup);

                searchAlbumBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(searchEditText.getText().toString().trim().isEmpty()){
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Can not process request");
                            alert.setMessage("You must enter a tag value");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                            return;
                        }
                        String tagName = searchTypeSpinner.getSelectedItem().toString();

                        String tagValue = searchEditText.getText().toString().trim();

                        final ArrayList<Photo> result = new ArrayList<>();

                        for(Photo temp : currentAlbum.getAlbum()) {

                            //get the tags from temp photo
                            ArrayList<Tag> tempTags = temp.getTags();

                            //search based on 3 cases
                            switch (tagName) {
                                case "Person":
                                    for (Tag tempTag : tempTags) {
                                        if (tempTag.getName().trim().equals("Person") && tempTag.getValue().toLowerCase().contains(tagValue.toLowerCase())) {
                                            result.add(temp);
                                        }
                                    }
                                    break;
                                case "Location":
                                    for (Tag tempTag : tempTags) {
                                        if (tempTag.getName().trim().equals("Location") && tempTag.getValue().toLowerCase().contains(tagValue.toLowerCase())) {
                                            result.add(temp);
                                        }
                                    }
                                    break;
                                case "Person and Location":
                                    for (Tag tempTag : tempTags) {
                                        if (tempTag.getValue().trim().toLowerCase().contains(tagValue.toLowerCase())) {
                                            if(!result.contains(temp))
                                                result.add(temp);
                                        }
                                    }
                                    break;
                            }
                        }

                        //load images into the GridView...
                        searchResultBitmap = new ArrayList<>();
                        for(Photo photo : result) {
                            searchResultBitmap.add((imagePathToBitmap(photo.getPath())));
                        }
                        gridView.setAdapter(new ImageAdapter(context, searchResultBitmap));
                        //change the listeners to get the selected photo from search result list instead of the whole album
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                selectedPhoto = result.get(position);
                                AlbumsList.setSelectedPhoto(selectedPhoto);
                                Intent intent = new Intent(ThumbnailViewActivity.this, ImageViewActivity.class);
                                startActivity(intent);
                            }
                        });
                        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                selectedPhoto = result.get(position);
                                removePhotoButton.setVisibility(View.VISIBLE);
                                return true;
                            }
                        });
                    }
                });
                searchAlbumBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                searchAlbumBuilder.show();
            }
        });

    }
    private Bitmap imagePathToBitmap(String imagepath) {
        Bitmap result = BitmapFactory.decodeFile(imagepath);
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            for(Image image: images){
                File imgFile = new  File(image.getPath());
                currentAlbum.addPhoto(new Photo(imgFile));
                AlbumsList.writeAlbums(context);
                loadGridview();
                Toast.makeText(ThumbnailViewActivity.this, "Photo added", Toast.LENGTH_LONG).show();
            }

            // or get a single image only
            //Image image = ImagePicker.getFirstImageOrNull(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void loadGridview(){
        bitmapList = new ArrayList<Bitmap>();
        for(Photo photo : currentAlbum.getAlbum()){
            bitmapList.add(imagePathToBitmap(photo.getPath()));
        }
        gridView.setAdapter(new ImageAdapter(context, bitmapList));
    }
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //refresh procedure here
        Intent intent = getIntent();
        finish();
        overridePendingTransition( 0, 0);
        startActivity(intent);
        overridePendingTransition( 0, 0);
    }
}
