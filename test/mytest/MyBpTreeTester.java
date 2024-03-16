import mypackage.MyBpTree;

class MyBpTreeTester {
	static int num = 500000;

	public static void main(String[] args)
	{
		int i;
		long start, end;
		double million = 1000000.0;
		int h[] = new int[num];
		Object o[] = new Object[num];

		for (i = 0; i < num; i++) {
			o[i] = new Object();
			h[i] = o[i].hashCode();
		}

		MyBpTree t = new MyBpTree(h[0], o[0]);

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
