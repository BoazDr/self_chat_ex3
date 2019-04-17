package com.example.self_chat_ex3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    MyAdapter mAdapter;
    ArrayList<String> allMessages;
    private static final String TEXT_VALUE = "textValue";
    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.plain_text_input);
        Button sendButton = findViewById(R.id.send_button);
        final SharedPreferences my_shared_prefs  = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> allEntries = my_shared_prefs.getAll();
        if (savedInstanceState == null) {
            allMessages = new ArrayList<>();
            // if our SharedPreferences is not empty, add all strings from it.
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                allMessages.add(entry.getValue().toString());
            }
        }
        else {
            allMessages = savedInstanceState.getStringArrayList(TEXT_VALUE);
        }

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter(this, allMessages);
        recyclerView.setAdapter(mAdapter);

        //Toast init
        final Context context = getApplicationContext();
        final CharSequence errorText = "Empty message? Realy? Try again...";
        final int duration = Toast.LENGTH_SHORT;


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = editText.getText().toString();
                if (item.isEmpty()){
                    Toast toast = Toast.makeText(context, errorText, duration);
                    toast.show();
                }
                else{
                    editText.setText("");
                    allMessages.add(item);
                    SharedPreferences.Editor editor = my_shared_prefs.edit();
                    editor.putString(item, item);
                    editor.apply();
                    mAdapter.notifyItemInserted(allMessages.size());
                }
            }
        });
        // write to log the current size of chat messages list
        int num_of_messages = mAdapter.getItemCount();
        Log.i(TAG, "The number of messages is: " + Integer.toString(num_of_messages));
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(TEXT_VALUE, allMessages);
    }


}