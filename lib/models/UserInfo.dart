import 'package:google_maps_flutter/google_maps_flutter.dart';

class UserData{

  String uid;
  String firstName;
  String lastName;
  bool isInfected;
  List<Position> listOfPositions;

  UserData({ this.uid, this.firstName, this.lastName, this.isInfected, this.listOfPositions});

}

class Position{

  String timestamp;
  LatLng position;

  Position({this.timestamp, this.position});

}