package com.example.whensunset.pictureprocessinggraduationdesign.impl;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.whensunset.pictureprocessinggraduationdesign.PictureProcessingApplication;
import com.example.whensunset.pictureprocessinggraduationdesign.base.IImageUriFetch;
import com.example.whensunset.pictureprocessinggraduationdesign.base.util.MyLog;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by whensunset on 2018/3/2.
 */

public class SystemImageUriFetch implements IImageUriFetch {
    public static final String TAG = "何时夕:SystemImageUriFetch";

    private static SystemImageUriFetch mSystemImageUriFetch;

    private Map<String, List<ImageInfo>> mImageInfoMap = new LinkedHashMap<>();
    private final List<ImageInfo> mImageInfoList = new ArrayList<>();

    public static SystemImageUriFetch getInstance() {
        if (mSystemImageUriFetch == null) {
            synchronized (SystemImageUriFetch.class) {
                if (mSystemImageUriFetch == null) {
                    mSystemImageUriFetch = new SystemImageUriFetch();
                }
            }
        }
        return mSystemImageUriFetch;
    }

    private SystemImageUriFetch() {
        freshImageInfo();
    }

    @Override
    public List<String> getALlImageUriListFromTag(Object tag) {
        List<ImageInfo> imageInfoList = mImageInfoMap.get(tag);
        MyLog.d(TAG, "getALlImageUriListFromTag", "状态:tag:imageInfoList:", "根据tag获取全部的uri" , tag , imageInfoList);
        return getRangeImageUriListFromList(imageInfoList , 0 , imageInfoList == null ? 0 : imageInfoList.size());
    }

    @Override
    public List<String> getRangeImageUriListFromTag(Object tag, int start, int end) {
        List<ImageInfo> imageInfoList = mImageInfoMap.get(tag);
        MyLog.d(TAG, "getRangeImageUriListFromTag", "状态:tag:start:end:", "根据tag获取部分uri" , tag , start , end);
        return getRangeImageUriListFromList(imageInfoList, start, end);
    }

    @Override
    public List<Object> getAllTag() {
        return new ArrayList<>(mImageInfoMap.keySet());
    }

    @Override
    public List<String> getAllImageUriList() {
        return getRangeImageUriList(0, mImageInfoList.size());
    }

    @Override
    public List<String> getRangeImageUriList(int start, int end) {
        return getRangeImageUriListFromList(mImageInfoList, start, end);

    }

    private List<String> getRangeImageUriListFromList(List<ImageInfo> imageInfoList, int start, int end) {
        List<String> imageUriList = new ArrayList<>();
        Flowable.fromIterable(imageInfoList)
                .take(end)
                .takeLast(end - start)
                .map(imageInfo -> imageInfo.uri)
                .subscribe(imageUriList::add);
        return imageUriList;
    }

    @Override
    public void freshImageInfo() {
        String[] projection = {MediaStore.Images.ImageColumns.DATA
                , MediaStore.Images.ImageColumns.SIZE
                , MediaStore.Images.ImageColumns.DISPLAY_NAME
                , MediaStore.Images.ImageColumns.TITLE
                , MediaStore.Images.ImageColumns.DATE_ADDED
                , MediaStore.Images.ImageColumns.DATE_MODIFIED
                , MediaStore.Images.ImageColumns.WIDTH
                , MediaStore.Images.ImageColumns.HEIGHT
                , MediaStore.Images.ImageColumns._ID};
        Cursor cursor = PictureProcessingApplication
                .getAppContext()
                .getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (cursor == null) {
            MyLog.d(TAG, "freshImageInfo", "状态:" , "cursor为null，数据清空");
            mImageInfoList.clear();
            return;
        }

        cursor.moveToFirst();
        mImageInfoMap.put("所有图片", mImageInfoList);
        while (cursor.moveToNext()) {
            ImageInfo imageInfo = new ImageInfo(cursor);
            String imageName = imageInfo.data.substring(imageInfo.data.lastIndexOf("/") + 1);
            String imageDirectory = imageInfo.data.substring(0, imageInfo.data.lastIndexOf("/"));
            imageInfo.setImageName(imageName);
            imageInfo.setImageDirectory(imageDirectory);
            mImageInfoList.add(imageInfo);

            if (mImageInfoMap.containsKey(imageDirectory)) {
                mImageInfoMap.get(imageDirectory).add(imageInfo);
            } else {
                List<ImageInfo> imageInfoList = new ArrayList<>();
                imageInfoList.add(imageInfo);
                mImageInfoMap.put(imageDirectory, imageInfoList);
            }

            MyLog.d(TAG, "freshImageInfo", "状态:imageInfo:", "获取图片信息完毕" , imageInfo.toString());
        }

        cursor.close();
    }

    public Map<String, List<ImageInfo>> getImageInfoMap() {
        return mImageInfoMap;
    }

    public List<ImageInfo> getImageInfoList() {
        return mImageInfoList;
    }

    public static class ImageInfo {
        public String data;
        public String size;
        public String displayName;
        public String title;
        public String dataAdded;
        public String dateModified;
        public String width;
        public String height;
        public String id;
        public String uri;
        public String imageName;
        public String imageDirectory;

        public ImageInfo(Cursor cursor) {
            data = cursor.getString(0);
            size = cursor.getString(1);
            displayName = cursor.getString(2);
            title = cursor.getString(3);
            dataAdded = cursor.getString(4);
            dateModified = cursor.getString(5);
            width = cursor.getString(6);
            height = cursor.getString(7);
            id = cursor.getString(8);
            uri = Uri.fromFile(new File(data)).toString();
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDataAdded() {
            return dataAdded;
        }

        public void setDataAdded(String dataAdded) {
            this.dataAdded = dataAdded;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public String getImageDirectory() {
            return imageDirectory;
        }

        public void setImageDirectory(String imageDirectory) {
            this.imageDirectory = imageDirectory;
        }

        @Override
        public String toString() {
            return "ImageInfo{" +
                    "data='" + data + '\'' +
                    ", size='" + size + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", title='" + title + '\'' +
                    ", dataAdded='" + dataAdded + '\'' +
                    ", dateModified='" + dateModified + '\'' +
                    ", width='" + width + '\'' +
                    ", height='" + height + '\'' +
                    ", id='" + id + '\'' +
                    ", uri='" + uri + '\'' +
                    ", imageName='" + imageName + '\'' +
                    ", imageDirectory='" + imageDirectory + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ImageInfo)) return false;

            ImageInfo imageInfo = (ImageInfo) o;

            if (data != null ? !data.equals(imageInfo.data) : imageInfo.data != null) return false;
            if (size != null ? !size.equals(imageInfo.size) : imageInfo.size != null) return false;
            if (displayName != null ? !displayName.equals(imageInfo.displayName) : imageInfo.displayName != null)
                return false;
            if (title != null ? !title.equals(imageInfo.title) : imageInfo.title != null)
                return false;
            if (dataAdded != null ? !dataAdded.equals(imageInfo.dataAdded) : imageInfo.dataAdded != null)
                return false;
            if (dateModified != null ? !dateModified.equals(imageInfo.dateModified) : imageInfo.dateModified != null)
                return false;
            if (width != null ? !width.equals(imageInfo.width) : imageInfo.width != null)
                return false;
            if (height != null ? !height.equals(imageInfo.height) : imageInfo.height != null)
                return false;
            if (id != null ? !id.equals(imageInfo.id) : imageInfo.id != null) return false;
            if (uri != null ? !uri.equals(imageInfo.uri) : imageInfo.uri != null) return false;
            if (imageName != null ? !imageName.equals(imageInfo.imageName) : imageInfo.imageName != null)
                return false;
            return imageDirectory != null ? imageDirectory.equals(imageInfo.imageDirectory) : imageInfo.imageDirectory == null;
        }

        @Override
        public int hashCode() {
            int result = data != null ? data.hashCode() : 0;
            result = 31 * result + (size != null ? size.hashCode() : 0);
            result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
            result = 31 * result + (title != null ? title.hashCode() : 0);
            result = 31 * result + (dataAdded != null ? dataAdded.hashCode() : 0);
            result = 31 * result + (dateModified != null ? dateModified.hashCode() : 0);
            result = 31 * result + (width != null ? width.hashCode() : 0);
            result = 31 * result + (height != null ? height.hashCode() : 0);
            result = 31 * result + (id != null ? id.hashCode() : 0);
            result = 31 * result + (uri != null ? uri.hashCode() : 0);
            result = 31 * result + (imageName != null ? imageName.hashCode() : 0);
            result = 31 * result + (imageDirectory != null ? imageDirectory.hashCode() : 0);
            return result;
        }
    }

}
