import 'package:v_tracker/models/UserInfo.dart';
import 'package:v_tracker/models/user.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:v_tracker/services/database.dart';

/// Provides the suthentication services for the users.
class AuthService{

  final FirebaseAuth _auth = FirebaseAuth.instance;

  // create user obj based on FirebaseUser
  User _userFromFirebaseUser(FirebaseUser user){
  return user != null ? User(uid: user.uid) : null;
  }

  /// auth change user stream
  Stream<User> get user{
    return _auth.onAuthStateChanged
    .map((FirebaseUser user) => _userFromFirebaseUser(user));
  }

  //sign in with email and password
  Future signInWithEmailAndPassword(String email, String password) async {
    try{
      AuthResult result = await _auth.signInWithEmailAndPassword(email: email, password: password);
      FirebaseUser user = result.user;
      return _userFromFirebaseUser(user);
    } catch(e){
      print(e.toString());
      return null;
    }
  }

  //register with email and password
  Future registerWithEmailAndPassword(String email, String password, String firstName, String lastName) async {
    try{
      AuthResult result = await _auth.createUserWithEmailAndPassword(email: email, password: password);
      FirebaseUser user = result.user;

      // create a new document for the user with the uid
      await DatabaseService().updateUserData(user.uid, firstName, lastName, false, new List<Position>());

      return _userFromFirebaseUser(user);
    } catch(e){
      print(e.toString());
      return null;
    }
  }

  // sign out
  Future signOut() async {
    try{
      return await _auth.signOut();
    }
    catch (error) {
      print(error.toString());
      return null;
    }
  }
}