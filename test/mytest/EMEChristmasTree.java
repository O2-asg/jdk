class EMEChristmasTree {
	public static void main(String[] args)
	{
		try
		{
			while (true)
				;
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			drawTree();
		}
	}

	static void drawTree()
	{
		int i, x, d;
		for (i = 0; i < 8; i++) {
			for (x = 0; x < 20; x++) {
				d = x-10;
				if (d <= 0) d *= -1;
				if (d <= i) System.out.print("*");
				else System.out.print(" ");
			}
			System.out.println("");
		}
		for (i = 0; i < 2; i++) {
			for (x = 0; x < 18; x++) {
				if ((x == 10) || (x == 11)) System.out.print("*");
				else System.out.print(" ");
			}
			System.out.println("");
		}
		System.out.println("Merry Christmas!");
	}
}
