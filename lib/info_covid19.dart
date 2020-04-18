import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/infected.dart';
import 'package:v_tracker/info_v_tracker.dart';
import 'authenticate/authenticate.dart';
import 'models/user.dart';
import 'user_list.dart';

/// Webview with info about the pandemic.
class InfoCOVID19 extends StatefulWidget {
  InfoCOVID19({Key key, this.title, this.address}) : super(key: key);

  /// Title of the page.
  final String title;
  /// Address of the webpage.
  final String address;

  @override
  _InfoCOVID19State createState() => _InfoCOVID19State();
}

class _InfoCOVID19State extends State<InfoCOVID19> {

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
        body: WebView(
          initialUrl: "https://www.worldometers.info/coronavirus/",
          javascriptMode: JavascriptMode.unrestricted,
        ),
      );
    }
  }
}