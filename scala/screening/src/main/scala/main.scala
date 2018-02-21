package scala;

import java.io.File;
import java.nio.charset.CodingErrorAction
import scala.collection.mutable.ListBuffer;
import scala.io.Source;
import scala.io.Codec;

class Application {

  private val decoder = Codec.UTF8.decoder.onMalformedInput(CodingErrorAction.IGNORE)

  def run(dir: String) {
    val filePathsBuffer = ListBuffer[String]();
    listCsvFiles(dir, filePathsBuffer);
    val filePaths = filePathsBuffer.toList;

    val averageCols = averageColumns(filePaths);
    println(s"Average Columns: ${averageCols}");

    val numRows = numberOfRows(filePaths);
    println(s"Number Of Rows: ${numRows}");

    val counts = wordCount(filePaths);
    println("Word Counts:");
    for (l <- counts.keys) {
      print(l);
      print(',');
      println(counts(l));
    }
  }

  def wordCount(filePaths: List[String]) : Map[String,Int] = {
    var m = Map[String,Int]();
    for (filePath <- filePaths) {
      val f = Source.fromFile(filePath)(decoder);
      for (line <- f.getLines()) {
        val l = line.replaceAll("[\"]", "");
        val arr = l.split("[\\s+,]");
        for (s <- arr) {
          val count = (if (m.contains(s)) m(s) else 0) + 1;
          m += (s -> count);
        }
      }
    }
    return m;
  }

  def averageColumns(filePaths: List[String]) : Int = {
    val numFiles = filePaths.length;
    var numColumns = 0;
    for (filePath <- filePaths) {
      val f = Source.fromFile(filePath)(decoder);
      val line = f.getLines().take(1).toList(0);
      f.close;
      numColumns += line.split(",").length;
    }
    return numColumns / numFiles;
  }

  def numberOfRows(filePaths: List[String]) : Int = {
    var rowSum = 0;
    for (filePath <- filePaths) {
      val f = Source.fromFile(filePath)(decoder);
      for (line <- f.getLines()) {
        rowSum += 1;
      }
      f.close();
    }
    return rowSum;
  }

  def listCsvFiles(dir: String, out: ListBuffer[String]) {
    val folder = new File(dir);
    for (file <- folder.listFiles()) {
      val path = file.getAbsolutePath();
      if (file.isFile() && path.endsWith(".csv")) {
        out += path;
      }
      if (file.isDirectory()) {
        listCsvFiles(path, out);
      }
    }
  }
}

object Main {
  def main(args: Array[String]) {
    val app = new Application();
    app.run(args(0));
  }
}
