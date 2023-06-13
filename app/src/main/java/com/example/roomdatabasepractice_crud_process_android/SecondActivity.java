package com.example.roomdatabasepractice_crud_process_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class SecondActivity extends AppCompatActivity {
    private RecyclerView rvStoredImages;
    private List<SelectedImage> storedImages;
    private SelectedImagesAdapter selectedImagesAdapter;
    private ImageDatabase imageDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        rvStoredImages = findViewById(R.id.rvStoredImages);
        rvStoredImages.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Room Database
        imageDatabase = Room.databaseBuilder(getApplicationContext(), ImageDatabase.class, "image-db").build();

        fetchImagesFromDatabase();
    }

    private void fetchImagesFromDatabase() {
        // Fetch selected images from the Room Database
        new Thread(new Runnable() {
            @Override
            public void run() {
                storedImages = imageDatabase.selectedImageDao().getAllSelectedImages();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (storedImages != null && storedImages.size() > 0) {
                            selectedImagesAdapter = new SelectedImagesAdapter(SecondActivity.this, storedImages);
                            rvStoredImages.setAdapter(selectedImagesAdapter);
                        } else {
                            // Handle no images found
                        }
                    }
                });
            }
        }).start();
    }
}