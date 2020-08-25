import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MJay
 * 03 March 2017 version 1.0
 */

public class Server {
    
    static ServerSocket ss;
    static int tmp = 0;
    static Socket s_user1;
    static boolean flag2 = true;
    static Socket s_user2;
    static String user1, user2;
    static File f;
    static FileWriter fr;
    static boolean u1_bit = true;
    static boolean u2_bit = true;
    static int line_number = 0;
    
    MyThread1 t1 = new MyThread1();
    MyThread2 t2 = new MyThread2();
    static boolean flag = true;
    
    public static void main(String[] args) throws Exception {

        Server obj1=new Server();
        ss=new ServerSocket(8009);    
        
        f = new File("Server_Log.txt");
           if (!f.exists())
               f.createNewFile();
        try (LineNumberReader lnr = new LineNumberReader(new FileReader(f))) {
            while ( (lnr.readLine())!=null)
            {
                line_number+=1;
            }
        }
        if (line_number == 0 )
            line_number+=1;
        else if (line_number > 1){
            line_number/=2;
            line_number+=1;
        }
        
        System.out.println("Host: "+InetAddress.getLocalHost().getHostAddress()+'\n'+"Port: 8009");
        
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
       
        System.out.println("Server Started.");
      
        fr = new FileWriter(f,true);
        fr.append(line_number+". "+ft.format(dNow));
        fr.append("   ::::      ");
        fr.append("Host: "+InetAddress.getLocalHost().getHostAddress()+'\n'+"Port: 8009"+System.lineSeparator());
        fr.close();
        line_number++;
        
        System.out.println("Waiting for users...");
        while(true)
        {
            if (u1_bit)
            {
                s_user1 = ss.accept();
                
                user1 = s_user1.getRemoteSocketAddress().toString();
                user1 = user1.substring(1,user1.length());
                System.out.println(user1+" Connceted.");
               
                u1_bit = false;
            }
            
            if (u2_bit)
            {
                s_user2 = ss.accept();
                user2 = s_user2.getRemoteSocketAddress().toString();
                user2 = user2.substring(1,user2.length());
                System.out.println(user2+" Connected.");
                
                u2_bit = false;
            }
            
            if (s_user1.isConnected() && s_user2.isConnected())
            {
                DataOutputStream dout1 = new DataOutputStream(s_user1.getOutputStream());
                dout1.writeUTF("!@#$a!@n!@ot!@#$");
                
                DataOutputStream dout2 = new DataOutputStream(s_user2.getOutputStream());
                dout2.writeUTF("!@#$a!@n!@ot!@#$");
                
                break;
            }
        }
        
        File f_user = new File("Users_Log.txt");
        if (!f_user.exists())
            f_user.createNewFile();
        try (FileWriter fout = new FileWriter(f_user)) {
            fout.append(line_number+". "+ft.format(dNow));
            fout.append("   ::::      ");
            fout.append(System.lineSeparator());
            fout.append("User 1 :: "+user1);
            fout.append(System.lineSeparator());
            fout.append("User 2 :: "+user2);
            fout.append(System.lineSeparator());
        }
        
        System.out.println("Users are ready to communicate...");
        line_number++;
        obj1.t1.start();
        obj1.t2.start();
     
    }
    public static String intToStr(byte j) 
    {
        String str = "";
        if( j < 0)
        {
            j =(byte)( j*(-1));
            str = "-";
        }
        while(j>0)
        {
            str = str+""+(char)(j%10);
            j = (byte)(j/10);
        }

        return str;
    }

    public static byte strToInt(String str) 
    {
        byte h = 0;
        byte r = 1;
        if (str.contains("-"))
        {
            str = str.substring(1,str.length());
            r = -1;
        }
        char[] c = str.toCharArray();
        for (int i = c.length-1;i>=0; i--)
        {
            h =(byte) (h*10 + (int)c[i]);
        }
        return (byte) (h*r);
    }
    
    class MyThread1 extends Thread implements Runnable
    {
        String str;
	int ff = 0;
        @Override
        public void run()
        {
            try
            {
                
                DataInputStream din = new DataInputStream(s_user1.getInputStream());
                
                DataOutputStream dout = new DataOutputStream(s_user2.getOutputStream());
                
                System.out.println(user1+ " Sending Data...");
                
                while (flag)
                {
                    String srt = din.readUTF();
                    System.out.print(srt+" ");
                    dout.writeUTF(srt);   
                }
            }
            catch(IOException e)
            {
	
                System.out.println("Unexpected Error on user side");
            }
        }
    }
    
    class MyThread2 extends Thread implements Runnable
    {
        int tmp = 0;
        int length = 0;
        @Override
        public void run()
        {
            try
            {
                DataInputStream din = new DataInputStream(s_user2.getInputStream());
                
                DataOutputStream dout = new DataOutputStream(s_user1.getOutputStream());

                System.out.println(user2+" Sending Data...");
                
                while (flag2)
                {
                    String srt = din.readUTF(); 
                    System.out.print(srt+" ");
                    dout.writeUTF(srt);   
                }
            }
            catch(IOException e)
            {
	
                System.out.println("Unexpected Error on user side");
            }            
        }
    }
}

