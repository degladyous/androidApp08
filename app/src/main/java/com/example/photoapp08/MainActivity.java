package com.example.photoapp08;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photoapp08.model.Album;
import com.example.photoapp08.model.AlbumsList;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final Context context = this;

    ListView albumListView;
    Button createAlbumButton;
    Button deleteAlbumButton;
    Button renameAlbumButton;
    TextView pressAndHold;
    private String newAlbumName;
    private String renameAlbumName;
    private String selectedAlbumName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlbumsList.readAlbums(this);
        albumListView = findViewById(R.id.albumListView);
        createAlbumButton = findViewById(R.id.createAlbumButton);
        deleteAlbumButton = findViewById(R.id.deleteAlbumButton);
        renameAlbumButton = findViewById(R.id.renameAlbumButton);
        pressAndHold = findViewById(R.id.pressAndHold);

        //load albums list into list view
        loadAlbumsListview();

        albumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumsList.setCurrentAlbum(AlbumsList.albums.get(position));
                Intent intent = new Intent(MainActivity.this, ThumbnailViewActivity.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, AlbumsList.albums.get(position).getAlbumName(),Toast.LENGTH_SHORT).show();
            }
        });
        albumListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedAlbumName = AlbumsList.albums.get(position).getAlbumName();
                //Toast.makeText(MainActivity.this,"Long Clicked: "+ AlbumsList.albums.get(position).getAlbumName(),Toast.LENGTH_SHORT).show();
                pressAndHold.setVisibility(View.INVISIBLE);
                deleteAlbumButton.setVisibility(View.VISIBLE);
                renameAlbumButton.setVisibility(View.VISIBLE);
                return true;
            }
        });
        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder createAlbumBuilder = new AlertDialog.Builder(context);
                createAlbumBuilder.setTitle("Enter new album name");
                final EditText input = new EditText(context);
                createAlbumBuilder.setView(input);

                // Set up the buttons
                createAlbumBuilder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newAlbumName = input.getText().toString().trim();
                        if(newAlbumName.isEmpty()){
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setTitle("Can not process request");
                            alert.setMessage("You must enter album name");
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                            return;
                        }
                        //Check if album already exists and do not let the add to happen
                        for(Album album : AlbumsList.albums){
                            if (album.getAlbumName().equalsIgnoreCase(newAlbumName)){
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Can not process request");
                                alert.setMessage("Album already exists");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                                return;
                            }
                        }
                        //Album not found, add new album
                        AlbumsList.albums.add(new Album(newAlbumName));
                        AlbumsList.writeAlbums(context);
                        //refresh current activity
                        loadAlbumsListview();
                        Toast.makeText(MainActivity.this, newAlbumName + " created",Toast.LENGTH_LONG).show();
                    }
                });
                createAlbumBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                createAlbumBuilder.show();
            }
        });

        deleteAlbumButton.setOnClickListener(new View.OnClickListener() {
            Album albumToDelete = null;
            @Override
            public void onClick(View v) {
                for(Album album : AlbumsList.albums){
                    if(album.getAlbumName().equalsIgnoreCase(selectedAlbumName)){
                        albumToDelete = album;
                    }
                }
                if(albumToDelete != null){
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Delete Album " + selectedAlbumName);
                    alert.setMessage("Are you sure you want to delete this album?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlbumsList.albums.remove(albumToDelete);
                            AlbumsList.writeAlbums(context);
                            loadAlbumsListview();
                            Toast.makeText(MainActivity.this, selectedAlbumName + " deleted",Toast.LENGTH_LONG).show();

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
            }
        });

        renameAlbumButton.setOnClickListener(new View.OnClickListener() {
            Album albumToRename;
            @Override
            public void onClick(View v) {
                AlertDialog.Builder renameAlbumBuilder = new AlertDialog.Builder(context);
                renameAlbumBuilder.setTitle("Rename Album " + selectedAlbumName);
                renameAlbumBuilder.setMessage("Enter new album name");
                final EditText input = new EditText(context);
                renameAlbumBuilder.setView(input);

                renameAlbumBuilder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        renameAlbumName = input.getText().toString().trim();
                        for(Album album : AlbumsList.albums){
                            if (album.getAlbumName().equalsIgnoreCase(renameAlbumName)){
                                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                                alert.setTitle("Can not process request");
                                alert.setMessage("Album already exists");
                                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alert.show();
                                return;
                            }
                        }
                        //Album not found, we can rename
                        for(Album album : AlbumsList.albums){
                            if(album.getAlbumName().equalsIgnoreCase(selectedAlbumName)){
                                albumToRename = album;
                            }
                        }
                        if(albumToRename != null){
                            albumToRename.setAlbumName(renameAlbumName);
                            AlbumsList.writeAlbums(context);
                            loadAlbumsListview();
                            Toast.makeText(MainActivity.this, selectedAlbumName + " renamed to " + renameAlbumName,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                renameAlbumBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                renameAlbumBuilder.show();
            }
        });
    }
    //used to load and reload albums listview every time create, rename, or delete buttons are clicked
    public void loadAlbumsListview(){
        ArrayList<String> temp = new ArrayList<String>();
        for (Album album : AlbumsList.albums){
            temp.add(album.getAlbumName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.activity_albumslistview,
                temp);
        albumListView.setAdapter(arrayAdapter);
        //And set buttons to invisible
        pressAndHold.setVisibility(View.VISIBLE);
        deleteAlbumButton.setVisibility(View.INVISIBLE);
        renameAlbumButton.setVisibility(View.INVISIBLE);

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