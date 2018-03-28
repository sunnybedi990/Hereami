package com.vedant.hereami.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.vedant.hereami.Fragment.CallsFragment;
import com.vedant.hereami.Fragment.ChatFragment;
import com.vedant.hereami.Fragment.ContactsFragment;
import com.vedant.hereami.R;
import com.vedant.hereami.miscellaneous.RuntimePermissionsActivity;
import com.vedant.hereami.miscellaneous.ViewPagerAdapter;
import com.vedant.hereami.login.login;

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
    private ArrayAdapter<String> itemsAdapter;
    private CoordinatorLayout coordinatorLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_without_icon);
        //Initializing viewPager
        firebaseAuth = FirebaseAuth.getInstance();
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        coordinatorLayout1 = findViewById(R.id
                .coordinatorLayoutmain1);
        if (firebaseAuth.getCurrentUser() == null) {
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, login.class));
        }
        //Initializing the tablayout
        tabLayout = findViewById(R.id.tablayout);
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



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        callsFragment=new CallsFragment();
        chatFragment=new ChatFragment();
        contactsFragment=new ContactsFragment();
        adapter.addFragment(callsFragment, "Tracking");
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
                        Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE}, R.string
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
