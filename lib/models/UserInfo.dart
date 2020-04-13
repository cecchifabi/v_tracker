import 'package:google_maps_flutter/google_maps_flutter.dart';

class UserData{

  String uid;
  String firstName;
  String lastName;
  bool isInfected;
  List<Position> listOfPositions;

  UserData ({this.uid, this.firstName, this.lastName, this.isInfected, this.listOfPositions});
}

class Position{

  String timestamp;
  double latitude;
  double longitude;

  Position({this.timestamp, this.latitude, this.longitude});

  Position.fromJson(Map<String, dynamic> json)
    :  timestamp = json['timestamp'],
       latitude = json['latitude'],
       longitude = json['longitude'];

  Map<String, dynamic> toJson() =>
      {
        'timestamp': timestamp,
        'latitude': latitude,
        'longitude': longitude
      };
}