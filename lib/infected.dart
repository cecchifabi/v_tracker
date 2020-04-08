import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:barcode_scan/barcode_scan.dart';
import 'dart:async';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/info_covid19.dart';
import 'package:v_tracker/info_v_tracker.dart';
import 'package:v_tracker/filesystem.dart';
import 'package:local_auth/local_auth.dart';

class Infected extends StatefulWidget {
  Infected({Key key, this.title}) : super(key: key);

  final String title;
  final CounterStorage storage = CounterStorage();

  @override
  _InfectedState createState() => _InfectedState();
}

class _InfectedState extends State<Infected> {

  String status = "";
  final LocalAuthentication auth = LocalAuthentication();

  @override
  void initState() {
    super.initState();
    widget.storage.read().then((String str) {
      setState(() {
        this.status = "Status: " + str;
      });
    });
  }

  Future _scanQR() async {
    // https://pub.dev/packages/barcode_scan

    print('####################################################');

    bool canCheckBiometrics = await auth.canCheckBiometrics;
    print('Can chech biometrics? $canCheckBiometrics');

    try {
      bool didAuthenticate = await auth.authenticateWithBiometrics(
        localizedReason: 'Please confirm that you want to change your status',
        useErrorDialogs: true,
        stickyAuth: true,
        );
      print('Autenticated: $didAuthenticate');
    } catch(e) {
      print('Exception: $e');
    }

    print('####################################################');

    try {

      String barcode = await BarcodeScanner.scan();

      if(barcode == 'INFECTED' || barcode == 'RECOVERED' || barcode == 'HEALTHY') {

        // Save the information on a file
        widget.storage.write(barcode);

        setState(() => this.status = "Status: " + barcode);
      
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
            title: Text(status),
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