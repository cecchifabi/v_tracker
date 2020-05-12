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
import 'services/database.dart';
import 'models/UserInfo.dart';
import 'package:sensors/sensors.dart';
import 'dart:math';

/// Home page of the application.
/// 
/// Shows the map with the markers for the position
/// of the current user and for the positions of
/// the infected users.
class HomePage extends StatefulWidget {
  HomePage({Key key, this.title}) : super(key: key);

  /// Title of the page.
  final String title;

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  /// Authorisation for FireBase.
  final FirebaseAuth _auth = FirebaseAuth.instance;

  /// Needed for Maps.
  StreamSubscription _locationSubscription;
  /// Needed for Maps.
  GoogleMapController _controller;

  /// Tracker for the current location.
  Location _locationTracker = Location();

  /// Marker for the current user.
  Marker marker;

  /// Circle for the current user, for the precision of the location.
  Circle circle;

  /// Markers for the infected users.
  Set<Marker> markers = new Set<Marker>();

  /// For movement checking.
  double xOld = 0, xCurr = 0;
  double yOld = 0, yCurr = 0;
  double zOld = 0, zCurr = 0;
  double magnitude = 0;

  /// Initial position of the camera
  static final CameraPosition _initialPosition = CameraPosition(

    target: LatLng(0, 0),
    zoom: 1

  );

  /// Gets the marker from an image.
  Future<Uint8List> getMarker(String image) async {

    ByteData byteData = await DefaultAssetBundle.of(context).load(image);
    return byteData.buffer.asUint8List();

  }

  /// Show the markers for the infected users, for the last 4 days.
  void showInfected() async{

    List<LatLng> infected = new List<LatLng>();
    List<String> infectedTime = new List<String>();

    final user = Provider.of<User>(context);
    final userList = Provider.of<List<UserData>>(context);

    for (int i = 0; i < userList.length; i++){
      if ( user.uid.toString() != userList[i].uid.toString() && userList[i].isInfected){
        // If the date is 4 days or more less older it gets added to show to user that an infected person was in that place
        if( DateTime.now().difference(DateTime.parse(userList[i].listOfPositions.last.timestamp )).inDays <= 4)

          infected.add( LatLng(userList[i].listOfPositions.last.latitude , (userList[i].listOfPositions.last.longitude) ));
          infectedTime.add(userList[i].listOfPositions.last.timestamp);
      }
    }

   Uint8List icon = await getMarker("assets/virus_1.png");

    for (int i = 0; i < infected.length; i++){

      // Add infected markers
      this.setState(() {
        marker = Marker(
            markerId: MarkerId("Infected"+i.toString()),
              position: infected[i],
              draggable: false,
              zIndex: -1,

              infoWindow: InfoWindow(
                title: infectedTime[i].split(" ")[0],
                //snippet: infectedTime[i].split(" ")[1].split(".")[0]
            ),
            flat: true,
            anchor: Offset(0.5, 0.5),
            icon: BitmapDescriptor.fromBytes(icon));
      });
      markers.add(marker);

    }

    print("\n");
  }
  /*  void showInfo(Marker marker){
      marker.showInfoWindow();
      marker.infoWindow.
    }*/


  void updateMarkerAndCircle(String markerID, bool circleMarker, LocationData newLocalData, Uint8List imageData) {

    LatLng latlng = LatLng(newLocalData.latitude, newLocalData.longitude);
    this.setState(() {
      marker = Marker(
        markerId: MarkerId(markerID),
        position: latlng,
        rotation: newLocalData.heading,
        draggable: false,
        zIndex: 2,
        flat: true,
        anchor: Offset(0.5, 0.5),
        icon: BitmapDescriptor.fromBytes(imageData)
      );
      if(circleMarker) {
        circle = Circle(
            circleId: CircleId(markerID),
            radius: newLocalData.accuracy,
            zIndex: 1,
            strokeWidth: 0,
            center: latlng,
            fillColor: Colors.white.withAlpha(150)
        );
      }
      markers.add(marker);
    });

  }

  void _getLocation() async {




    final user = Provider.of<User>(context);
    final userList = Provider.of<List<UserData>>(context);



    try{


      Uint8List imageData = await getMarker("assets/user_red_2.png");

      // Check if have permissions
      //bool _serviceEnabled = await local.serviceEnabled();

      var location = await _locationTracker.getLocation();
      updateMarkerAndCircle("Home", true,location, imageData);

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
          updateMarkerAndCircle("Home", true, newLocalData, imageData);

          // Update user's database with the new location only if the device is moving
          accelerometerEvents.listen((AccelerometerEvent event) {

            magnitude = sqrt(pow(event.x, 2) + pow(event.y, 2) + pow(event.z, 2));
            xCurr = event.x;
            yCurr = event.y;
            zCurr = event.z;

          });

          print('Magnitude out: ' + magnitude.toString());
          print('xCurr: ' + xCurr.toString());
          print('yCurr: ' + yCurr.toString());
          print('zCurr: ' + zCurr.toString());

          if(magnitude > 10) {

            UserData currUser;
            for (int i = 0; i < userList.length; i++){
              if (user.uid.toString() == userList[i].uid.toString()){
                currUser = userList[i];
                break;
              }
            }
            Position tmpPos = new Position(timestamp: DateTime.now().toString(), latitude: newLocalData.latitude, longitude: newLocalData.longitude);

            currUser.listOfPositions.add(tmpPos);

            DatabaseService().updateUserData(currUser.uid, currUser.firstName, currUser.lastName, currUser.isInfected, currUser.listOfPositions);

            print('\n\n\nLocation sent!!!');

          }
          else {

            print("\n\n\nLocation changed, but magnitude was not great enough!!!");
            
          }
        }
      });

    } catch(e) {

      print('Exception: $e');

    }


    showInfected();

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
    //final userList = Provider.of<List<UserData>>(context);



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
              markers: markers,
              //Set.of((marker != null) ? [marker] : []),
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
