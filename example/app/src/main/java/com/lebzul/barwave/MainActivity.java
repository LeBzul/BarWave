package com.lebzul.barwave;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.lebzul.barwave.barwave.BarWave;
import com.lebzul.barwave.barwave.IBarWave;
import com.lebzul.barwave.item.MyListAdapter;
import com.lebzul.barwave.item.MyListData;

public class MainActivity extends AppCompatActivity {

    private BarWave nav;
    private MyListData[] myListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initList();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MyListAdapter adapter = new MyListAdapter(myListData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        nav = findViewById(R.id.nav);
        nav.setRecyclerView(recyclerView);
        nav.setListener(new IBarWave() {
            @Override
            public void percentScrollChange(int percent) {
                Log.d("nav", "Percent updated: "+percent);
            }
        });
    }

    private void initList() {
        myListData = new MyListData[] {
                new MyListData("Email", android.R.drawable.ic_dialog_email),
                new MyListData("Info", android.R.drawable.ic_dialog_info),
                new MyListData("Delete", android.R.drawable.ic_delete),
                new MyListData("Map", android.R.drawable.ic_dialog_map),
                new MyListData("Direction", android.R.drawable.ic_menu_directions),
        };
    }
}
