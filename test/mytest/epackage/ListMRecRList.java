package epackage;

class ListMRecRList {
	ListMRecRNode head;

	ListMRecRList(int hash, ListNode nextnode, int key, Object obj)
	{
		this.head = new ListMRecRNode(hash, nextnode, key, obj);
	}

	void addNode(int hash, ListNode nextnode, int key, Object obj)
	{
		ListMRecRNode newnode = new ListMRecRNode(hash, nextnode, key, obj);

		newnode.next = this.head;
		this.head = newnode;
	}

	void delNode(int hash)
	{
		if (this.head.hash == hash) { // this.head holds the info
			this.head = this.head.next;
			return;
		}

		ListMRecRNode n = this.head;

		while (n.next != null) {
			if (n.next.hash == hash) { // find the info
				n.next = n.next.next; // delete record
				return;
			}
			n = n.next;
		}
	}

	// deletes record of broken (or to be deleted) ListNode and
	// returns brokenNode.next
	// hash is brokenNode.hashCode()
	ListMRecRNode delAndRetNode(int hash)
	{
		ListMRecRNode ret = null;

		if (this.head.hash == hash) { // this.head holds the info
			ret = this.head;
			this.head = this.head.next;
			return ret;
		}

		ListMRecRNode n = this.head;

		while (n.next != null) {
			if (n.next.hash == hash) { // find the info
				ret = n.next;
				n.next = n.next.next; // delete record
				return ret;
			}
			n = n.next;
		}

		/* should not reach here */
		return ret; // null
	}

	// after deletion of ListNode, connection has changed
	// must update connection
	void updateNextNode(ListNode nextnode, int hash)
	{
		ListMRecRNode n = this.head;

		while (n != null) {
			if (n.hash == hash) {
				n.nextnode = nextnode;
				return;
			}
			n = n.next;
		}
	}
}
