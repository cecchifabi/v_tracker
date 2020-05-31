package com.v_tracker.ui.Database;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.v_tracker.ui.models.Position;
import com.v_tracker.ui.models.User;

import java.util.List;

public class Database {
    boolean isInfected;
    List<Position> list;
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

    public List<Position> getListOfPositions(){
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               User user = documentSnapshot.toObject(User.class);
               list = user.getListOfPositions();
            }
        });
        return list;
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
