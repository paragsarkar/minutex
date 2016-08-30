package com.minutex.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.minutex.R;
import com.minutex.adapter.ProductListAdapter;
import com.minutex.common.util.DividerItemDecoration;
import com.minutex.domain.ProductDetails;

import java.util.ArrayList;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProductListAdapter productListAdapter;
    private RecyclerView productRecyclerView;
    private TextView username;
    private TextView useremail;
    ArrayList<ProductDetails> productDetailses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        productRecyclerView = (RecyclerView)findViewById(R.id.product_Recycler_view);
        useremail = (TextView)findViewById(R.id.user_email);
        username = (TextView)findViewById(R.id.user_name);
        productDetailses.add(new ProductDetails(R.mipmap.ic_launcher,"abc","uioq","50"));
        productDetailses.add(new ProductDetails(R.drawable.ic_action_remove,"pqr","qeq","150"));
        productDetailses.add(new ProductDetails(R.drawable.ic_action_remove,"1abc","adadawe","250"));
        productDetailses.add(new ProductDetails(R.drawable.ic_menu_camera,"a2bc","wrqqwrq","250"));
        setRecyclerViewAdapter(productDetailses);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            navigateToUpload();
        }
        else if (id == R.id.nav_gallery) {
            navigateToHistory();

        } else if (id == R.id.nav_slideshow) {
            navigateToUpload();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setRecyclerViewAdapter(ArrayList<ProductDetails> productDetailses)
    {
        productListAdapter = new ProductListAdapter(getApplicationContext(),productDetailses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        productRecyclerView.setLayoutManager(mLayoutManager);
        productRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        productRecyclerView.setAdapter(productListAdapter);
    }

    private void navigateToUpload()
    {
        Intent intent = new Intent(MainScreen.this,UploadActivity.class);
        startActivity(new Intent(intent));
    }

    private void navigateToHistory()
    {
        Intent intent = new Intent(MainScreen.this,UploadActivity.class);
        startActivity(new Intent(intent));
    }


}
