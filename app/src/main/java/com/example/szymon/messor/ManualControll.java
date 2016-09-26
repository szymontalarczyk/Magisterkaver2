package com.example.szymon.messor;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ManualControll extends Fragment implements AdapterView.OnItemSelectedListener {


    public Settings_Fragment.InterfaceDataCommunicator interfaceDataCommunicator;


    //ManualControll id = 2;
    static int id = 2;
    View myView;
    Spinner spinner;
    ArrayAdapter adapter;
    TextView x, y, z, alfa, beta, gamma, speed;
    FloatingActionButton sendbutton_manual;
    FloatingActionButton emergency_manual;

    static final float offset = (float) 0.01;
    float offset_leg = offset;

    int flaga;
    float x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send;
    Button plusx, plusy, plusz, plusalfa, plusbeta, plusgamma, plusspeed;
    Button minusx, minusy, minusz, minusalfa, minusbeta, minusgamma, minusspeed;
    Button zero_values;
    Switch switch_auto;
    String Ip;
    int port;


    TextView textdata1, textdata2, textdata3, textdata4, textdata5, textdata6, textdata7;

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
        myView = inflater.inflate(R.layout.manualcontroll, container, false);
        spinner = (Spinner) myView.findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.lista_komend_sterowanie_manualne, android.R.layout.simple_list_item_multiple_choice);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
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
        textdata7 = (TextView) myView.findViewById(R.id.textSpeed);

        switch_auto = (Switch) myView.findViewById(R.id.auto_switch_manual);

        zero_values = (Button) myView.findViewById(R.id.zero_manual);
        sendbutton_manual = (FloatingActionButton) myView.findViewById(R.id.send_button_robotstate);
        emergency_manual = (FloatingActionButton) myView.findViewById(R.id.emergency_STOP_manual);

        Ip = getArguments().getString("ip");
        port = getArguments().getInt("port");
        sendbutton_manual.setOnClickListener(sendbuttonOnClickListener);
        zero_values.setOnClickListener(zeroes_listener);
        emergency_manual.setOnClickListener(emergency_listener);


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


        float zero = (float) 0.0;

        x.setText(String.format("%.2f", zero)+" m");
        y.setText(String.format("%.2f", zero)+" m");
        z.setText(String.format("%.2f", zero)+" m");
        alfa.setText(String.format("%.2f", zero)+" rad");
        beta.setText(String.format("%.2f", zero)+" rad");
        gamma.setText(String.format("%.2f", zero)+" rad");
        speed.setText(String.format("%.2f", 0.1*100)+ " %");


        return myView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView selected_option = (TextView) view;
        Toast.makeText(this.getActivity(), "Wybrano: " + selected_option.getText(), Toast.LENGTH_SHORT).show();
        zeroes();
        if (spinner.getSelectedItemPosition() > 1) {
            beta_send=(float)0.1;
            beta.setText(String.format("%.2f", beta_send*100));
            offset_leg = 1;
            textdata1.setText("Wybór nogi");
            textdata2.setText("Kąt Alfa ");
            textdata3.setText("Kąt Beta ");
            textdata4.setText("Kąt Gamma ");
            textdata5.setVisibility(View.VISIBLE);
            textdata5.setText("Prędkość [%]");
            textdata6.setVisibility(View.GONE);
            beta.setVisibility(View.VISIBLE);
            gamma.setVisibility(View.GONE);
            plusbeta.setVisibility(View.VISIBLE);
            plusgamma.setVisibility(View.GONE);
            minusbeta.setVisibility(View.VISIBLE);
            minusgamma.setVisibility(View.GONE);
            speed.setVisibility(View.GONE);
            plusspeed.setVisibility(View.GONE);
            minusspeed.setVisibility(View.GONE);
            textdata7.setVisibility(View.GONE);
            speed_send=0;

        }
        else {
            offset_leg = offset;
            textdata1.setText("Przesunięcie X ");
            textdata2.setText("Przesunięcie Y ");
            textdata3.setText("Przesunięcie Z ");
            textdata4.setText("Obrót wokół osi X ");
            textdata5.setText("Obrót wokół osi Y ");
            textdata5.setVisibility(View.VISIBLE);
            textdata6.setVisibility(View.VISIBLE);
            beta.setVisibility(View.VISIBLE);
            gamma.setVisibility(View.VISIBLE);
            plusbeta.setVisibility(View.VISIBLE);
            plusgamma.setVisibility(View.VISIBLE);
            minusbeta.setVisibility(View.VISIBLE);
            minusgamma.setVisibility(View.VISIBLE);
            textdata7.setVisibility(View.VISIBLE);
            plusspeed.setVisibility(View.VISIBLE);
            minusspeed.setVisibility(View.VISIBLE);
            speed.setVisibility(View.VISIBLE);
            speed_send=(float)0.1;
            beta_send=(float)0.0;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    View.OnClickListener sendbuttonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {


            flaga = spinner.getSelectedItemPosition() + 1;
            // x_send=Float.parseFloat(x.getText().toString());
            //  y_send=Float.parseFloat(y.getText().toString());
            // z_send=Float.parseFloat(z.getText().toString());
            //  alfa_send=Float.parseFloat(alfa.getText().toString());
            // beta_send=Float.parseFloat(beta.getText().toString());
            //  gamma_send=Float.parseFloat(gamma.getText().toString());
            // speed_send=Float.parseFloat(speed.getText().toString());


            interfaceDataCommunicator.updateData(Ip, port, flaga, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);


        }
    };


    View.OnClickListener buttons_listener = new View.OnClickListener() {

        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.plusX:
                    x_send = x_send + offset_leg;
                    break;
                case R.id.minusX:
                    x_send = x_send - offset_leg;
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

            x.setText(String.format("%.2f", x_send) +" m");
            y.setText(String.format("%.2f", y_send)+" m");
            z.setText(String.format("%.2f", z_send)+" m");
            alfa.setText(String.format("%.2f", alfa_send)+" rad");
            if (spinner.getSelectedItemPosition() > 1) {
                beta.setText(String.format("%.2f", beta_send*100)+" %");
            }
            else
            {
                beta.setText(String.format("%.2f", beta_send)+" rad");

            }
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

        x.setText("0,00 m");
        y.setText("0,00 m");
        z.setText("0,00 m");
        alfa.setText("0,00 rad");
        beta.setText("0,00 rad");
        gamma.setText("0,00 rad");

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


}