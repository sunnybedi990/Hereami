package com.vedant.hereami;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListActivity extends Activity {
    public List<String> lst;
    public ListView listView;
    public String[] stockArr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.listview1);
        Firebase.setAndroidContext(this);
        Firebase fb_parent = new Firebase("https://iamhere-29f2b.firebaseio.com/");
        final Firebase fb_to_read = fb_parent.child("data");
        Firebase fb_put_child = fb_to_read.push();
        lst = new ArrayList<String>();
        final int a;
        lst.clear();
        // FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("geofire").setDatabaseUrl(GEO_FIRE_DB).build();
        //FirebaseApp app = FirebaseApp.initializeApp(this, options);
        fb_to_read.addValueEventListener(new ValueEventListener() {


            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            public void onDataChange(DataSnapshot result) {
                // Result will be holded Here

                for (DataSnapshot dsp : result.getChildren()) {
                    String keyname = String.valueOf(dsp.getKey()).replace("dot", ".");

                    long sun = result.getChildrenCount();
                    if (lst.size() > sun) {
                        lst.clear();
                    } else {


                        lst.add(keyname); //add result into array list


                        Log.e(">>>>>List Value", lst.size() + "");

                        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, lst);
                        final Collator col = Collator.getInstance();
                        itemsAdapter.sort(new Comparator<String>() {
                            @Override
                            public int compare(String lhs, String rhs) {
                                return col.compare(lhs, rhs);
                            }
                        });


                        itemsAdapter.notifyDataSetChanged();


                        listView.setAdapter(itemsAdapter);
                        Log.e(">>>>asdd", lst + "");


                    }

                }


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //TextView t1 = (TextView) findViewById(android.R.id.text1);
                        Log.e(">>>>asd", lst.get(position) + "");
                        Intent intent4 = new Intent(ListActivity.this, TestActivity.class).putExtra("key_position", lst.get(position).replace(".", "dot"));
                        startActivity(intent4);
                    }
                });


            }


        });


        //Log.e(">>>>>List size", lst.size() + "");

//        Log.e(">>>>>stock size", stockArr.length + "");

        //Log.e(">>>>>List ", stockArr[0] + "");
//            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,lst);
//
        //          listView.setAdapter(itemsAdapter);
//
    }



}
