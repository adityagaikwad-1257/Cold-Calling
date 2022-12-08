package com.adi.coldcall.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "Calls")
public class Call {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final String _ID;
    private final String phoneNumber;
    private final long date;
    private final long duration;

    private String remark;
    private String status;

    /*
        Different values for @status
        There could be more statuses such as POSITIVE_RESPONSE, NEGATIVE_RESPONSE, etc
     */
    public static final String STATUS_CALL_ANSWERED = "Call answered";
    public static final String STATUS_CALL_NOT_ANSWERED = "Call not answered";

    public Call(String _ID, String phoneNumber, long date, long duration) {
        this._ID = _ID;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.duration = duration;
    }

    @NonNull
    @Override
    public String toString() {
        return "Call{" +
                "_ID='" + _ID + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", remark='" + remark + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Call)) return false;
        Call call = (Call) o;
        return id == call.id && date == call.date && duration == call.duration && _ID.equals(call._ID) && Objects.equals(phoneNumber, call.phoneNumber) && Objects.equals(remark, call.remark) && status.equals(call.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, _ID, phoneNumber, date, duration, remark, status);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_ID() {
        return _ID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getDate() {
        return date;
    }

    public long getDuration() {
        return duration;
    }
}
