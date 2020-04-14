import 'package:flutter/material.dart';
import 'package:v_tracker/models/user.dart';
import 'package:v_tracker/models/UserInfo.dart';
import 'package:geolocator/geolocator.dart' as GEO;

class UserTile extends StatefulWidget {
  UserTile({Key key, this.position}) : super(key: key);

  final Position position;

  @override
  _UserTileState createState() => _UserTileState();
}

class _UserTileState extends State<UserTile> {

  String _subtitle = "";

  Future<String> _getAddress(double lat, double long) async {

    List<GEO.Placemark> placemarks = await GEO.Geolocator()
        .placemarkFromCoordinates(lat, long);
    if (placemarks != null && placemarks.isNotEmpty) {
      final GEO.Placemark pos = placemarks[0];
      setState(() {
        _subtitle = pos.thoroughfare + ', ' + pos.locality;
      });
      return  pos.thoroughfare + ', ' + pos.locality;
    }
    return "";
  }


  @override
    Widget build(BuildContext context) {
      _getAddress(widget.position.latitude,widget.position.longitude);
      return Padding(
          padding: EdgeInsets.only(top: 8.0),
          child: Card(
              margin: EdgeInsets.fromLTRB(20.0, 6.0, 2.0, 0.0),
              child: ListTile(
                title: Text('Date and Time: ${widget.position.timestamp}'),
                subtitle: Text( _subtitle ),
              )
          )
      );
    }
  }


