package com.example.photoapp08;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photoapp08.model.Album;
import com.example.photoapp08.model.AlbumsList;
import com.example.photoapp08.model.Photo;
import com.example.photoapp08.model.Tag;

import java.util.ArrayList;

public class ImageViewActivity extends AppCompatActivity {
    final Context context = this;
    ImageView bigImageView;
    TextView captionTextView;
    ListView tagListView;
    FloatingActionButton parseLeft;
    FloatingActionButton parseRight;
    Button addTagButton;
    Button deleteTagButton;
    Button moveButton;

    Spinner tagTypeSpinner;
    EditText tagValueEditText;
    private Photo selectedPhoto;
    private Tag selectedTag;
    private Album albumToMoveTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        bigImageView = findViewById(R.id.bigImageView);
        captionTextView = findViewById(R.id.captionTextView);
        tagListView = findViewById(R.id.tagListView);
        parseLeft = findViewById(R.id.parseLeft);
        parseRight = findViewById(R.id.parseRight);
        addTagButton = findViewById(R.id.addTagButton);
        deleteTagButton = findViewById(R.id.deleteTagButton);
        moveButton = findViewById(R.id.moveButton);

        //selectedPhoto = (Photo) getIntent().getSerializableExtra("selected_photo_object");
        selectedPhoto = AlbumsList.getSelectedPhoto();

        Bitmap bitmap = BitmapFactory.decodeFile(selectedPhoto.getPath());
        bigImageView.setImageBitmap(bitmap);

        loadCaption();

        loadTagsListview();

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popup = getLayoutInflater().inflate(R.layout.add_tag_popup,null);
                AlertDialog.Builder addTagBuilder = new AlertDialog.Builder(context);
                addTagBuilder.setTitle("Add Tag");
                tagTypeSpinner = popup.findViewById(R.id.tagTypeSpinner);
                tagValueEditText = popup.findViewById(R.id.tagValueEditText);
                addTagBuilder.setView(popup);

                addTagBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(tagValueEditText.getText().toString().trim().isEmpty()){
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
                        selectedPhoto.addTag(new Tag(tagTypeSpinner.getSelectedItem().toString(),
                                tagValueEditText.getText().toString().trim()));
                        AlbumsList.writeAlbums(context);
                        loadTagsListview();
                        Toast.makeText(ImageViewActivity.this, "Tag added",Toast.LENGTH_LONG).show();

                    }
                });
                addTagBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                addTagBuilder.show();
            }
        });

        tagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedTag = selectedPhoto.getTags().get(position);
            }
        });

        deleteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTag == null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Can not process request");
                    alert.setMessage("You must select a tag first");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alert.show();
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Tag ");
                alert.setMessage("Are you sure you want to delete this tag?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPhoto.removeTag(selectedTag);
                        AlbumsList.writeAlbums(context);
                        loadTagsListview();
                        Toast.makeText(ImageViewActivity.this, "Tag deleted",Toast.LENGTH_LONG).show();
                        selectedTag = null;//set to null so it waits for next tag to be selected
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
        parseRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPhotoIndex = AlbumsList.getCurrentAlbum().getAlbum().indexOf(selectedPhoto);
                if(selectedPhotoIndex != AlbumsList.getCurrentAlbum().getAlbum().size()-1){
                    selectedPhoto = AlbumsList.getCurrentAlbum().getAlbum().get(selectedPhotoIndex+1);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedPhoto.getPath());
                    bigImageView.setImageBitmap(bitmap);
                    loadCaption();
                    loadTagsListview();
                } else if (selectedPhotoIndex == AlbumsList.getCurrentAlbum().getAlbum().size()-1){
                    Toast.makeText(ImageViewActivity.this, "No more photos in this album", Toast.LENGTH_LONG).show();
                }
            }
        });
        parseLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPhotoIndex = AlbumsList.getCurrentAlbum().getAlbum().indexOf(selectedPhoto);
                if(selectedPhotoIndex != 0){
                    selectedPhoto = AlbumsList.getCurrentAlbum().getAlbum().get(selectedPhotoIndex-1);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedPhoto.getPath());
                    bigImageView.setImageBitmap(bitmap);
                    loadCaption();
                    loadTagsListview();
                }
            }
        });
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Move Photo");
                builder.setMessage("Select an album to move this photo to:");
                ArrayList<String> temp = new ArrayList<String>();
                final ArrayList<Album> moveAlbumList = new ArrayList<Album>();
                for (Album album : AlbumsList.albums){
                    if(album.getAlbumName().equalsIgnoreCase(AlbumsList.currentAlbum.getAlbumName()))
                        continue;
                    temp.add(album.getAlbumName());
                    moveAlbumList.add(album);
                }
                //temp.remove(AlbumsList.currentAlbum.getAlbumName());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                        R.layout.activity_albumslistview,
                        temp);
                final ListView listView = new ListView(context);
                listView.setAdapter(arrayAdapter);
                listView.setPadding(30, 16, 30, 16);
                listView.setSelector(R.color.ef_grey);
                builder.setView(listView);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        albumToMoveTo = moveAlbumList.get(position);
                    }
                });

                builder.setPositiveButton("Move", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(albumToMoveTo==null){
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Can not process request");
                            alert.setMessage("You must select an album first");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                            return;
                        }
                        AlbumsList.getCurrentAlbum().getAlbum().remove(selectedPhoto);
                        albumToMoveTo.addPhoto(selectedPhoto);
                        AlbumsList.writeAlbums(context);
                        onBackPressed();//go back
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void loadCaption() {
        captionTextView.setText("Caption: "+selectedPhoto.getCaption());
    }

    public void loadTagsListview(){
        //Set up tags
        ArrayList<String> temp = new ArrayList<String>();
        for(Tag tag : selectedPhoto.getTags()){
            temp.add(tag.getName() + ": " + tag.getValue());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_albumslistview,
                temp);
        tagListView.setAdapter(arrayAdapter);
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
