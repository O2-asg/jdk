public class VariousObject {
	public static void main(String[] args)
	{
		Object o = new Object();
		int[] numarr = new int[5];
//		String str = "hoge";
		String str = new String("hoge");
		int[][] twonumarr = new int[5][10];
		Myclass m = new Myclass(8);

		System.out.printf("Object hashCode is %x\n", o.hashCode());
		System.out.printf("int[5] hashCode is %x\n", numarr.hashCode());
		System.out.printf("String hashCode is %x\n", str.hashCode());
		System.out.printf("hoge hashCode is %x\n", "hoge".hashCode());
		System.out.printf("int[5][10] hashCode is %x\n", twonumarr.hashCode());
		System.out.printf("int[0] hashCode is %x\n", twonumarr[0].hashCode());
		System.out.printf("int[1] hashCode is %x\n", twonumarr[1].hashCode());
		System.out.printf("int[2] hashCode is %x\n", twonumarr[2].hashCode());
		System.out.printf("int[3] hashCode is %x\n", twonumarr[3].hashCode());
		System.out.printf("int[4] hashCode is %x\n", twonumarr[4].hashCode());
		System.out.printf("Myclass hashCode is %x\n", m.hashCode());
	}
}

class Myclass {
	private int x;

	Myclass(int x) {
		this.x = x;
	}
}

