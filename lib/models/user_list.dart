import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/models/user.dart';
import 'package:v_tracker/models/UserInfo.dart';

class UserList extends StatefulWidget {
  @override
  _UserListState createState() => _UserListState();
}

class _UserListState extends State<UserList> {
  @override
  Widget build(BuildContext context) {

  final userList = Provider.of<List<UserData>>(context);
  final user = Provider.of<User>(context);

  print("user_uid=" + user.uid);
  for (int i = 0; i < userList.length; i++){
    print("Position= " + userList[i]);
  }



    return Container();
  }
}
