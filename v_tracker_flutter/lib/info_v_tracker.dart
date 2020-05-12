import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/info_covid19.dart';
import 'package:v_tracker/infected.dart';
import 'authenticate/authenticate.dart';
import 'models/user.dart';
import 'user_list.dart';

/// Info about the app.
class InfoVTracker extends StatefulWidget {
  InfoVTracker({Key key, this.title}) : super(key: key);

  /// Title of the page
  final String title;

  @override
  _InfoVTrackerState createState() => _InfoVTrackerState();
}

class _InfoVTrackerState extends State<InfoVTracker> {

  final FirebaseAuth _auth = FirebaseAuth.instance;

  @override
  Widget build(BuildContext context) {

    final user = Provider.of<User>(context);

    //check user
    if (user == null)
    {
      return Authenticate();
    }
    else {
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
        body:Column(
          children: [Container(
              padding: EdgeInsets.fromLTRB(0, 10, 0, 0),
              child: Center(
                  child: Text(
                    'App made for subject Computação Móvel',
                    textAlign: TextAlign.center,
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                  )
              )
          ),
            Container(
                padding: EdgeInsets.fromLTRB(0, 10, 0, 0),
                child: Center(
                    child: Text(
                      'Engenharia de Computadores e Telemática',
                      textAlign: TextAlign.center,
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    )

                )
            ),
            Container(
                padding: EdgeInsets.fromLTRB(0, 10, 0, 0),
                child: Center(
                    child: Text(
                      'Universidade de Aveiro',
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    )
                )
            ),
            Container(
                padding: EdgeInsets.fromLTRB(0, 20, 0, 0),
                child: Center(
                    child: Text(
                      'Students List',
                      style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                    )
                )
            ),
            Container(
                padding: EdgeInsets.fromLTRB(0, 10, 0, 0),
                child: Center(
                    child: Text(
                      'Tiago Feitor\t 85134\nRui Silva\t85135\nFabio Cecchinato\t100720',
                      style: TextStyle(fontSize: 14),
                    )
                )
            ),
            Container(
                padding: EdgeInsets.fromLTRB(20, 20, 20, 0),
                     child: Text(
                        'This app saves the locations where the user went by and stores it in the database.\n'
                            'If at any point the user gets infected, he/she can change its status and the current list of positions'
                            ' will be displayed on the map, with the locations and timestamps, so everyone else knows what places are safe to go.\n'
                            'A user can at any point change its status with the use of the correct QR Code (one to set the state to infected and'
                            ' another one to set the state to not infected.',
                        textAlign: TextAlign.justify,
                        style: TextStyle(fontSize: 14),
                      )
                 )
              ],
          ),
        );
    }
  }
}