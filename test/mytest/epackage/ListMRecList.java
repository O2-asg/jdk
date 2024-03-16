package epackage;

class ListMRecList {
	ListMRecNode head;

	ListMRecList(int hash, ListNode nextnode)
	{
		this.head = new ListMRecNode(hash, nextnode);
	}

	void addNode(int hash, ListNode nextnode)
	{
		ListMRecNode newnode = new ListMRecNode(hash, nextnode);

		newnode.next = this.head;
		this.head = newnode;
	}

	// deletes record of broken (or to be deleted) ListNode and
	// returns record of brokenNode (to restructure)
	// hash is brokenNode.hashCode()
	ListMRecNode delAndRetNode(int hash)
	{
		ListMRecNode ret = null;

		if (this.head.hash == hash) { // this.head holds the info
			ret = this.head;
			this.head = this.head.next;
			return ret;
		}

		ListMRecNode n = this.head;

		while (n.next != null) {
			if (n.next.hash == hash) { // find the info
				ret = n.next; // get next to repair data structure
				n.next = n.next.next; // delete record
				return ret;
			}
			n = n.next;
		}

		return ret; // null
	}

	// deletes record only
	// hash is deletenode.hashCode()
	void delNode(int hash)
	{
		if (this.head.hash == hash) { // this.head holds the record
			this.head = this.head.next;
			return;
		}

		ListMRecNode n = this.head;

		while (n.next != null) {
			if (n.next.hash == hash) { // found the record
				n.next = n.next.next; // delete record
				return;
			}
			n = n.next;
		}
	}


	// after deletion of ListNode, connection has changed
	// must update connection
	void updateNextNode(ListNode nextnode, int hash)
	{
		ListMRecNode n = this.head;

		while (n != null) {
			if (n.hash == hash) {
				n.nextnode = nextnode;
				return;
			}
			n = n.next;
		}
	}
}
