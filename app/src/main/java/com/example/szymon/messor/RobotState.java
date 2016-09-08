package com.example.szymon.messor;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class RobotState extends android.app.Fragment implements AdapterView.OnItemSelectedListener {


    public Settings_Fragment.InterfaceDataCommunicator interfaceDataCommunicator;


    //RobotState id = 4;
    static int id = 4;
    View myView;
    Spinner spinner_rs;
    ArrayAdapter adapter;
    FloatingActionButton sendbutton_rs;
    FloatingActionButton emergency_rs;
    TextView rs_response,dataresponse0,dataresponse1,dataresponse2,dataresponse3,dataresponse4,dataresponse5,dataresponse6,dataresponse7;
    String response;
    Switch autoswitch_robotstate;
    int flaga;
    float x_send,y_send,z_send,alfa_send,beta_send,gamma_send,speed_send;
    float response_1,response_2,response_3,response_4,response_5,response_6,response_0,response_7;
    String Ip;
    int port;
    Button plus_leg1, plus_leg0,minus_leg0,minus_leg1;
    EditText leg1,leg0;
    TextView textdata1, textdata2, textdata3, textdata4, textdata5, textdata6,textdata0,textdata7,dataview1,dataview2,dataview3,dataview4,dataview5,dataview6,dataview7,dataview0,chooseleg0,chooseleg1;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            flaga = spinner_rs.getSelectedItemPosition()+10;
            response="odebrano  "+response_0 + "  " + response_1+ "  " + response_2+ "  " + response_3+ "  " + response_4+ "  " + response_5+ "  " + response_6+"  " + response_7;
            set_response();
            rs_response.setText(response);





            interfaceDataCommunicator.updateData(Ip, port, flaga, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);

            timerHandler.postDelayed(this, 1);

        }
    };




    @Override
    public void onAttach (Activity activity)
    {
        super.onAttach(activity);
        try{
            interfaceDataCommunicator = (Settings_Fragment.InterfaceDataCommunicator) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());}

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.robotstate, container, false);
        spinner_rs = (Spinner)myView.findViewById(R.id.spinner_rc);
        adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.lista_komend_odczyt_stanu_robota,android.R.layout.simple_list_item_multiple_choice);
        spinner_rs.setAdapter(adapter);
        spinner_rs.setOnItemSelectedListener(this);
        autoswitch_robotstate=(Switch)myView.findViewById(R.id.switch_robotstate);
        plus_leg0=(Button)myView.findViewById(R.id.plusleg0);
        plus_leg1=(Button)myView.findViewById(R.id.plusleg1);
        minus_leg0=(Button)myView.findViewById(R.id.minusleg0);
        minus_leg1=(Button)myView.findViewById(R.id.minusleg1);


        plus_leg0.setOnClickListener(buttons_listener);
        plus_leg1.setOnClickListener(buttons_listener);
        minus_leg0.setOnClickListener(buttons_listener);
        minus_leg1.setOnClickListener(buttons_listener);
        leg0=(EditText)myView.findViewById(R.id.et_leg0);
        leg1=(EditText)myView.findViewById(R.id.et_leg1);


        x_send=0;
        y_send=1;

        textdata1 = (TextView) myView.findViewById(R.id.textdata1);
        textdata2 = (TextView) myView.findViewById(R.id.textdata2);
        textdata3 = (TextView) myView.findViewById(R.id.textdata3);
        textdata4 = (TextView) myView.findViewById(R.id.textdata4);
        textdata5 = (TextView) myView.findViewById(R.id.textdata5);
        textdata6 = (TextView) myView.findViewById(R.id.textdata6);
        textdata0 = (TextView) myView.findViewById(R.id.textdata0);
        textdata7 = (TextView) myView.findViewById(R.id.textdata7);

        dataview0 = (TextView)myView.findViewById(R.id.dataview0);
        dataview1 = (TextView)myView.findViewById(R.id.dataview1);
        dataview2 = (TextView)myView.findViewById(R.id.dataview2);
        dataview3 = (TextView)myView.findViewById(R.id.dataview3);
        dataview4 = (TextView)myView.findViewById(R.id.dataview4);
        dataview5 = (TextView)myView.findViewById(R.id.dataview5);
        dataview6 = (TextView)myView.findViewById(R.id.dataview6);
        dataview7 = (TextView)myView.findViewById(R.id.dataview7);

        chooseleg0 = (TextView)myView.findViewById(R.id.chooseleg0);
        chooseleg1 = (TextView)myView.findViewById(R.id.chooseleg1);


        autoswitch_robotstate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){


            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               if(isChecked==true)
               {
                 timerHandler.postDelayed(timerRunnable,0);

               }
                else
               {
                   timerHandler.removeCallbacks(timerRunnable);

               }



            }
        });



       rs_response=(TextView)myView.findViewById(R.id.response_robotstate);
        dataresponse0=(TextView)myView.findViewById(R.id.dataview0);
        dataresponse1=(TextView)myView.findViewById(R.id.dataview1);
        dataresponse2=(TextView)myView.findViewById(R.id.dataview2);
        dataresponse3=(TextView)myView.findViewById(R.id.dataview3);
        dataresponse4=(TextView)myView.findViewById(R.id.dataview4);
        dataresponse5=(TextView)myView.findViewById(R.id.dataview5);
        dataresponse6=(TextView)myView.findViewById(R.id.dataview6);
        dataresponse7=(TextView)myView.findViewById(R.id.dataview7);

      sendbutton_rs=(FloatingActionButton) myView.findViewById(R.id.send_button_robotstate);
        emergency_rs=(FloatingActionButton) myView.findViewById(R.id.emergency_STOP_robotstate);

        Ip = getArguments().getString("ip");
        port = getArguments().getInt("port");
        sendbutton_rs.setOnClickListener(sendbuttonOnClickListener);
        emergency_rs.setOnClickListener(emergency_listener);







        return myView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        TextView selected_option = (TextView) view;
        Toast.makeText(this.getActivity(),"Wybrano: "+selected_option.getText(),Toast.LENGTH_SHORT).show();


     int id_function = spinner_rs.getSelectedItemPosition();
        if(id_function==2  || id_function==4 || id_function==7 || id_function==9 || id_function==11)
        {
            function_with6args();

        }
        else
        {
            function_with3args();

        }


            set_response_zero();

        //interfaceDataCommunicator.updateData(Ip, port, flaga, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    View.OnClickListener sendbuttonOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {



            flaga = spinner_rs.getSelectedItemPosition()+10;


            response="odebrano  "+response_0 + "  " + response_1+ "  " + response_2+ "  " + response_3+ "  " + response_4+ "  " + response_5+ "  " + response_6+"  " + response_7;



            interfaceDataCommunicator.updateData(Ip, port, flaga, x_send, y_send, z_send, alfa_send, beta_send, gamma_send, speed_send, id);
set_response();


            rs_response.setText(response);


        }
    };


void set_response()
{

    dataresponse0.setText(String.valueOf(response_0));
    dataresponse1.setText(String.valueOf(response_1));
    dataresponse2.setText(String.valueOf(response_2));
    dataresponse3.setText(String.valueOf(response_3));
    dataresponse4.setText(String.valueOf(response_4));
    dataresponse5.setText(String.valueOf(response_5));
    dataresponse6.setText(String.valueOf(response_6));
    dataresponse7.setText(String.valueOf(response_7));

}
static float zero = 0;
void set_response_zero()
{

    dataresponse0.setText(String.valueOf(zero));
    dataresponse1.setText(String.valueOf(zero));
    dataresponse2.setText(String.valueOf(zero));
    dataresponse3.setText(String.valueOf(zero));
    dataresponse4.setText(String.valueOf(zero));
    dataresponse5.setText(String.valueOf(zero));
    dataresponse6.setText(String.valueOf(zero));
    dataresponse7.setText(String.valueOf(zero));

}


    View.OnClickListener emergency_listener = new View.OnClickListener(){

        public void onClick(View view)
        {

            interfaceDataCommunicator.updateData(Ip, port, 1, 0, 0, 0, 0, 0, 0, (float)0.5, id);

        }

    };


    public void setResponse(String response) {
        this.response = response;
    }

    public void sendvalues(float response_data0, float response_data1, float response_data2, float response_data3, float response_data4, float response_data5, float response_data6,float response_data7) {

        response_0=response_data0;
        response_1=response_data1;
        response_2=response_data2;
        response_3=response_data3;
        response_4=response_data4;
        response_5=response_data5;
        response_6=response_data6;
        response_7=response_data7;

    }

    View.OnClickListener buttons_listener = new View.OnClickListener() {

        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.plusleg0:
                    x_send = x_send + 1;
                    break;
                case R.id.minusleg0:
                    x_send = x_send - 1;
                    break;
                case R.id.plusleg1:
                    y_send = y_send + 1;
                    break;
                case R.id.minusleg1:
                    y_send = y_send - 1;
                    break;


            }
            if(x_send<0)
            {
                x_send=0;
            }
            if(y_send<0)
            {
                y_send=0;
            }
            if(x_send>5)
            {
                x_send=5;
            }
            if(y_send>5)
            {
                y_send=5;
            }

            leg0.setText(String.format("%.0f", x_send));
            leg1.setText(String.format("%.0f", y_send));
            set_text();

        }


};

void function_with3args()
{
    textdata1.setVisibility(View.VISIBLE);
    textdata2.setVisibility(View.VISIBLE);
    textdata3.setVisibility(View.VISIBLE);
    textdata4.setVisibility(View.GONE);
    textdata5.setVisibility(View.GONE);
    textdata6.setVisibility(View.GONE);
    textdata0.setVisibility(View.GONE);
    textdata7.setVisibility(View.GONE);


    dataview0.setVisibility(View.GONE);
    dataview1.setVisibility(View.VISIBLE);
    dataview2.setVisibility(View.VISIBLE);
    dataview3.setVisibility(View.VISIBLE);
    dataview4.setVisibility(View.GONE);
    dataview5.setVisibility(View.GONE);
    dataview6.setVisibility(View.GONE);
    dataview7.setVisibility(View.GONE);

    chooseleg0.setVisibility(View.VISIBLE);
    leg0.setVisibility(View.VISIBLE);
    plus_leg0.setVisibility(View.VISIBLE);
    minus_leg0.setVisibility(View.VISIBLE);

    chooseleg1.setVisibility(View.GONE);
    leg1.setVisibility(View.GONE);
    plus_leg1.setVisibility(View.GONE);
    minus_leg1.setVisibility(View.GONE);

    set_text();
}


    void function_with6args()
    {
        textdata1.setVisibility(View.VISIBLE);
        textdata2.setVisibility(View.VISIBLE);
        textdata3.setVisibility(View.VISIBLE);
        textdata4.setVisibility(View.VISIBLE);
        textdata5.setVisibility(View.VISIBLE);
        textdata6.setVisibility(View.VISIBLE);
        textdata0.setVisibility(View.GONE);
        textdata7.setVisibility(View.GONE);


        dataview0.setVisibility(View.GONE);
        dataview1.setVisibility(View.VISIBLE);
        dataview2.setVisibility(View.VISIBLE);
        dataview3.setVisibility(View.VISIBLE);
        dataview4.setVisibility(View.VISIBLE);
        dataview5.setVisibility(View.VISIBLE);
        dataview6.setVisibility(View.VISIBLE);
        dataview7.setVisibility(View.GONE);


        chooseleg0.setVisibility(View.VISIBLE);
        leg0.setVisibility(View.VISIBLE);
        plus_leg0.setVisibility(View.VISIBLE);
        minus_leg0.setVisibility(View.VISIBLE);

        chooseleg1.setVisibility(View.VISIBLE);
        leg1.setVisibility(View.VISIBLE);
        plus_leg1.setVisibility(View.VISIBLE);
        minus_leg1.setVisibility(View.VISIBLE);
        set_text();
    }


    void set_text()
    {

        textdata1.setText("Noga nr. " + String.format("%.0f", x_send)+ "   Serwo nr. 0       ");
        textdata2.setText("Noga nr. " + String.format("%.0f", x_send)+ "   Serwo nr. 1       ");
        textdata3.setText("Noga nr. " + String.format("%.0f", x_send)+ "   Serwo nr. 2       ");
        textdata4.setText("Noga nr. " + String.format("%.0f", y_send)+ "   Serwo nr. 0       ");
        textdata5.setText("Noga nr. " + String.format("%.0f", y_send)+ "   Serwo nr. 1       ");
        textdata6.setText("Noga nr. " + String.format("%.0f", y_send)+ "   Serwo nr. 2       ");
    }

    }


