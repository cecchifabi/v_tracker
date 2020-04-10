import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/models/UserInfo.dart';
import 'package:v_tracker/services/auth.dart';
import 'package:v_tracker/services/database.dart';
import 'package:v_tracker/models/user.dart';



void main() => runApp(App());

class App extends StatelessWidget{

   // This widget is the root of the application.
  @override
  Widget build(BuildContext context) {
    return StreamProvider<List<UserData>>.value(
      value: DatabaseService().users,
      child: StreamProvider<User>.value(
        value: AuthService().user,
        child: MaterialApp(
          title: 'Flutter Demo',
          theme: ThemeData(
            primarySwatch: Colors.purple,
          ),
          home: HomePage(title: 'V Tracker'),
        ),
      ),
    );
  }
}