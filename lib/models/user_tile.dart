import 'package:flutter/material.dart';
import 'package:v_tracker/models/user.dart';
import 'package:v_tracker/models/UserInfo.dart';

class UserTile extends StatefulWidget {
  UserTile({Key key, this.position}) : super(key: key);

  final Position position;

  @override
  _UserTileState createState() => _UserTileState();
}

class _UserTileState extends State<UserTile> {

  @override
    Widget build(BuildContext context) {
      return Padding(
          padding: EdgeInsets.only(top: 8.0),
          child: Card(
              margin: EdgeInsets.fromLTRB(20.0, 6.0, 2.0, 0.0),
              child: ListTile(
                title: Text('Date and Time: ${widget.position.timestamp}'),
                subtitle: Text('Lat: ${widget.position.latitude} Lon: ${widget.position.longitude}'),
              )
          )
      );
    }
  }


