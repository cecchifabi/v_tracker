package com.v_tracker.ui.Database;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.v_tracker.ui.models.Position;
import com.v_tracker.ui.models.User;

import java.util.List;

public class Database {
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
}
