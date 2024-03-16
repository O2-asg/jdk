package epackage;

class ListMRecRNode {
	int hash;
	ListNode nextnode;
	int key; // original node info
	Object o; // essential for recovering original node
	ListMRecRNode next;

	ListMRecRNode(int hash, ListNode nextnode, int key, Object obj)
	{
		this.hash = hash;
		this.nextnode = nextnode;
		this.o = obj;
		this.key = key;
		this.next = null;
	}
}
