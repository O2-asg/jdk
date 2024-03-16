package epackage;

class ListMRecNode {
	int hash;
//	Object o; // essential for recovering original node
	ListNode nextnode;
	ListMRecNode next;

	ListMRecNode(int hash, ListNode nextnode)
	{
		this.hash = hash;
		this.nextnode = nextnode;
		this.next = null;
	}
}
