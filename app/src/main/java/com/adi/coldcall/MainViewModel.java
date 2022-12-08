package com.adi.coldcall;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adi.coldcall.models.Call;
import com.adi.coldcall.room.CallDao;

import java.util.List;

public class MainViewModel extends ViewModel {
    /*
        holds the value of phone number last called
     */
    private String lastCallNumber;

    private final CallDao callDao;

    public MainViewModel(){
        this.callDao = ColdCall.getDatabase().callDao();
    }

    public String getLastCallNumber() {
        return lastCallNumber;
    }

    public void setLastCallNumber(String lastCallNumber) {
        this.lastCallNumber = lastCallNumber;
    }

    public void insetCallEntry(Call call){
        new Thread(() -> callDao.addCallEntry(call)).start();
    }

    public void updateCallEntry(Call call){
        new Thread(() -> callDao.updateCallEntry(call)).start();
    }

    public void deleteCallEntry(Call call){
        new Thread(() -> callDao.deleteCallEntry(call)).start();
    }

    /*
        returns LiveData of List of Calls retrieved from the database
        in descending order of @date
     */
    public LiveData<List<Call>> getCalls(){
        return callDao.getCalls();
    }
}
