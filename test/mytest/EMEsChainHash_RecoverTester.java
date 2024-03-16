import epackage.EChainHash_Recover;

public class EMEsChainHash_RecoverTester {
	static int NUM = 100000;

	public static void main(String[] args)
	{
		int i;
		int h[] = new int[NUM];
		long start, end;
		double million = 1000000.0;

		EChainHash_Recover ch = new EChainHash_Recover();
		Object o[] = new Object[NUM];

		for (i = 0; i < NUM; i++) {
			o[i] = new Object();
			h[i] = o[i].hashCode();
		}

//		start = System.nanoTime();
		for (i = 0; i < NUM; i++) {
			ch.hash_store(h[i], o[i]);
		}
//		end = System.nanoTime();
System.out.println("-----ChainHash GC-----");
		start = System.nanoTime();
		for (i = 0; i < NUM; i++) {
			ch.hash_delete(h[i]);
		}
		end = System.nanoTime();

		System.out.println((end-start)/million); // ms
	}
}
