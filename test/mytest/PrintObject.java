public class PrintObject {
	public static void main(String[] args)
	{
		Object x = new Object();
		int h = x.hashCode();
		System.out.println("value " + x + " is in x");
		System.out.printf("hashcode is %x\n", h);
		System.gc();
	}
}
