package com.example.roomdatabasepractice_crud_process_android;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface SelectedImageDao {

    @Insert
    void insert(SelectedImage... selectedImages);

    @Query("SELECT * FROM selected_images")
    List<SelectedImage> getAllSelectedImages();
}
