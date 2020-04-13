import 'dart:io';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/authenticate/authenticate.dart';
import 'package:v_tracker/models/user.dart';
import 'package:v_tracker/models/UserInfo.dart';
import 'package:v_tracker/models/user_tile.dart';
import 'package:v_tracker/services/auth.dart';
import 'package:v_tracker/services/database.dart';

import 'home_page.dart';
import 'infected.dart';
import 'info_covid19.dart';
import 'info_v_tracker.dart';

class UserList extends StatefulWidget {
  @override
  _UserListState createState() => _UserListState();
}

class _UserListState extends State<UserList> {
  @override
  Widget build(BuildContext context) {



  final FirebaseAuth _auth = FirebaseAuth.instance;
  final userList = Provider.of<List<UserData>>(context);
  final user = Provider.of<User>(context);

if (user == null){
  return Authenticate();
}
else {
  // Get user's current listOfPositions
  UserData display;
  for (int i = 0; i < userList.length; i++){
    if (user.uid.toString() == userList[i].uid.toString()){
      display = userList[i];
    }
  }
  print(display.listOfPositions.length);

  return StreamProvider<List<UserData>>.value(
    value: DatabaseService().users,
    child: StreamProvider<User>.value(
      value: AuthService().user,
      child: Scaffold(
        appBar: AppBar(
          title: Text('List of Positions'),
          centerTitle: true,
        ),
        drawer: Drawer(
          // Set the children for the drawer.
          child: ListView(
            children: <Widget>[
              ListTile(
                leading: Icon(Icons.home),
                title: Text('Home'),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => HomePage(title: 'V Tracker')),
                  );
                },
              ),
              ListTile(
                leading: Icon(Icons.report_problem),
                title: Text('Report an infection'),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) =>
                        Infected(title: 'Report an infection')),
                  );
                },
              ),
              ListTile(
                leading: Icon(Icons.announcement),
                title: Text('COVID-19'),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) =>
                        InfoCOVID19(title: 'COVID-19 info',
                            address: 'WebView of the address https://www.worldometers.info/coronavirus/')),
                  );
                },
              ),
              ListTile(
                leading: Icon(Icons.list),
                title: Text('User Tracker Info'),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) =>
                        UserList(),
                    ),
                  );
                },
              ),
              ListTile(
                leading: Icon(Icons.person),
                title: Text('Sign Out'),
                onTap: () async {
                  await _auth.signOut();
                }, //onTap
              ),
              Divider(
                height: 4.0,
              ),
              ListTile(
                leading: Icon(Icons.info_outline),
                title: Text('Info on this App'),
                onTap: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(builder: (context) =>
                        InfoVTracker(title: 'V Tracker info')),
                  );
                },
              ),
            ],
          ),
        ),
        body: ListView.builder(
            itemCount: display.listOfPositions.length,
            itemBuilder: (context, index) {
              return UserTile(position: display.listOfPositions[index]);
            }
        ),
      ),
    ),
  );
}
  }
}
