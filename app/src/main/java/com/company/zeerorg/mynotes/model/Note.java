package com.company.zeerorg.mynotes.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zeerorg on 7/2/17.
 */

@Entity
public class Note {
    @Id
    private long id;

    @NotNull
    private String data;

    @NotNull
    private boolean uploaded;

    @NotNull
    private long timestamp;

    @NotNull
    private boolean updated;  // means if it is different from one in backend repo

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    private String objectId;

    @Generated(hash = 421258113)
    public Note(long id, @NotNull String data, boolean uploaded, long timestamp,
            boolean updated, String objectId) {
        this.id = id;
        this.data = data;
        this.uploaded = uploaded;
        this.timestamp = timestamp;
        this.updated = updated;
        this.objectId = objectId;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public boolean getUploaded() {
        return this.uploaded;
    }

    public boolean getUpdated() {
        return this.updated;
    }
}
