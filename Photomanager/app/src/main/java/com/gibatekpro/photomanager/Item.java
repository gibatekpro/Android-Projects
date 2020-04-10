package com.gibatekpro.photomanager;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String imageFileName, imageFolder, absolutePathOfImage;
    private boolean isHidden, canWrite, duplicate,selected=false;
    private long imageSize,lastModified;
    private int type;
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_AD = 1;

    public static final int DELETED = 0;
    public static final int READ = 1;
    public static final int UNREAD = 2;
    public static final int FAVORITE = 3;
    public static final int NONE = 4;

    public Item() {
    }

    public Item(String imageFileName, String imageFolder, String absolutePathOfImage, long imageSize,long lastModified, boolean isHidden, boolean canWrite, int type) {
        this.imageFileName = imageFileName;
        this.imageFolder = imageFolder;
        this.absolutePathOfImage = absolutePathOfImage;
        this.imageSize = imageSize;
        this.lastModified = lastModified;
        this.isHidden = isHidden;
        this.canWrite = canWrite;
        this.type = type;
    }

    public Item(int type) {
        this.imageFileName = "";
        this.imageFolder = "";
        this.absolutePathOfImage = "";
        this.isHidden =false;
        this.canWrite = false;
        this.imageSize = 0;
        this.type = type;
    }


    public Item(String imageFileName, String imageFolder, String absolutePathOfImage, long imageSize,long lastModified, boolean isHidden, boolean canWrite,boolean duplicate) {
        this.imageFileName = imageFileName;
        this.imageFolder = imageFolder;
        this.absolutePathOfImage = absolutePathOfImage;
        this.isHidden = isHidden;
        this.canWrite = canWrite;
        this.imageSize = imageSize;
        this.lastModified = lastModified;
        this.duplicate=duplicate;
        this.type = TYPE_ITEM;
    }

    protected Item(Parcel in) {
        this.imageFileName = in.readString();
        this.imageFolder = in.readString();
        this.absolutePathOfImage = in.readString();
        this.isHidden = in.readByte() != 0;
        this.canWrite = in.readByte() != 0;
        this.imageSize = in.readLong();
        this.lastModified = in.readLong();
        this.type = in.readInt();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getImageFolder() {
        return imageFolder;
    }

    public void setImageFolder(String imageFolder) {
        this.imageFolder = imageFolder;
    }

    public String getAbsolutePathOfImage() {
        return absolutePathOfImage;
    }

    public void setAbsolutePathOfImage(String absolutePathOfImage) {
        this.absolutePathOfImage = absolutePathOfImage;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public long getImageSize() {
        return imageSize;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setImageSize(long imageSize) {
        this.imageSize = imageSize;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageFileName);
        parcel.writeString(imageFolder);
        parcel.writeString(absolutePathOfImage);
        parcel.writeByte((byte) (isHidden ? 1 : 0));
        parcel.writeByte((byte) (canWrite ? 1 : 0));
        parcel.writeLong(imageSize);
        parcel.writeLong(lastModified);
        parcel.writeInt(type);
    }
}
