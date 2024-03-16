class InfLoop {
	public static void main(String[] args)
	{
		try
		{
			while (true)
				;
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			System.out.println("caught EME when infinite looping");
		}
	}
}
