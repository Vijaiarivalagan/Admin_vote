package com.geforce.vijai.admin_vote;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class status extends AppCompatActivity {
TextView status;
    String reg_no,post,status_voted,url_status="http://vijai1.eu5.org/sona/polling_status.php?stu_status=1";
    String result_to_print="SECTION   VOTED   NOT VOTED";
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        status=(TextView)findViewById(R.id.statuss);
        sp =getSharedPreferences("admin",MODE_PRIVATE);
        reg_no=sp.getString("reg_no","reg_no");
        post=sp.getString("post_st_end","reg_no");
        url_status+="&reg_no="+reg_no+"&post="+post;
        get_status(url_status);
    }

    private void get_status(String url) {
        Log.i("url_timer",url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("----------------inside on error"+error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){
        for(int i = 0; i<array.length(); i++) {

            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                int j=i+1;
                result_to_print+= "\n" +"   "+ json.getString("section")+"      " + json.getInt("voted") + "      " + json.getInt("notvoted");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //status_voted=result_to_print.substring(result_to_print.indexOf('\n')+1);
        //Log.d("status_voted",status_voted);
        Log.d("result_to_print",result_to_print);
        status.setText(result_to_print);

        //all.setText(result_to_print+"\n\n");

    }

}
