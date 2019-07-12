package com.asfvisibook.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.asfvisibook.android.Adapter.BeliefAdapter;
import com.asfvisibook.android.database.Database;
import com.example.asfvisibook.Model.Belief;
import java.util.List;

public class Beliefs extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Belief> beliefList;
    BeliefAdapter adapter;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_beliefs);
        initialize();
        setView();

    }

    private void setView() {
        beliefList = new Database(this).getBeliefs();
        adapter = new BeliefAdapter(this, beliefList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();


    }

    private void initialize() {
        recyclerView = findViewById(R.id.beliefrecycler);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Our Beliefs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home){
            super.onBackPressed();
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


}
