import epackage.EBpTree_v2;

class EMEsBpTree_v2Tester {
	public static void main(String[] args)
	{
		int num = 100000;
		int i;
		int h[] = new int[num];
		long start, end;
		double million = 1000000.0;
		Object o[] = new Object[num];

		for (i = 0; i < num; i++) {
			o[i] = new Object();
			h[i] = o[i].hashCode();
		}

		EBpTree_v2 t = new EBpTree_v2(h[0], o[0]);

//		start = System.nanoTime();
		for (i = 1; i < num; i++) {
			t.insert(h[i], o[i]);
		}
//		end = System.nanoTime();
System.out.println("-----B+-tree GC-----");
		start = System.nanoTime();
		for (i = 0; i < num; i++) {
			t.delete(h[i]);
		}
		end = System.nanoTime();

		System.out.println((end-start)/million);
	}
}
