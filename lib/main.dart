import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/home_page.dart';
    import 'package:provider/provider.dart';
import 'package:v_tracker/services/auth.dart';
import 'models/user.dart';



void main() => runApp(App());

class App extends StatelessWidget {
  // This widget is the root of the application.
  //return SignIn();

  @override
  Widget build(BuildContext context) {
    return StreamProvider<User>.value(
      value: AuthService().user,
      child: MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          primarySwatch: Colors.purple,
        ),
        home: HomePage(title: 'V Tracker'),
      ),
    );
  }
}