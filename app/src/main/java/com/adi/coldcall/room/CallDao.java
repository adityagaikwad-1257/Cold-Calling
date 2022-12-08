package com.adi.coldcall.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.adi.coldcall.models.Call;

import java.util.List;

@Dao
public interface CallDao {

    @Insert
    void addCallEntry(Call call);

    @Delete
    void deleteCallEntry(Call call);

    @Update
    void updateCallEntry(Call call);

    @Query("select * from calls order by date desc")
    LiveData<List<Call>> getCalls();
}
