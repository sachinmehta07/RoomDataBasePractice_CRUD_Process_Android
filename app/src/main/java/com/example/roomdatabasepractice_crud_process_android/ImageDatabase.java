package com.example.roomdatabasepractice_crud_process_android;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SelectedImage.class}, version = 1)
public abstract class ImageDatabase extends RoomDatabase {
    public abstract SelectedImageDao selectedImageDao();
}