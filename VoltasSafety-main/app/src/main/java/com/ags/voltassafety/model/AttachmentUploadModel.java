package com.ags.voltassafety.model;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AttachmentUploadModel {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("Uri")
    @Expose
    private Uri uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

}
