class TryCatchExample {
	public static void main(String[] args)
	{
		Object o = new Object();
		o = null;
		try
		{
			int h = o.hashCode();
		}
		catch(NullPointerException e)
		{
			System.out.printf("ぬるぽ %x\n", e.hashCode());
			System.out.println("ガッ");
		}
	}
}
