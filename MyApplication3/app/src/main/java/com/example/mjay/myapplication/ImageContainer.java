package com.example.mjay.myapplication;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by MJay on 4/4/2017.
 */

public class ImageContainer
{
    private static Bitmap bmp;
    private static File f;
    private static boolean img_sel;

    public static void setBitmapImage(Bitmap bmp)
    {
        ImageContainer.bmp=bmp;
    }

    public static Bitmap getBitmapImage()
    {
        return ImageContainer.bmp;
    }

    public static void setBoolValue(boolean val)
    {
        ImageContainer.img_sel=val;
    }
    public static boolean isImageSelected()
    {
        return ImageContainer.img_sel;
    }

    public static void setFile(File ff)
    {
        f = ff;
    }

    public static File getFile()
    {
        return f;
    }
}
