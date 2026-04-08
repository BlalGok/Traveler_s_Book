package com.muhammetbilalgok.travelbook.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.muhammetbilalgok.travelbook.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDAO {
    @Insert
    Completable insert(Place place);

    @Delete
    Completable delete(Place place);

    @Query("SELECT * FROM Place")
    Flowable<List<Place>> getAll();
    /*
    @Query("SELECT * FROM Place WHERE name= :nameinput")
    List<Place> getAll(String nameinput);
    */
}
