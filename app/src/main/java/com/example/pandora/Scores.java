package com.example.pandora;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.prefs.PreferencesFactory;

public class Scores   {
    Map<String, Object> user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    Scores(Activity context) {
        user = new HashMap<>();
        sharedPref = context.getPreferences(Context.MODE_PRIVATE);
    }

    Map<String, Object> readUserFromFile() {
        int highScore = sharedPref.getInt("score", -1);
        String name = sharedPref.getString("name", "null");
        Log.i("log", "file:" + name);
        Map<String, Object> saveUser = new HashMap<String, Object>();
        saveUser.put("name", name);
        saveUser.put("score", highScore);
        Log.i("log", "" + saveUser);
        return saveUser;

    }


    void pushScoreToFireStore(String name, Object score) {

        user = new HashMap<>();
        user.put("name", name);
        user.put("score", score);
        db.collection("users").document(name)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("log", "document updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });


    }

    void saveUsers(ArrayList<Map<String,Object>> list){
        editor = sharedPref.edit();
        Gson gson=new Gson();
        String json=gson.toJson(list);
        MainActivity.log(json);
        editor.putString("users",json);
        editor.apply();
    }


    public void getScores() {


        db.collection("users")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            return;
                        }

                        ArrayList<Map<String,Object>> users = new ArrayList<>();
                      /*  Map<String,Object> user=new HashMap<>();*/
                        for (QueryDocumentSnapshot doc : value) {
                            users.add(doc.getData());
                        }
                        MainActivity.log(users+"");
                       saveUsers(users);



                    }
                });

    }

}
