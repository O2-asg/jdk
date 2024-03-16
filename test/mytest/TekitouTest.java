class TekitouTest {
	public static void main(String[] args)
	{
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		System.out.printf("%x\n", o1.hashCode());
		System.out.printf("%x\n", o2.hashCode());
		System.out.printf("%x\n", o3.hashCode());
/*		try {
			while (true) {
				;
			}
		}
		catch (ECCuncorrectableMemoryException e) {
			System.out.println("hash is "+e.getBrokenObjectHash());
		}*/
	}
}
