package com.udacity.lorianns.popularmovie2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lorianns on 8/6/16.
 */
public class VideoEntity implements Parcelable {

    final String _ID = "id";
    final String _ISO = "iso_639_1";
    final String _ISO_2 = "iso_3166_1";
    final String _KEY = "key";
    final String _NAME = "name";
    final String _SITE = "site";
    final String _SIZE = "size";
    final String _TYPE = "type";

    private String id;
    private String iso;
    private String iso2;
    private String key;
    private String name;
    private String site;
    private String type;

    private Integer size;


    public VideoEntity (JSONObject jsonObject) throws JSONException {
        id = jsonObject.getString(_ID);
        iso = jsonObject.getString(_ISO);
        iso2 = jsonObject.getString(_ISO_2);
        key = jsonObject.getString(_KEY);
        name = jsonObject.getString(_NAME);
        site = jsonObject.getString(_SITE);
        type = jsonObject.getString(_TYPE);

        size = jsonObject.getInt(_SIZE);
    }

    public VideoEntity(Parcel parcel) {
        id = parcel.readString();
        iso = parcel.readString();
        iso2 = parcel.readString();
        key = parcel.readString();
        name = parcel.readString();
        site = parcel.readString();
        type = parcel.readString();
        size = parcel.readInt();
    }

    public static final Parcelable.Creator<VideoEntity> CREATOR = new Parcelable.Creator<VideoEntity>() {
        @Override
        public VideoEntity createFromParcel(Parcel parcel) {
            return new VideoEntity(parcel);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(iso);
        parcel.writeString(iso2);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeString(type);
        parcel.writeInt(size);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
