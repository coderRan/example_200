package com.game.utils;

import android.graphics.Bitmap;

/**
 * Created by zdr on 16-3-10.
 * 该类是对每个切块的描述
 */
public class ImagePiece {
    /*
    * index ： 每个图片切块的下标
    * bitmap： 指向完整的图片
    * */
    private int index = -1;
    private Bitmap bitmap = null;

    public ImagePiece(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;
    }

    public ImagePiece() {

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "ImagePiece{" +
                "index=" + index +
                ", bitmap=" + bitmap +
                '}';
    }
}
