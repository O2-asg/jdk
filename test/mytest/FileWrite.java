import java.io.FileWriter;

public class FileWrite {

  public static void main(String args[]) {

    String data = "This is the data in the output file\n";

    try {
      // Creates a FileWriter
      FileWriter output = new FileWriter("mylogfile.log", true);

      // Writes the string to the file
      output.write(data);

      // Closes the writer
      output.close();
    }

    catch (Exception e) {
      e.getStackTrace();
    }
  }
}
