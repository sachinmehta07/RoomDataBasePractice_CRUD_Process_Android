package com.example.roomdatabasepractice_crud_process_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    private static final int REQUEST_GALLERY = 101;

    private Button btnOpenGallery;
    private Button btnStoreImages;
    private RecyclerView rvSelectedImages;
    private List<SelectedImage> selectedImages;
    private SelectedImagesAdapter selectedImagesAdapter;
    private ImageDatabase imageDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnStoreImages = findViewById(R.id.btnStoreImages);
        rvSelectedImages = findViewById(R.id.rvSelectedImages);
        rvSelectedImages.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Room Database
        imageDatabase = Room.databaseBuilder(getApplicationContext(), ImageDatabase.class, "image-db").build();

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnStoreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeImages();
            }
        });
    }

    private void openGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), REQUEST_GALLERY);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            startActivityForResult(intent, REQUEST_GALLERY);
//        }
    }

    private void storeImages() {
        // Store selected images in the Room Database
        // Store selected images in the Room Database
        new Thread(new Runnable() {
            @Override
            public void run() {
                SelectedImage[] imagesArray = selectedImages.toArray(new SelectedImage[selectedImages.size()]);
                imageDatabase.selectedImageDao().insert(imagesArray);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Images stored in the database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void fetchImagesFromDatabase() {
        // Fetch selected images from the Room Database
        new Thread(new Runnable() {
            @Override
            public void run() {
                selectedImages = imageDatabase.selectedImageDao().getAllSelectedImages();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (selectedImages != null && selectedImages.size() > 0) {
                            selectedImagesAdapter = new SelectedImagesAdapter(MainActivity.this, selectedImages);
                            rvSelectedImages.setAdapter(selectedImagesAdapter);
                            btnStoreImages.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(MainActivity.this, "No images found", Toast.LENGTH_SHORT).show();
                            btnStoreImages.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                selectedImages = new ArrayList<>();

                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imagePath = getRealPathFromURI(imageUri);
                    SelectedImage selectedImage = new SelectedImage(imagePath);
                    selectedImages.add(selectedImage);
                }

                selectedImagesAdapter = new SelectedImagesAdapter(this, selectedImages);
                rvSelectedImages.setAdapter(selectedImagesAdapter);
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }
}