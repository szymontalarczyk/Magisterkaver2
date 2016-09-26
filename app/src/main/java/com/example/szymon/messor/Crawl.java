package com.example.szymon.messor;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class Crawl extends Fragment implements AdapterView.OnItemSelectedListener {

    public Settings_Fragment.InterfaceDataCommunicator interfaceDataCommunicator;


    //Crawl id = 2;
    static int id = 5;
    View myView;
    Spinner spinner_crawl;
    ArrayAdapter adapter_crawl;
    TextView x, y, z, alfa, beta, gamma, speed;
    FloatingActionButton sendbutton_crawl;
    FloatingActionButton emergency_crawl;

    static final float offset = (float) 0.01;

    boolean was_tripod,was_wave;

    int flaga=0;
    float x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send;
    Button plusx, plusy, plusz, plusalfa, plusbeta, plusgamma, plusspeed;
    Button minusx, minusy, minusz, minusalfa, minusbeta, minusgamma, minusspeed;
    Button zero_values;
    Switch switch_auto;
    String Ip;
    int port;

    RadioButton wave,tripod;
    TextView textdata1, textdata2, textdata3, textdata4, textdata5, textdata6;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            interfaceDataCommunicator = (Settings_Fragment.InterfaceDataCommunicator) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.crawl, container, false);
        spinner_crawl = (Spinner) myView.findViewById(R.id.spinner_crawl);
        adapter_crawl = ArrayAdapter.createFromResource(this.getActivity(), R.array.lista_komend_kroczenie, android.R.layout.simple_list_item_multiple_choice);
        spinner_crawl.setAdapter(adapter_crawl);
        spinner_crawl.setOnItemSelectedListener(this);

        x = (TextView) myView.findViewById(R.id.setX);
        y = (TextView) myView.findViewById(R.id.setY);
        z = (TextView) myView.findViewById(R.id.setZ);
        alfa = (TextView) myView.findViewById(R.id.setAlfa);
        beta = (TextView) myView.findViewById(R.id.setBeta);
        gamma = (TextView) myView.findViewById(R.id.setGamma);
        speed = (TextView) myView.findViewById(R.id.setSpeed);

        plusx = (Button) myView.findViewById(R.id.plusX);
        plusy = (Button) myView.findViewById(R.id.plusY);
        plusz = (Button) myView.findViewById(R.id.plusZ);
        plusalfa = (Button) myView.findViewById(R.id.plusAlfa);
        plusbeta = (Button) myView.findViewById(R.id.plusBeta);
        plusgamma = (Button) myView.findViewById(R.id.plusGamma);
        plusspeed = (Button) myView.findViewById(R.id.plusSpeed);

        minusx = (Button) myView.findViewById(R.id.minusX);
        minusy = (Button) myView.findViewById(R.id.minusY);
        minusz = (Button) myView.findViewById(R.id.minusZ);
        minusalfa = (Button) myView.findViewById(R.id.minusAlfa);
        minusbeta = (Button) myView.findViewById(R.id.minusBeta);
        minusgamma = (Button) myView.findViewById(R.id.minusGamma);
        minusspeed = (Button) myView.findViewById(R.id.minusSpeed);


        textdata1 = (TextView) myView.findViewById(R.id.textX);
        textdata2 = (TextView) myView.findViewById(R.id.textY);
        textdata3 = (TextView) myView.findViewById(R.id.textZ);
        textdata4 = (TextView) myView.findViewById(R.id.textAlfa);
        textdata5 = (TextView) myView.findViewById(R.id.textBeta);
        textdata6 = (TextView) myView.findViewById(R.id.textGamma);

        switch_auto = (Switch) myView.findViewById(R.id.auto_switch_crawl);

        zero_values = (Button) myView.findViewById(R.id.zero_crawl);
        sendbutton_crawl = (FloatingActionButton) myView.findViewById(R.id.send_button_robotstate);
        emergency_crawl = (FloatingActionButton) myView.findViewById(R.id.emergency_STOP_crawl);

        Ip = getArguments().getString("ip");
        port = getArguments().getInt("port");
        sendbutton_crawl.setOnClickListener(sendbuttonOnClickListener);
        zero_values.setOnClickListener(zeroes_listener);
        emergency_crawl.setOnClickListener(emergency_listener);


        plusx.setOnClickListener(buttons_listener);
        minusx.setOnClickListener(buttons_listener);
        plusy.setOnClickListener(buttons_listener);
        minusy.setOnClickListener(buttons_listener);
        plusz.setOnClickListener(buttons_listener);
        minusz.setOnClickListener(buttons_listener);
        plusalfa.setOnClickListener(buttons_listener);
        minusalfa.setOnClickListener(buttons_listener);
        plusbeta.setOnClickListener(buttons_listener);
        minusbeta.setOnClickListener(buttons_listener);
        plusgamma.setOnClickListener(buttons_listener);
        minusgamma.setOnClickListener(buttons_listener);
        plusspeed.setOnClickListener(buttons_listener);
        minusspeed.setOnClickListener(buttons_listener);

        wave=(RadioButton)myView.findViewById(R.id.wave_step);
        tripod=(RadioButton)myView.findViewById(R.id.tripod_step);

        wave.setOnClickListener(radiobutton_listener);
        tripod.setOnClickListener(radiobutton_listener);

        float zero = (float) 0.0;

        x.setText(String.format("%.2f", zero)+"m");
        y.setText(String.format("%.2f", zero)+"m");
        z.setText(String.format("%.2f", zero)+"m");
        alfa.setText(String.format("%.2f", zero)+"rad");
        beta.setText(String.format("%.2f", zero)+"rad");
        gamma.setText(String.format("%.2f", zero)+"rad");
        speed.setText(String.format("%.2f", 0.05)+"%");

        speed_send=(float)0.3;
        speed.setText(String.format("%.2f", speed_send*100));

        return myView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView selected_option = (TextView) view;
        Toast.makeText(this.getActivity(), "Wybrano: " + selected_option.getText(), Toast.LENGTH_SHORT).show();
        zeroes();







    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    View.OnClickListener sendbuttonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {



          interfaceDataCommunicator.updateData(Ip, port, flaga, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);

        }
    };


    View.OnClickListener buttons_listener = new View.OnClickListener() {

        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.plusX:
                    x_send = x_send + offset;
                    break;
                case R.id.minusX:
                    x_send = x_send - offset;
                    break;
                case R.id.plusY:
                    y_send = y_send + offset;
                    break;
                case R.id.minusY:
                    y_send = y_send - offset;
                    break;
                case R.id.plusZ:
                    z_send = z_send + offset;
                    break;
                case R.id.minusZ:
                    z_send = z_send - offset;
                    break;
                case R.id.plusAlfa:
                    alfa_send = alfa_send + offset;
                    break;
                case R.id.minusAlfa:
                    alfa_send = alfa_send - offset;
                    break;
                case R.id.plusBeta:
                    beta_send = beta_send + offset;
                    break;
                case R.id.minusBeta:
                    beta_send = beta_send - offset;
                    break;
                case R.id.plusGamma:
                    gamma_send = gamma_send + offset;
                    break;
                case R.id.minusGamma:
                    gamma_send = gamma_send - offset;
                    break;
                case R.id.plusSpeed:
                    speed_send = speed_send + offset;
                    break;
                case R.id.minusSpeed:
                    speed_send = speed_send - offset;
                    break;


            }

            if (switch_auto.isChecked() == true) {
                interfaceDataCommunicator.updateData(Ip, port, flaga, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);

            }

            x.setText(String.format("%.2f", x_send)+" m");
            y.setText(String.format("%.2f", y_send)+" m");
            z.setText(String.format("%.2f", z_send)+" m");
            alfa.setText(String.format("%.2f", alfa_send)+" rad");
            beta.setText(String.format("%.2f", beta_send)+" rad");
            gamma.setText(String.format("%.2f", gamma_send)+" rad");
            speed.setText(String.format("%.2f", speed_send*100)+" %");


        }

    };


    View.OnClickListener emergency_listener = new View.OnClickListener() {

        public void onClick(View view) {

            interfaceDataCommunicator.updateData(Ip, port, 1, 0, 0, 0, 0, 0, 0, (float) 0.5, id);

        }

    };


    void zeroes() {

        x.setText("0,00 m" );
        y.setText("0,00 m");
        z.setText("0,00 m");
        alfa.setText("0,00 rad");
        beta.setText("0,00 rad");
        gamma.setText("0,00 rad");
        speed.setText(String.format("%.2f", speed_send*100)+" %");

        x_send = 0;
        y_send = 0;
        z_send = 0;
        alfa_send = 0;
        beta_send = 0;
        gamma_send = 0;
    }


    View.OnClickListener zeroes_listener = new View.OnClickListener() {

        public void onClick(View view) {

            zeroes();

        }

    };

    View.OnClickListener radiobutton_listener = new View.OnClickListener() {

        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.wave_step:
                    if (wave.isChecked()) {
                        tripod.setChecked(false);
                        flaga = 26;
                        interfaceDataCommunicator.updateData(Ip, port, 29, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);
                        was_wave=true;
if(was_tripod)
{
    interfaceDataCommunicator.updateData(Ip, port, 28, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);
was_tripod=false;
}



                    }


                    break;
                case R.id.tripod_step:
                    if (tripod.isChecked()) {
                        wave.setChecked(false);
                        flaga=25;
                        interfaceDataCommunicator.updateData(Ip, port, 27, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);
                        was_tripod=true;

                        if(was_wave)
                        {
                            interfaceDataCommunicator.updateData(Ip, port, 30, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);
was_wave=false;
                        }


                    }


                    break;


            }

        }


    };
}