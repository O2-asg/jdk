import java.io.InputStream;
import java.io.IOException;
import java.util.Scanner;

public class ProcessBuilderTest {
	public static void main(String[] args) {
		System.out.print("enter pid > ");
		Scanner s = new Scanner(System.in);
		String pid = s.nextLine();
		s.close();

		try
		{
			ProcessBuilder builder = new ProcessBuilder("kill", "-USR1", pid);
			Process process = builder.start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.println("should not reach here");
	}
}
