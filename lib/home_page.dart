import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:location/location.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:typed_data';
import 'dart:async';
import 'package:v_tracker/infected.dart';
import 'package:v_tracker/info_covid19.dart';
import 'package:v_tracker/info_v_tracker.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/services/auth.dart';
import 'package:v_tracker/services/database.dart';
import 'package:v_tracker/authenticate/authenticate.dart';
import 'package:v_tracker/models/user.dart';
import 'package:v_tracker/user_list.dart';
import 'package:v_tracker/models/UserInfo.dart';



class HomePage extends StatefulWidget {
  HomePage({Key key, this.title}) : super(key: key);

  // This (stateful) widget is the home page of the application.

  final String title;

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  final FirebaseAuth _auth = FirebaseAuth.instance;

  // Needed for Maps
  StreamSubscription _locationSubscription;
  GoogleMapController _controller;
  Location _locationTracker = Location();
  Marker marker;
  Circle circle;

  static final CameraPosition _initialPosition = CameraPosition(

    target: LatLng(0, 0),
    zoom: 18

  );

  Future<Uint8List> getMarker() async {

    ByteData byteData = await DefaultAssetBundle.of(context).load("assets/smile.png");
    return byteData.buffer.asUint8List();

  }

  void updateMarkerAndCircle(LocationData newLocalData, Uint8List imageData) {

    LatLng latlng = LatLng(newLocalData.latitude, newLocalData.longitude);
    this.setState(() {
      marker = Marker(
        markerId: MarkerId('Home'),
        position: latlng,
        rotation: newLocalData.heading,
        draggable: false,
        zIndex: 2,
        flat: true,
        anchor: Offset(0.5, 0.5),
        icon: BitmapDescriptor.fromBytes(imageData)
      );
      circle = Circle(
        circleId: CircleId('Person'),
        radius: newLocalData.accuracy,
        zIndex: 1,
        strokeColor: Colors.blue,
        center: latlng,
        fillColor: Colors.blue.withAlpha(70)
      );
    });

  }

  void _getLocation() async {

    final user = Provider.of<User>(context);
    final userList = Provider.of<List<UserData>>(context);

    try{

      Uint8List imageData = await getMarker();

      // Check if have permissions
      //bool _serviceEnabled = await local.serviceEnabled();

      var location = await _locationTracker.getLocation();
      updateMarkerAndCircle(location, imageData);

      if(_controller != null) {
        _controller.animateCamera(CameraUpdate.newCameraPosition(new CameraPosition(
          bearing: 192.8334901395799,
          target: LatLng(location.latitude, location.longitude),
          tilt: 0,
          zoom: 18
        )));
      }

      if(_locationSubscription != null) {
        _locationSubscription.cancel();
      }
      _locationSubscription = _locationTracker.onLocationChanged.listen((newLocalData) {
        if(_controller != null) {
          updateMarkerAndCircle(newLocalData, imageData);

          /*
          * PUT HERE THE CODE NEEDED WHEN A CHANGE OF LOCATION IS DETECTED
          * newLocalData is the new location. It has the following arìttributes:
          *   final double latitude; // Latitude, in degrees
          *   final double longitude; // Longitude, in degrees
          *   final double accuracy; // Estimated horizontal accuracy of this location, radial, in meters
          *   final double altitude; // In meters above the WGS 84 reference ellipsoid
          *   final double speed; // In meters/second
          *   final double speedAccuracy; // In meters/second, always 0 on iOS
          *   final double heading; //Heading is the horizontal direction of travel of this device, in degrees
          *   final double time; //timestamp
          */

          // Update user's database with the new location
          UserData currUser;
          for (int i = 0; i < userList.length; i++){
            if (user.uid.toString() == userList[i].uid.toString()){
           currUser = userList[i];
           }
          }
          Position tmpPos = new Position(timestamp: DateTime.now().toString(), latitude: newLocalData.latitude, longitude: newLocalData.longitude);

          currUser.listOfPositions.add(tmpPos);

          DatabaseService().updateUserData(currUser.uid, currUser.firstName, currUser.lastName, currUser.isInfected, currUser.listOfPositions);

          print("\n\n\nLocation changed!!!");
          print("New location: ${newLocalData.latitude}, ${newLocalData.longitude}, ${newLocalData.time}\n\n\n");
        }
      });

    } catch(e) {

      print('Exception: $e');

    }
  }

  @override
  void dispose() {

    if(_locationSubscription != null) {
      _locationSubscription.cancel();
    }
    super.dispose();
    
  }

  // This method is rerun every time setState is called
  @override
  Widget build(BuildContext context) {

    final user = Provider.of<User>(context);
    final userList = Provider.of<List<UserData>>(context);



    //Timer.periodic(Duration(seconds:1), (Timer t) => print("xd" + DateTime.now().toString()));

    //check user
    if (user == null)
    {
      return Authenticate();
    }
    else {
      return StreamProvider<List<UserData>>.value(
          value: DatabaseService().users,
          child: StreamProvider<User>.value(
            value: AuthService().user,
            child: Scaffold(
              appBar: AppBar(
                title: Text(widget.title),
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
              body: GoogleMap(
              mapType: MapType.hybrid,
              initialCameraPosition: _initialPosition,
              //markers:  Set<Marker>.of(markers.values),
              markers: Set.of((marker != null) ? [marker] : []),
              circles: Set.of((circle != null) ? [circle] : []),
              onMapCreated: (GoogleMapController controller) {
                _controller = controller;
              },
            ),
              floatingActionButton: FloatingActionButton(
                onPressed: () {
                  _getLocation();
                },
                tooltip: 'Get Location',
                child: Icon(Icons.my_location),
              ),
            ),
          ),
        );
    }
  }
}
