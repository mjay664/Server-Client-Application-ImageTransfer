package com.example.mjay.myapplication;

/**
 * Created by MJay on 4/12/2017.
 */

public class StringsContainer {
    private static String directory_path;

    public static String getDirectoryPath()
    {
        return StringsContainer.directory_path;
    }

    public static void setDirectory_path(String path) { StringsContainer.directory_path = path; }

}
