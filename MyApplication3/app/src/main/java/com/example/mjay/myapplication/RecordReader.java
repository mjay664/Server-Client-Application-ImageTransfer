package com.example.mjay.myapplication;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by MJay on 4/12/2017.
 */

public class RecordReader {
    public static File f_sender;
    public static File f_reciever;
    public static FileReader sender_file_reader;
    public static FileReader reciever_file_reader;
    public static String directory;

    public static void setDirectory(String path)
    {
        directory = path;
    }

    public static void openRecordFiles() throws IOException
    {
        f_reciever = new File(directory, "#~reciever.record");
        f_sender = new File(directory, "#~sender.record");

        sender_file_reader = new FileReader(f_sender);
        reciever_file_reader = new FileReader(f_reciever);
    }

    public static FileReader getSenderRecordFile()
    {
        return sender_file_reader;
    }

    public static FileReader getRecieverRecordFile()
    {
        return reciever_file_reader;
    }
}
