package com.example.bijen.onlinedhaba.app;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.bijen.onlinedhaba.R;
import com.example.bijen.onlinedhaba.adapter.ViewPagerAdapter;

public class User extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_user);
        viewPager = (ViewPager) findViewById(R.id.view_pager_user);
        toolbar = (Toolbar) findViewById(R.id.user_toolbar);
        toolbar.setTitle("User");
        toolbar.inflateMenu(R.menu.menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentUserSignup(),"Sign Up");
        viewPagerAdapter.addFragment(new FragmentUserLogin(),"Login");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
