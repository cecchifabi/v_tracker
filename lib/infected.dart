import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:barcode_scan/barcode_scan.dart';
import 'package:provider/provider.dart';
import 'dart:async';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/info_covid19.dart';
import 'package:v_tracker/info_v_tracker.dart';
import 'package:v_tracker/filesystem.dart';
import 'package:local_auth/local_auth.dart';
import 'authenticate/authenticate.dart';
import 'models/user.dart';
import 'user_list.dart';
import'services/database.dart';
import 'models/UserInfo.dart';
import 'models/user_tile.dart';

class Infected extends StatefulWidget {
  Infected({Key key, this.title}) : super(key: key);


  final String title;
  final CounterStorage storage = CounterStorage();

  @override
  _InfectedState createState() => _InfectedState();
}

class _InfectedState extends State<Infected> {

  String status = "";
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final LocalAuthentication auth = LocalAuthentication();

  @override
  void initState() {
    super.initState();

  }



  _changeStatus(bool status){


    final user = Provider.of<User>(context);
    final userList = Provider.of<List<UserData>>(context);



    try{
          UserData currUser;
          for (int i = 0; i < userList.length; i++){
            if (user.uid.toString() == userList[i].uid.toString()){
              currUser = userList[i];
            }
          }
          DatabaseService().updateUserData(currUser.uid, currUser.firstName, currUser.lastName, status, currUser.listOfPositions);
          print("\n\nUser changed his status to infected = " + status.toString());
          setState(() {
            this.status = status.toString();
          });


    } catch(e) {

      print('Exception: $e');

    }




  }

  showAlertDialog(BuildContext context, bool infected) async {
    Widget infectedB;
    Widget cancelB;
    AlertDialog alert;

    //CHANGE TO INFECTED STATUS
    if(infected){
      infectedB = FlatButton(
        child: Text("Ok"),
        onPressed: (){
          Navigator.pop(context);
          _changeStatus(true);
        },
      );

      cancelB = FlatButton(
        child: Text("Cancel"),
        onPressed:  () {
          Navigator.pop(context);

        },
      );
      // set up the AlertDialog
      alert = AlertDialog(
        title: Text("Warning: changing status to infected!"),
        content: Text("Your QR code says that you are INFECTED! \nClick ok to confirm the change"),
        actions: [
          infectedB,
          cancelB,
        ],
      );
    }

    //CHANGE TO NOT INFECTED
    else{infectedB = FlatButton(
        child: Text("Ok"),
        onPressed: (){
          Navigator.pop(context);
          _changeStatus(true);
        },
      );

      cancelB = FlatButton(
        child: Text("Cancel"),
        onPressed:  () {
          Navigator.pop(context);

        },
      );
      // set up the AlertDialog
      alert = AlertDialog(
        title: Text("Warning: changing status to healthy!"),
        content: Text("Your QR code says that you are NOT INFECTED! \nClick ok to confirm the change"),
        actions: [
          infectedB,
          cancelB,
        ],
    );

    }
    // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );

  }


  Future _scanQR() async {
    // https://pub.dev/packages/barcode_scan
    /*
    print('####################################################');

    bool canCheckBiometrics = await auth.canCheckBiometrics;
    print('Can chech biometrics? $canCheckBiometrics');

    try {
      bool didAuthenticate = await auth.authenticateWithBiometrics(
        localizedReason: 'Please confirm that you want to change your status',
        useErrorDialogs: true,
        stickyAuth: true,
        );
      if(didAuthenticate)
        showAlertDialog(context);

      print('Autenticated: $didAuthenticate');
    } catch(e) {
      print('Exception: $e');
    }

    print('####################################################');
    //QR CODE ------------------------------------------------
      */
    try {

      String barcode = await BarcodeScanner.scan();

      if(barcode == 'INFECTED' ) {}
        showAlertDialog(context, true);

      if(  barcode == 'HEALTHY') {
        showAlertDialog(context, false);

      }
      else {

        setState(() => this.status = "Invalid QR code. Reload this page to see your previous status.");

      }

    } on PlatformException catch (e) {

      if (e.code == BarcodeScanner.CameraAccessDenied) {

        setState(() {
          this.status = 'The user did not grant the camera permission!';
        });
      } else {

        setState(() => this.status = 'Unknown error: $e');
      }
    } on FormatException{

      setState(() => this.status = 'null (User returned using the "back"-button before scanning anything. Result)');
    
    } catch (e) {

      setState(() => this.status = 'Unknown error: $e');

    }
  }

  @override
  Widget build(BuildContext context) {

    final user = Provider.of<User>(context);
    final userList = Provider.of<List<UserData>>(context);


    //check user
    if (user == null)
    {
      return Authenticate();
    }

    else {
      //GET INITIAL STATUS OF USER
      UserData currUser;
      //widget.storage.read().then((String str) {

      for (int i = 0; i < userList.length; i++) {
        if (user.uid.toString() == userList[i].uid.toString()) {
          currUser = userList[i];
        }
      }
      setState(() {
        this.status = currUser.isInfected.toString();
      });


      return Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
          centerTitle: true,
        ),
        drawer: Drawer(
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
        body: ListView(
          children: <Widget>[
            ListTile(
              leading: Icon(Icons.info_outline),
              title: Text('COVID-19 symptoms'),
            ),
            ListTile(
              leading: Icon(Icons.looks_one),
              title: Text('Cough'),
            ),
            ListTile(
              leading: Icon(Icons.looks_two),
              title: Text('Fever'),
            ),
            ListTile(
              leading: Icon(Icons.looks_3),
              title: Text('Tiredness'),
            ),
            ListTile(
              leading: Icon(Icons.looks_4),
              title: Text('Difficulty breathing (severe cases)'),
            ),
            ListTile(
              leading: Icon(Icons.arrow_right),
              title: Text("Infected: " + status.toUpperCase() ),
            ),
          ],
        ),
        floatingActionButton: FloatingActionButton.extended(
          onPressed: () {
            _scanQR();

          },
          tooltip: 'Scan QR code',
          icon: Icon(Icons.settings_overscan),
          label: Text('Change your status'),
        ),
      );
    }
  }
}