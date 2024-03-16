class SleepingProcess {
	public static void main(String[] args)
	{
		try
		{
			while (true) {
				Thread.sleep(5000); // 5 second sleep
			}
		}
		catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}
}
