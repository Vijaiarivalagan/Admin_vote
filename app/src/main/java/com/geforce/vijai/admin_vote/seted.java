//start and end the poll
package com.geforce.vijai.admin_vote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class seted extends AppCompatActivity {
String reg_no,post,url="http://vijai1.eu5.org/sona/timer.php";
TextView reply;
//int start,end;

SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seted);
        reply=(TextView)findViewById(R.id.reply_msg);
        sp=getSharedPreferences("admin",MODE_PRIVATE);
        reg_no=sp.getString("reg_no","reg_no");
        Intent intent=getIntent();
        //post=intent.getStringExtra("post");
        post=sp.getString("post_st_end","post");
        Log.d("seted post",sp.getString("post_st_end","post"));
        Log.d("seted vari",post);
    }

    public void end_polling(View view) {
        url="http://vijai1.eu5.org/sona/timer.php";
        String data="?reg_no="+reg_no+"&post="+post+"&start=0&end=1";
        url+=data;
        Log.i("url_end",url);
        submitdb(url);
        Intent i =new Intent(getApplicationContext(),result.class);
        startActivity(i);
        finish();

    }

    public void start_polling(View view) {
        url="http://vijai1.eu5.org/sona/timer.php";
        String data="?reg_no="+reg_no+"&post="+post+"&start=1&end=0";
        url+=data;
        Log.i("url_start",url);
        submitdb(url);
    }

    private void submitdb(String url) {
        Log.i("url_timer",url);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    String message=jsonObject.getString("message");
                    boolean error=jsonObject.getBoolean("error");
                    if(!error){
                        reply.setText(message);

                    }
                    else {

                        Toast.makeText(getApplicationContext(),"error in timers",Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (JSONException e) {
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
       // url="http://vijai1.eu5.org/sona/timer.php";
    }


}
