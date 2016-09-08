package com.example.szymon.messor;

import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Properties;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Settings_Fragment.InterfaceDataCommunicator{
    FragmentManager fragmentManager = getFragmentManager();
NavigationView navigationView = null;
Toolbar toolbar = null;

    String dstAddress;
    int dstPort;
    byte[] data;
    byte[] id;
    byte[] dane;
    byte[] inputdata;
    Bundle bundleMainScreen;
    Bundle bundleCrawl;
    Bundle bundleAcc;
    Bundle bundleManual;
    Bundle bundleRobotState;
    Bundle bundleSettings;

    MainScreen MainScreen;
    Crawl Crawl;
    Accelerometr Accelerometr;
    ManualControll ManualControll;
    RobotState RobotState;
    Settings_Fragment Settings;

    String response;
    int dstport;
    float response_data0;
    float response_data1;
    float response_data2;
    float response_data3;
    float response_data4;
    float response_data5;
    float response_data6;
    float response_data7;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inputdata = new  byte[32];
        init();
        dstAddress="10.0.0.1";
        dstport=2426;
        MainScreen = new MainScreen();
        fragmentManager.beginTransaction().replace(R.id.content_frame, MainScreen).commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.MainScreen) {

            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainScreen()).commit();

        } else if (id == R.id.ManualControll) {

            ManualControll = new ManualControll();
            bundleManual.putString("ip",dstAddress);
            bundleManual.putInt("port",dstPort);
            ManualControll.setArguments(bundleManual);
            fragmentManager.beginTransaction().replace(R.id.content_frame, ManualControll).commit();

        } else if (id == R.id.CrawlControll) {

            Crawl = new Crawl();
            bundleCrawl.putString("ip",dstAddress);
            bundleCrawl.putInt("port",dstPort);
            Crawl.setArguments(bundleCrawl);


            fragmentManager.beginTransaction().replace(R.id.content_frame, Crawl).commit();
        } else if (id == R.id.Accelerometr) {

            Accelerometr = new Accelerometr();
            bundleAcc.putString("ip",dstAddress);
            bundleAcc.putInt("port",dstPort);
            Accelerometr.setArguments(bundleAcc);

            fragmentManager.beginTransaction().replace(R.id.content_frame,Accelerometr).commit();

        } else if (id == R.id.Settings) {
            Settings= new Settings_Fragment();

            fragmentManager.beginTransaction().replace(R.id.content_frame, Settings).commit();



        } else if (id == R.id.robot_state) {
            RobotState = new RobotState();
            bundleRobotState.putString("ip",dstAddress);
            bundleRobotState.putInt("port",dstPort);
            RobotState.setArguments(bundleRobotState);
            fragmentManager.beginTransaction().replace(R.id.content_frame, RobotState).commit();

        }

        else if (id == R.id.CloseApp) {
            System.exit(0);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    void init() {


        bundleMainScreen = new Bundle();
        bundleMainScreen= new Bundle();
        bundleCrawl= new Bundle();
        bundleAcc= new Bundle();
        bundleManual= new Bundle();
        bundleRobotState= new Bundle();
        bundleSettings= new Bundle();

    }

    public byte[] data_to_robot(int flaga,float x,float y, float z, float alpha, float beta, float gamma, float speed )
    {
        data = new  byte[32];
        id = new byte[4];
        dane = new byte[28];

        id=intToBytes(flaga);
        dane=floatTobytes(x,y,z,alpha,beta,gamma,speed);



        for (int i=0;i<data.length;i++)
        {
            if(i<4)
            {
                data[i]=id[3-i];

            }
            else
                data[i]=dane[31-i];

        }

        return data;
    }

    public void data_from_robot(byte inputData[] )
    {

        response_data0 = ByteBuffer.wrap(inputData,0,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data1 = ByteBuffer.wrap(inputData,4,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data2 = ByteBuffer.wrap(inputData,8,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data3 = ByteBuffer.wrap(inputData,12,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data4 = ByteBuffer.wrap(inputData,16,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data5 = ByteBuffer.wrap(inputData,20,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data6 = ByteBuffer.wrap(inputData,24,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        response_data7 = ByteBuffer.wrap(inputData,28,4).order(ByteOrder.LITTLE_ENDIAN).getFloat();

        response="połączono  "+response_data0 + "  " + response_data1+ "  " + response_data2+ "  " + response_data3+ "  " + response_data4+ "  " + response_data5+ "  " + response_data6+ "  " + response_data7;


    }







    public void writeBuffer (ByteBuffer buffer, OutputStream stream) {
        WritableByteChannel channel = Channels.newChannel(stream);

        try {
            channel.write(buffer);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public byte[] intToBytes( final int i ) {
        ByteBuffer intbytes = ByteBuffer.allocate(4);
        intbytes.putInt(i);
        return intbytes.array();
    }

    public byte[] floatTobytes ( final float x,final float y,final float z,final float alpha,final float beta,final float gamma,final float speed)
    {
        ByteBuffer floatbytes = ByteBuffer.allocate(28);

        floatbytes.putFloat(speed);
        floatbytes.putFloat(gamma);
        floatbytes.putFloat(beta);
        floatbytes.putFloat(alpha);
        floatbytes.putFloat(z);
        floatbytes.putFloat(y);
        floatbytes.putFloat(x);


        return floatbytes.array();

    }




    public class MyClientTask extends AsyncTask<Void, Void,Void> {

        MyClientTask(String addr, int port,byte[] data) {
            dstAddress = "10.0.0.1";
            dstPort = 2426;


        }



        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            ByteBuffer buffer2=ByteBuffer.wrap(data);
            try {
                socket = new Socket(dstAddress, dstPort);


                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                writeBuffer(buffer2,dataOutputStream);

                if(dataInputStream==null)
                {
                response="brak odpowiedzi";
                    dataInputStream.close();
                }
                if (dataInputStream != null) {

                    int bytes_read = 0;


                    try {


                        do {
                            bytes_read =dataInputStream.read(inputdata);

                            if(bytes_read!=-1)
                            {
                              data_from_robot(inputdata);


                            }
                        }while (bytes_read==inputdata.length);




                        dataInputStream.close();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

           }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }



    @Override
    public void updateData(String ip,int port,int flaga,float x,float y, float z, float alpha, float beta, float gamma, float speed, int id) {

        dstAddress=ip;
        dstport=port;


        //choice ==1 settings data
        if (id ==1) {



            data = data_to_robot(flaga, x, y, z, alpha, beta, gamma, speed);

            MyClientTask myClientTask = new MyClientTask(dstAddress,dstport,data);
            myClientTask.execute();
             Settings.setResponse(response);


        }
        //id==2 manualcontroll

       if (id ==2) {

            data = data_to_robot(flaga, x, y, z, alpha, beta, gamma, speed);
           MyClientTask myClientTask = new MyClientTask(dstAddress,dstport,data);
           myClientTask.execute();


        }

        //id==3 Accelerometr
        if (id==3)
        {
            data = data_to_robot(flaga, x, y, z, alpha, beta, gamma, speed);
            MyClientTask myClientTask = new MyClientTask(dstAddress,dstport,data);
            myClientTask.execute();

        }

//id==4 RobotState
        if (id==4)
        {
            data = data_to_robot(flaga, x, y, z, alpha, beta, gamma, speed);
            MyClientTask myClientTask = new MyClientTask(dstAddress,dstport,data);
            myClientTask.execute();
            RobotState.sendvalues( response_data0, response_data1, response_data2, response_data3, response_data4, response_data5, response_data6, response_data7);
            RobotState.setResponse(response);

        }

//id==5 Crawl
        if (id==5)
        {
            data = data_to_robot(flaga, x, y, z, alpha, beta, gamma, speed);
            MyClientTask myClientTask = new MyClientTask(dstAddress,dstport,data);
            myClientTask.execute();


        }
        //SSH FOR AHRS
        if(id==6)
        {
            SSH_AHRS ahrs = new SSH_AHRS();
            ahrs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if(id==7)
        {

            SSH_MESSOR messor = new SSH_MESSOR();
            messor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        //zerowanie inputdatastream
        for(int i = 0;i<32;i++)
        {
            inputdata[i]=0;

        }
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



    public class SSH_AHRS extends AsyncTask<Void, Void,Void> {



        @Override
        protected Void doInBackground(Void... arg0) {
            try
            {
                String command = "'/home/szymon/Desktop/Untitled Folder/robot-build/AHRS'";
                String host = "10.0.0.1";
                String user = "robot";
                String password = "messor";

                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, 22);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);;
                session.setPassword(password);
                session.connect();

                Channel channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand(command);
                ((ChannelExec)channel).setPty(true);
                channel.setInputStream(null);
                ((ChannelExec)channel).setErrStream(System.err);

                InputStream input = channel.getInputStream();
                channel.connect();

                System.out.println("Channel Connected to machine " + host + " server with command: " + command );

                try{
                    InputStreamReader inputReader = new InputStreamReader(input);
                    BufferedReader bufferedReader = new BufferedReader(inputReader);
                    String line = null;

                    while((line = bufferedReader.readLine()) != null){
                        System.out.println(line);
                    }
                    bufferedReader.close();
                    inputReader.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }

                channel.disconnect();
                session.disconnect();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }




            return null;
                }





    }


    public class SSH_MESSOR extends AsyncTask<Void, Void,Void> {



        @Override
        protected Void doInBackground(Void... arg0) {
            try
            {
                String command = "'/home/szymon/Desktop/Untitled Folder/robot-build/Messor' ";
                String host = "10.0.0.1";
                String user = "robot";
                String password = "messor";

                JSch jsch = new JSch();
                Session session = jsch.getSession(user, host, 22);
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);;
                session.setPassword(password);
                session.connect();

                Channel channel = session.openChannel("exec");
                ((ChannelExec)channel).setCommand(command);
                ((ChannelExec)channel).setPty(true);
                channel.setInputStream(null);
                ((ChannelExec)channel).setErrStream(System.err);

                InputStream input = channel.getInputStream();
                channel.connect();

                System.out.println("Channel Connected to machine " + host + " server with command: " + command );

                try{
                    InputStreamReader inputReader = new InputStreamReader(input);
                    BufferedReader bufferedReader = new BufferedReader(inputReader);
                    String line = null;

                    while((line = bufferedReader.readLine()) != null){
                        System.out.println(line);
                    }
                    bufferedReader.close();
                    inputReader.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }

                channel.disconnect();
                session.disconnect();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }




            return null;
        }





    }


}





