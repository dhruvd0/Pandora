package com.example.pandora;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class Leaderboard extends AppCompatActivity {
    ArrayList<Map<String, Object>> users = new ArrayList<>();

    public void setFullScreen() {//sets the view to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_leaderboard);

        users = MainActivity.fireStoreHandler.readUsers();


        updateTable(users);

    }

    void updateTable(ArrayList<Map<String, Object>> users) {

        for (Map<String, Object> user : users) {
            String name = "" + user.get("name");
            String score = "" + user.get("score");
            if (!name.equals("null")) {
                addRow(name, score);
            }


        }
    }

    void addRow(String name, String score) {

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);

        tv0.setText("   "+name);
        tv0.setTextColor(Color.GREEN);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText("   " + score);
        tv1.setTextColor(Color.GREEN);

        tbrow0.addView(tv1);
        stk.addView(tbrow0);
    }

}
