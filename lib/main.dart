import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/models/UserInfo.dart';
import 'package:v_tracker/services/auth.dart';
import 'package:v_tracker/services/database.dart';
import 'package:v_tracker/models/user.dart';
import 'package:android_alarm_manager/android_alarm_manager.dart';
/*import 'package:geolocator/geolocator.dart';*/

/*
/// Get the current location
/// 
/// It runs in a different isolate, that should be active even in background
void callback() async {

  print("\n\nRunning in background (in another isolate)!");

  Geolocator geoLocator = Geolocator();

  /* Get the current location:
   * have to use GeoLocator because Location doesn't work here,
   * don't know why...
   */

    try {
      
     //print(DB.DatabaseService().test());

      //var _position = await geoLocator.getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
     // print("\n\nLat: ${_position.latitude}, Lon: ${_position.longitude}, Time: ${_position.timestamp}");

      // Update user's database with the new location
      
      // ...Put the code here. Be careful that this piece of code is running on another isolated
      // (different portion of memory), and cannot share info with the rest of the program unless you
      // manage how to exchange messages between the two, but it's very complicated.

    } catch(e) {

      print('Exception on GeoLocator: $e');

    }
    
}
*/

void main() async {

  /*WidgetsFlutterBinding.ensureInitialized();*/

  runApp(App());

  /*await AndroidAlarmManager.initialize();
  await AndroidAlarmManager.periodic(const Duration(seconds: 10), 0, callback);*/

}

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