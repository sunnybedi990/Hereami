package com.vedant.hereami.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.vedant.hereami.Fragment.CallsFragment;
import com.vedant.hereami.Fragment.ChatFragment;
import com.vedant.hereami.Fragment.ContactsFragment;
import com.vedant.hereami.R;
import com.vedant.hereami.RuntimePermissionsActivity;
import com.vedant.hereami.ViewPagerAdapter;
import com.vedant.hereami.login;

public class TabWOIconActivity extends RuntimePermissionsActivity {
    private static final int REQUEST_PERMISSIONS = 5;
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    //Fragments
    ChatFragment chatFragment;
    CallsFragment callsFragment;
    ContactsFragment contactsFragment;
    private FirebaseAuth firebaseAuth;
    private CoordinatorLayout coordinatorLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);
        //Initializing viewPager
        firebaseAuth = FirebaseAuth.getInstance();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        coordinatorLayout1 = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayoutmain1);
        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);
        re();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_status:
                Toast.makeText(this, "Home Status Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Home Settings Click", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_with_icon:
                Intent withicon=new Intent(this,TabWithIconActivity.class);
                startActivity(withicon);
                finish();
                return true;
            case R.id.action_customtab:
                Intent custom_tab=new Intent(this,CustomTabActivity.class);
                startActivity(custom_tab);
                finish();
                return true;
            case R.id.action_logout:
                firebaseAuth.signOut();
                //closing activity
                finish();
                //starting login activity
                startActivity(new Intent(this, login.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        callsFragment=new CallsFragment();
        chatFragment=new ChatFragment();
        contactsFragment=new ContactsFragment();
        adapter.addFragment(callsFragment, "Main");
        adapter.addFragment(chatFragment,"CHAT");
        //  adapter.addFragment(contactsFragment,"CONTACTS");
        viewPager.setAdapter(adapter);
    }

    public void onPermissionsGranted(final int requestCode) {
        // Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public void re() {
        TabWOIconActivity.super.requestAppPermissions(new
                        String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);

    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            //    tsTemp = String.format("%02d:%02d", hour, minutes);
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

}
