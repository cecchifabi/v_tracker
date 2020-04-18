//import 'package:google_maps_flutter/google_maps_flutter.dart';

class UserData{

  String uid;
  String firstName;
  String lastName;
  bool isInfected;
  List<Position> listOfPositions;

  UserData ({this.uid, this.firstName, this.lastName, this.isInfected, this.listOfPositions});
}

/// Encodes the position for a user.
class Position{

  /// Timestamp.
  String timestamp;
  /// Latitude.
  double latitude;
  /// Longitude.
  double longitude;

  Position({this.timestamp, this.latitude, this.longitude});

  Position.fromJson(Map<String, dynamic> json)
    :  timestamp = json['timestamp'],
       latitude = json['latitude'],
       longitude = json['longitude'];

  /// Converts the Position object in a Json format.
  Map<String, dynamic> toJson() =>
      {
        'timestamp': timestamp,
        'latitude': latitude,
        'longitude': longitude
      };
}