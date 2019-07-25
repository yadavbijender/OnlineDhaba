package com.example.bijen.onlinedhaba.app;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.bijen.onlinedhaba.R;
import com.example.bijen.onlinedhaba.adapter.ViewPagerAdapter;

public class Admin extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_admin);
        viewPager = (ViewPager) findViewById(R.id.view_pager_admin);

        setUpToolbar();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentAdminSignin(),"Sign up");
        viewPagerAdapter.addFragment(new FragmentAdminLogin(),"Log in");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        toolbar.setTitle("Admin");
    }
}
