import epackage.EList_v2_recover;

public class EMEsList_v2_recoverTester {
	public static void main(String[] args)
	{
		int num = 10000;
		Object o[] = new Object[num];
		double million = 1000000;
		long start, end;
		int h[] = new int[num];

		for (int i = 0; i < num; i++) {
			o[i] = new Object();
			h[i] = o[i].hashCode();
		}

		EList_v2_recover lst = new EList_v2_recover(h[0], o[0]);

		for (int i = 1; i < num; i++) {
			lst.addNode(h[i], o[i]);
		}

		start = System.nanoTime();
		for (int i = 1; i < num; i++) {
			lst.delNode(h[i]);
		}
		end = System.nanoTime();

		System.out.println((end-start)/million); // ms
	}
}
