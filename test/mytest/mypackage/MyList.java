package mypackage;

public class MyList {
	ListNode head;

	public MyList()
	{
		this.head = new ListNode();
	}

	public void addNode(int key, Object obj)
	{
		ListNode newnode = new ListNode(key, obj);

		newnode.next = this.head.next;
		this.head.next = newnode;
	}

	public void delNode(int key)
	{
		ListNode n = this.head;
		ListNode target = null;

		while (n.next != null) {
			if (n.next.key == key) {
				target = n.next;
				break;
			}
			n = n.next;
		}

		if (target != null) {
			n.next = target.next;
		}
	}

	public void showList()
	{
		ListNode n = this.head;

		while (n != null) {
			if (n == this.head) {
				n = n.next;
				continue;
			}
			System.out.printf("Object %s, key is %d\n", n.obj.toString(), n.key);
			n = n.next;
		}
	}

	public Object getObject(int key)
	{
		ListNode n = this.head;

		n = n.next;

		while (n != null) {
			if (n.key == key)
				return n.obj;
			n = n.next;
		}

		return null;
	}
}
