import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:location/location.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:typed_data';
import 'dart:async';
import 'package:v_tracker/infected.dart';
import 'package:v_tracker/info_covid19.dart';
import 'package:v_tracker/info_v_tracker.dart';

class HomePage extends StatefulWidget {
  HomePage({Key key, this.title}) : super(key: key);

  // This (stateful) widget is the home page of the application.

  final String title;

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  // Needed for Maps
  StreamSubscription _locationSubscription;
  GoogleMapController _controller;
  Location _locationTracker = Location();
  Marker marker;
  Circle circle;

  static final CameraPosition _initialPosition = CameraPosition(

    target: LatLng(0, 0),
    zoom: 14

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
    try{

      Uint8List imageData = await getMarker();

      // Check if have permissions
      //bool _serviceEnabled = await local.serviceEnabled();

      var location = await _locationTracker.getLocation();
      updateMarkerAndCircle(location, imageData);

      if(_locationSubscription != null) {
        _locationSubscription.cancel();
      }
      _locationSubscription = _locationTracker.onLocationChanged.listen((newLocalData) {
        if(_controller != null) {
          _controller.animateCamera(CameraUpdate.newCameraPosition(new CameraPosition(
            bearing: 192.8334901395799,
            target: LatLng(newLocalData.latitude, newLocalData.longitude),
            tilt: 0,
            zoom: 18
            )));
          updateMarkerAndCircle(newLocalData, imageData);
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

    return Scaffold(
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
              onTap: (){
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => HomePage(title: 'V Tracker')),
                );
              },
            ),
            ListTile(
              leading: Icon(Icons.report_problem),
              title: Text('Report an infection'),
              onTap: (){
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => Infected(title: 'Report an infection')),
                );
              },
            ),
            ListTile(
              leading: Icon(Icons.announcement),
              title: Text('COVID-19'),
              onTap: (){
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => InfoCOVID19(title: 'COVID-19 info', address: 'WebView of the address https://www.worldometers.info/coronavirus/')),
                );
              },
            ),
            Divider(
              height: 4.0,
            ),
            ListTile(
              leading: Icon(Icons.info_outline),
              title: Text('Info on this App'),
              onTap: (){
                Navigator.push(
                  context,
                  MaterialPageRoute(builder: (context) => InfoVTracker(title: 'V Tracker info')),
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
    );
  }
}