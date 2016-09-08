package com.example.szymon.messor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class Settings_Fragment extends android.app.Fragment {

public InterfaceDataCommunicator interfaceDataCommunicator;

    public interface InterfaceDataCommunicator {
        public void updateData(String ip,int port,int flaga,float x,float y, float z, float alpha, float beta, float gamma, float speed, int id);
    }

    ///SETTINGS ID =1;
    int id =1;
    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;
    Button AHRS,MESSOR;
    String Ip;
    int port;

    View myView;


    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach(activity);
        try{
            interfaceDataCommunicator = (InterfaceDataCommunicator) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.settings_fragment, container, false);
        editTextAddress = (EditText) myView.findViewById(R.id.address);
        editTextPort = (EditText) myView.findViewById(R.id.port);
        buttonConnect = (Button) myView.findViewById(R.id.connect);
        buttonClear = (Button) myView.findViewById(R.id.clear);
        AHRS=(Button)myView.findViewById(R.id.ahrsbtn);
        MESSOR=(Button)myView.findViewById(R.id.messorbtn);

        AHRS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                interfaceDataCommunicator.updateData(Ip, port, 1, 0, 0, 0, 0, 0, 0, (float) 0.5, 6);


            }
        });

        MESSOR.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                interfaceDataCommunicator.updateData(Ip, port, 1, 0, 0, 0, 0, 0, 0, (float) 0.5, 7);

            }
        });
        textResponse = (TextView) myView.findViewById(R.id.response);


        buttonConnect.setOnClickListener(buttonConnectOnClickListener);


        buttonClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                editTextAddress.setText("");
                editTextPort.setText("");
            }
        });




        return myView;
    }
        View.OnClickListener buttonConnectOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

    Ip = editTextAddress.getText().toString();
    port = Integer.parseInt(editTextPort.getText().toString());

            interfaceDataCommunicator.updateData(Ip, port, 1, 0, 0, 0, 0, 0, 0, (float) 0.5, id);


        }
    };


    public void setResponse(String response)
    {
        textResponse.setText(response);
    }



};


