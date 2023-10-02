package com.example.myapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDAO {
    @Insert
    public void insertContact(Contact contact);
    @Update
    public void updateContact(Contact contact);
    @Delete
    public void deleteContact(Contact contact);
    @Query("SELECT * FROM contact_table")
    public List<Contact> selectAllContact();
}
