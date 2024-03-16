import epackage.EList;

public class EMEsListTester {
	static int num = 100000;

	public static void main(String[] args)
	{
		int keys[] = new int[num];
		Object values[] = new Object[num];
		double million = 1000000;
		long start, end;

		for (int i = 0; i < num; i++) {
			values[i] = new Object();
			keys[i] = i;
		}

		EList lst = new EList(keys[0], values[0]);

//		start = System.nanoTime();
		for (int i = 1; i < num; i++) {
			lst.addNode(keys[i], values[i]);
		}
//		end = System.nanoTime();
System.out.println("-----List GC-----");
//		start = System.nanoTime();
/*		for (int i = 1; i < num; i++) {
			lst.delNode(keys[i]);
		}*/
//		end = System.nanoTime();

//		System.out.println((end-start)/million); // ms
	}
}
