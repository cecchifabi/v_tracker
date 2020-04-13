import 'dart:convert';
import 'dart:io';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/cupertino.dart';
import 'package:v_tracker/models/UserInfo.dart';
import 'package:v_tracker/models/user.dart';

class DatabaseService{

  // collection reference
  final CollectionReference userCollection = Firestore.instance.collection('users');

  Future updateUserData(String uid, String firstName, String lastName, bool isInfected, List<Position> listOfPositions) async {
    List<String> tmp = new List<String>();
    for (int i = 0; i < listOfPositions.length; i++){
      tmp.add(jsonEncode(listOfPositions[i]));
    }

    return await userCollection.document(uid).setData({
      'uid': uid,
      'firstName': firstName,
      'lastName': lastName,
      'isInfected': isInfected,
      'listOfPositions': tmp,
    });
  }

  //user info list from snapshot
  List<UserData> _userInfoListFromSnapshot(QuerySnapshot snapshot){
    return snapshot.documents.map((doc){
      List<Position> tmp = new List<Position>();
      if (doc.data['listOfPositions'].length > 0)
      {
        for (int i = 0; i < doc.data['listOfPositions'].length; i++){
          Map positionMap = jsonDecode(doc.data['listOfPositions'][i]);
          tmp.add(Position.fromJson(positionMap));
        }
      }

      return UserData(
        uid: doc.data['uid'],
        firstName: doc.data['firstName'],
        lastName: doc.data['lastName'],
        isInfected: doc.data['isInfected'],
        listOfPositions: tmp
      );
    }).toList();
  }

  //user info from snapshot
  UserData userInfoFromID(String userID) {
    UserData tmpCurrUser = new UserData(uid: "2", firstName: "3", lastName: "4", isInfected: false, listOfPositions: new List<Position>());
    DocumentReference documentReference = userCollection.document(userID);
    List<Position> tmp = new List<Position>();
    tmp.add(new Position(timestamp: "a",latitude: 2,longitude: 2));
    documentReference.get().then((datasnapshot){
    if (datasnapshot.data['listOfPositions'].length > 0)
    {
      for (int i = 0; i < datasnapshot.data['listOfPositions'].length; i++){
        Map positionMap = jsonDecode(datasnapshot.data['listOfPositions'][i]);
        tmp.add(Position.fromJson(positionMap));
      }
    }
    tmpCurrUser = new UserData(uid: datasnapshot.data['uid'], firstName:  datasnapshot.data['firstName'], lastName: datasnapshot.data['lastName'], isInfected:  datasnapshot.data['isInfected'], listOfPositions: tmp);
      return tmpCurrUser;
    });
    return tmpCurrUser;
  }

  //user info from snapshot
  UserData _userInfoFromSnapshot(DocumentSnapshot snapshot){
    return UserData(
      uid: snapshot.data["uid"],
      firstName: snapshot.data['firstName'],
      lastName: snapshot.data['lastName'],
      isInfected: snapshot.data['isInfected'],
      listOfPositions: snapshot.data['listOfPositions']
    );
  }

  // get users stream
  Stream<List<UserData>> get users{
    return userCollection.snapshots()
    .map(_userInfoListFromSnapshot);
  }

  //get user doc stream
  Stream<UserData> get userInfo{
    return userCollection.document().snapshots()
        .map(_userInfoFromSnapshot);
  }
}