package com.geforce.vijai.admin_vote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
String posting,data2;
EditText can1,can2,can3,can4;
String url1="http://vijai1.eu5.org/sona/set_candidate_details.php",data;
String  url2="http://vijai1.eu5.org/sona/reset_vote.php",reg_no;
SharedPreferences sp;
boolean set=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        can1=(EditText)findViewById(R.id.c1);
        can2=(EditText)findViewById(R.id.c2);
        can3=(EditText)findViewById(R.id.c3);
        can4=(EditText)findViewById(R.id.c4);
        final Spinner spinner=(Spinner)findViewById(R.id.post_spinner);

        sp =getSharedPreferences("admin",MODE_PRIVATE);
        reg_no=sp.getString("reg_no","reg_no");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posting=spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void submit(View view) {
        String candi1=can1.getText().toString();
        String candi2=can2.getText().toString();
        String candi3=can3.getText().toString();
        String candi4=can4.getText().toString();

        data="?c1="+candi1+"&c2="+candi2+"&c3="+candi3+"&c4="+candi4+"&admin_reg_no="+reg_no+"&post="+posting;
        url1+=data;
        Log.i("url_set_candi",url1);

        data2="?post="+posting+"&reset_candidate=1&reset_student=1&reset_dept=1"+"&reg_no="+reg_no;
        url2+=data2;
        Log.i("url_set_candi",url2);
        reset_votings(url2);
    }

    private void reset_votings(String url) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    boolean error=jsonObject.getBoolean("error");
                    if(!error){
                        Log.d("reset status","successs");
                        set=true;
                        submitdb(url1);
                    }
                    else {
                        Log.d("reset status","fail");
                        Toast.makeText(getApplicationContext(),""+message.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("reset status","fail");
                    Toast.makeText(getApplicationContext(),"error"+e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("reset status","fail");
                Toast.makeText(getApplicationContext(),""+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void submitdb(String url) {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    boolean error=jsonObject.getBoolean("error");
                    if(!error){
                        if(set){
                            SharedPreferences.Editor et=sp.edit();
                            et.putString("post_st_end",posting);
                            et.apply();
                            Log.d("post form variable",posting);
                            Log.d("post from shared main",sp.getString("post_st_end","default post"));
                            Intent i =new Intent(getApplicationContext(),seted.class);

                            startActivity(i);
                            finish();
                            set=false;
                            }
                        }
                    else {
                        Log.d("set candi","fail");
                        Toast.makeText(getApplicationContext(),""+message.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("set candi","fail");
                    Toast.makeText(getApplicationContext(),"error"+e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("set candi","fail");
                Toast.makeText(getApplicationContext(),""+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void reset(View view) {
        can1.getText().clear();
        can2.getText().clear();
        can3.getText().clear();
        can4.getText().clear();
    }
}
