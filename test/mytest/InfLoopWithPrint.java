class InfLoopWithPrint {
	public static void main(String[] args)
	{
		try
		{
			while (true) {
				Thread.sleep(2000);
				System.out.println("running...");
			}
		}
		catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}
}
