package com.example.listclickshabalkova;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences myNoteSharedPref;
    private static String NOTE_TEXT = "note_text";
    private static String HEADER = "header";
    private static String COUNT = "count";
    private List<Map<String, String>> values;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPrefs();
        list = findViewById(R.id.listView);
        final SwipeRefreshLayout swipe = findViewById(R.id.swipe);
        values = prepareContent();
        String[] from = new String[]{"header", "count"};
        int[] to = new int[]{R.id.textViewHeader, R.id.textViewText};
        final SimpleAdapter adapter = new SimpleAdapter(this,values, R.layout.list_item, from, to);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView headerView = findViewById(R.id.textViewHeader);
//                TextView countView = findViewById(R.id.textViewText);
//                headerView.setText("");
//                countView.setText("");
                values.remove(i);
                adapter.notifyDataSetChanged();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                values.clear();
                values.addAll(prepareContent());
   //             values = prepareContent();
//                SimpleAdapter adapter = new SimpleAdapter(this,values, R.layout.list_item, from, to);
//                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        });


    }


    @NonNull
    private List<Map<String, String>> prepareContent() {
        String[] largeText = myNoteSharedPref.getString(NOTE_TEXT, "").split("\n\n");
        List<Map<String, String>> content = new ArrayList<>();

        for (int i=0; i < largeText.length; i++){
            Map<String,String> firstRowMap = new HashMap<>();
            firstRowMap.put(HEADER,largeText[i]);
            firstRowMap.put(COUNT, String.valueOf(largeText[i].length()));
            content.add(firstRowMap);
        }
        return  content;
    }

    private void initPrefs() {
        myNoteSharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);
        SharedPreferences.Editor myEditor = myNoteSharedPref.edit();
        String largeText = getString(R.string.large_text);
        myEditor.putString(NOTE_TEXT, largeText);
        myEditor.apply();
    }
//    private void getDateFromSharedPref(){
//        String noteTxt = myNoteSharedPref.getString(NOTE_TEXT, "");
//        mInputNote.setText(noteTxt);
//    }
}