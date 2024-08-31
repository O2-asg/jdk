import epackage.EList;

class TekitouTest {
	public static void main(String[] args)
	{
		EList lst = new EList(1, null);
		Object objs[];

		lst.addNode(2, null);
		lst.addNode(3, null);

		try {
			System.gc();
		} catch (ECCuncorrectableMemoryException e) {
			int hash = e.getBrokenObjectHash();
			System.out.printf("hash is %x\n", hash);
			lst.delNode(2);
			System.gc();
			System.out.println("after reconstruction & another GC");
		}
	}
}

