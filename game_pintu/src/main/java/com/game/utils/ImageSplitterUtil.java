package com.game.utils;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zdr on 16-3-10.
 * 该类将进行图片的切割，将一张完整的图片切割为List<ImagePiece>集合
 */

public class ImageSplitterUtil {

    /**
     * @param bitmap 完整的图片
     * @param piece  切成piece × piece
     * @return List<ImagePiece> 切片集合
     */
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {

        List<ImagePiece> imagePieces = new ArrayList<>();

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int pieceWidth = Math.min(bitmapWidth, bitmapHeight) / piece;

        for (int i = 0; i < piece; i++) {
            for (int j = 0; j < piece; j++) {
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * piece);

                int x = j * pieceWidth;
                int y = i * pieceWidth;
                imagePiece.setBitmap(Bitmap.createBitmap(bitmap,x,y,pieceWidth,pieceWidth));

                imagePieces.add(imagePiece);
            }
        }
        return imagePieces;
    }
}
