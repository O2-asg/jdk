class InfLoops {
	public static void main(String[] args)
	{
		ChildInfLoop child = new ChildInfLoop();
		child.start();

		while (true)
			;
	}
}

class ChildInfLoop extends Thread {
	public void run()
	{
		while (true)
			;
	}
}
