import 'package:flutter/material.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'package:v_tracker/home_page.dart';
import 'package:v_tracker/infected.dart';
import 'package:v_tracker/info_v_tracker.dart';

class InfoCOVID19 extends StatefulWidget {
  InfoCOVID19({Key key, this.title, this.address}) : super(key: key);

  final String title;
  final String address;

  @override
  _InfoCOVID19State createState() => _InfoCOVID19State();
}

class _InfoCOVID19State extends State<InfoCOVID19> {
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
      body: WebView(
        initialUrl: "https://www.worldometers.info/coronavirus/",
        javascriptMode: JavascriptMode.unrestricted,
      ),
    );
  }
}