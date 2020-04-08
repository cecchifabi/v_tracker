import 'dart:async';
import 'dart:io';
import 'package:path_provider/path_provider.dart';

class CounterStorage {
  Future<String> get _localPath async {
    final directory = await getApplicationDocumentsDirectory();

    return directory.path;
  }

  Future<File> get _localFile async {
    final path = await _localPath;
    return File('$path/counter.txt');
  }

  Future<String> read() async {
    try {
      final file = await _localFile;

      // Read the file
      String contents = await file.readAsString();

      return contents;
    } catch (e) {
      
      return 'UKNOWN (have you ever set a status?)';
    }
  }

  Future<File> write(String str) async {
    final file = await _localFile;

    // Write the file
    return file.writeAsString('$str');
  }
}