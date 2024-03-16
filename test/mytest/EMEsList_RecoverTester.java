import epackage.EList_Recover;

public class EMEsList_RecoverTester {
	static int num = 10;

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

		EList_Recover lst = new EList_Recover(keys[0], values[0]);

//		start = System.nanoTime();
		for (int i = 1; i < num; i++) {
			lst.addNode(keys[i], values[i]);
		}
		lst.showList();
//		end = System.nanoTime();
System.out.println("-----List GC-----");
//		start = System.nanoTime();
/*		for (int i = 1; i < num; i++) {
			lst.delNode(keys[i]);
		}*/
		lst.delNode(4);
		lst.showList();
//		end = System.nanoTime();

//		System.out.println((end-start)/million); // ms
	}
}
