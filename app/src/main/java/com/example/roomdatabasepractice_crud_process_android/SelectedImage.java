package com.example.roomdatabasepractice_crud_process_android;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "selected_images")
public class SelectedImage {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String imagePath;

    public SelectedImage() {
        // Empty constructor required by Room
    }


    public SelectedImage(String imagePath) {
        this.imagePath = imagePath;
    }

    @Ignore
    public SelectedImage(int id, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
    }

    // Getters and setters for the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}