class NullpoException {
	public static void main(String[] args)
	{
		Object o = new Object();
		o = null;
		int h = o.hashCode();
		System.out.println("should not reach here");
	}
}
