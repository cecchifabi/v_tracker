import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:v_tracker/models/UserInfo.dart';
import 'package:v_tracker/models/user.dart';

class DatabaseService{

  // collection reference
  final CollectionReference userCollection = Firestore.instance.collection('users');

  Future updateUserData(String uid, String firstName, String lastName) async {
    return await userCollection.document(uid).setData({
      'uid': uid,
      'firstName': firstName,
      'lastName': lastName,
      'isInfected': false,
      'listOfPositions': new List<Position>(),
    });
  }

  //user info list from snapshot
  List<UserData> _userInfoListFromSnapshot(QuerySnapshot snapshot){
    return snapshot.documents.map((doc){
      List<Position> tmp = null;
      if (doc.data['listOfPositions'] != null)
      {
        tmp = doc.data['listOfPositions'].cast<Position>();
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
  UserData _userInfoFromSnapshot(DocumentSnapshot snapshot){
    return UserData(
      uid: snapshot.data['uid'],
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