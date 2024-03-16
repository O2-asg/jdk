class ClassinClass {
	public static void main(String[] args)
	{
		ParentClass p = new ParentClass();
		System.out.printf("parent hash is %x\n", p.hashCode());
		p.print_h();
	}
}

class ParentClass {
	ChildClass c;

	public ParentClass()
	{
		this.c = new ChildClass();
	}

	public void print_h()
	{
		System.out.printf("child hash is %x\n", c.hashCode());
	}
}

class ChildClass {
}
