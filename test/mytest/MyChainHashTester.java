import mypackage.MyChainHash;

public class MyChainHashTester {
	static int NUM = 100000;

	public static void main(String[] args)
	{
		int i;
		int h[] = new int[NUM];

		MyChainHash ch = new MyChainHash();
		Object o[] = new Object[NUM];
		long start, end;
		double million = 1000000.0;

		for (i = 0; i < NUM; i++) {
			o[i] = new Object();
			h[i] = o[i].hashCode();
		}

		start = System.nanoTime();
		for (i = 0; i < NUM; i++) {
			ch.hash_store(h[i], o[i]);
		}
		end = System.nanoTime();
//System.out.println("-----ChainHash GC-----");
/*		start = System.nanoTime();
		for (i = 0; i < NUM; i++) {
			ch.hash_delete(h[i]);
		}
		end = System.nanoTime();*/

		System.out.println((end-start)/million); // ms
	}
}
