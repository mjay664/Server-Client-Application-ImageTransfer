package com.example.mjay.myapplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by MJay on 4/12/2017.
 */

public class RecordCreator {
    private static File f_sender;
    private static File f_reciever;
    public static FileWriter sender_file_writer;
    public static FileWriter reciever_file_writer;
    private static String directory;

    public static void setDirectory(String path){ RecordCreator.directory = path; }


    public static void setSenderRecord(String record)
    {

        try {
            RecordCreator.f_sender = new File(RecordCreator.directory, "#~sender.record");
            RecordCreator.sender_file_writer = new FileWriter(f_sender, true);
            sender_file_writer.append("\n" + record);
            sender_file_writer.close();
        }
        catch (IOException e)
        {
            e.getMessage();
        }
    }

    public static void setRecieverRecord(String record)
    {
        try {
            RecordCreator.f_reciever = new File(RecordCreator.directory, "#~reciever.record");
            RecordCreator.reciever_file_writer = new FileWriter(f_reciever, true);
            reciever_file_writer.append("\n" + record);
            reciever_file_writer.close();
        }
        catch (IOException e)
        {
            e.getMessage();
        }
    }
}
