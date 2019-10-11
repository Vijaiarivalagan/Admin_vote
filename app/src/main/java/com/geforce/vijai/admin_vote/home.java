package com.geforce.vijai.admin_vote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void vote_candi(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public void start_end(View view) {
        Intent intent=new Intent(this,seted.class);
        startActivity(intent);
    }

    public void gotostatus(View view) {
        Intent intent=new Intent(this,status.class);
        startActivity(intent);
    }

    public void gotoresult(View view) {
        Intent intent=new Intent(this,result.class);
        startActivity(intent);
    }
}
