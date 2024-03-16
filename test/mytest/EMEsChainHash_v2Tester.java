import epackage.EChainHash_v2;

public class EMEsChainHash_v2Tester {
	public static void main(String[] args)
	{
		int NUM = 200000;
		int i;
		int h[] = new int[NUM];
		long start, end;
		double million = 1000000.0;

		EChainHash_v2 ch = new EChainHash_v2();
		Object o[] = new Object[NUM];

		for (i = 0; i < NUM; i++) {
			o[i] = new Object();
			h[i] = o[i].hashCode();

		}
System.out.println("-----ChainHash GC-----");
		start = System.nanoTime();
		for (i = 0; i < NUM; i++) {
			ch.hash_store(o[i], h[i]);
		}
		end = System.nanoTime();

/*		start = System.nanoTime();
		for (i = 0; i < NUM; i++) {
			ch.hash_delete(h[i]);
		}
		end = System.nanoTime();*/

		System.out.println((end-start)/million); // ms
	}
}
