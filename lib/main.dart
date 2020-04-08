import 'package:flutter/material.dart';
import 'package:v_tracker/home_page.dart';

void main() => runApp(App());

class App extends StatelessWidget {
  // This widget is the root of the application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.purple,
      ),
      home: HomePage(title: 'V Tracker'),
    );
  }
}