package com.v_tracker.ui.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.v_tracker.ui.models.Position;
import com.v_tracker.ui.models.User;

import java.util.ArrayList;
import java.util.List;

public class Database {
    boolean isInfected;
    static List<Position> list = new ArrayList<>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void updateUser(User user){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).set(user);
    }

    public void addNewPosition(final Position position){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.addNewPosition(position);
                updateUser(user);
            }
        });
    }

    public List<Position> getListOfPositions() {
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               User user = documentSnapshot.toObject(User.class);
               list = user.getListOfPositions();
               for (int i = 0; i < user.getListOfPositions().size(); i++){
                   System.out.println("LATITUDE=" + user.getListOfPositions().get(i).getLatitude());
                   System.out.println("LONGITUDE=" + user.getListOfPositions().get(i).getLongitude());
                   System.out.println("TIMESTAMP=" + user.getListOfPositions().get(i).getTimestamp());
               }
            }
        });
        return list;
    }

    public void getAllUsers(){
        List<User> listOfUsers = new ArrayList<>();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        listOfUsers.add(user);
                        Log.d("USER ID", "USER ID= " + user.getUid());
                    }
                }
            }
        });

    }
    
    public boolean getState(){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                isInfected = user.getIsInfected();
            }
        });
        return isInfected;
    }

    public void updateState(boolean isInfected){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.setInfected(isInfected);
                updateUser(user);
            }
        });
    }
}
