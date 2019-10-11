package com.geforce.vijai.admin_vote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import java.util.List;

public class Login extends AppCompatActivity {
EditText ephone,email,ereg_no;
Button login;
Spinner edept;
String data,url="http://vijai1.eu5.org/sona/admin_login.php";
String shared_id,shared_dept,sphone,smail,sdept,sreg_no;
SharedPreferences sp;
//final ProgressBar pb=(ProgressBar)findViewById(R.id.pbr);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.login);
        ephone=(EditText)findViewById(R.id.phone);
        email=(EditText)findViewById(R.id.mail);
        edept=(Spinner) findViewById(R.id.dept);
        ereg_no=(EditText)findViewById(R.id.reg_no);

        edept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sdept=edept.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    public void login(View view) {
        //pb.setVisibility(View.VISIBLE);
        login.setEnabled(false);
         sphone= ephone.getText().toString().trim();
         smail=email.getText().toString().trim();
         sreg_no=ereg_no.getText().toString().trim();


        /*if (sphone.isEmpty() || sphone.length() < 10) {
            ephone.setError("Valid number is required");
            ephone.requestFocus();
            return;
        }
        if(smail.isEmpty()){
            email.setError("mail is require");
            email.requestFocus();
            return;
        }
        if(sdept.isEmpty() || sdept.equals("cse")){
            email.setError("mail is require");
            email.requestFocus();
            return;
        }
        else */if(sphone.length()==1) {
           // String phoneNumber = "+91" + number;
            checkdb();

        }
    }

    private void checkdb() {
        //http://vijai1.eu5.org/sona/admin_login.php?dept=cse&reg_no=1&mail=arivalagan588@gmail.com&phone=7010630368
        data="?phone="+sphone+"&mail="+smail+"&dept="+sdept+"&reg_no="+sreg_no;
        url+=data;
        Log.d( "url: ",url);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    boolean error=jsonObject.getBoolean("error");
                    if(!error){
                        shared_id=jsonObject.getString("reg_no");
                        shared_dept=jsonObject.getString("dept");

                        sp =getSharedPreferences("admin",MODE_PRIVATE);
                        SharedPreferences.Editor et=sp.edit();
                        et.putString("phone",ephone.getText().toString());
                        et.putString("reg_no",shared_id);
                        et.putString("dept",shared_dept);
                        et.putString("mail",email.getText().toString().trim());
                        et.apply();

                        Toast.makeText(getApplicationContext(),"Admin of"+shared_dept,Toast.LENGTH_SHORT).show();

                        Intent i =new Intent(getApplicationContext(),home.class);
                        //i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                    else {

                        Toast.makeText(getApplicationContext(),""+message.toString(),Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (JSONException e) {
                    login.setEnabled(true);
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"error"+e.toString(),Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),""+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        login.setEnabled(true);
        url="http://vijai1.eu5.org/sona/admin_login.php";
    }
}
