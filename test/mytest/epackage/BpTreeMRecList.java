package epackage;

class BpTreeMRecList {
	BpTreeMRecNode head;

	BpTreeMRecList(int ownhash, boolean is_leaf)
	{
		this.head = new BpTreeMRecNode(ownhash, is_leaf);
	}

	void addNode(int ownhash, boolean is_leaf)
	{
		BpTreeMRecNode newnode;

		newnode = new BpTreeMRecNode(ownhash, is_leaf);

		newnode.next = this.head;
		this.head = newnode;
	}

	void updateChildren(int ownhash, BpTreeNode[] children, int newsize)
	{
		int i;
		BpTreeMRecNode mrn = this.head;

		while (mrn != null && mrn.ownhash != ownhash) {
			mrn = mrn.next;
		}

		if (mrn != null) {
			for (i = 0; i <= newsize; i++) {
				mrn.nodes[i] = children[i];
			}
			mrn.size = newsize;
		}
	}

	void updateParent(int ownhash, BpTreeNode newparent)
	{
		BpTreeMRecNode mrn = this.head;

		while (mrn != null && mrn.ownhash != ownhash) {
			mrn = mrn.next;
		}

		if (mrn != null) mrn.parent = newparent;
	}

	BpTreeMRecNode delNode(int ownhash)
	{
		BpTreeMRecNode mrn = this.head;
		BpTreeMRecNode ret = null;

		if (this.head.ownhash == ownhash) {
			ret = this.head;
			this.head = this.head.next;
			return ret;
		}

		while (mrn.next != null) {
			if (mrn.next.ownhash == ownhash) {
				ret = mrn.next;
				mrn.next = mrn.next.next;
				return ret;
			}
			mrn = mrn.next;
		}

		return ret; // null
	}

	BpTreeMRecNode getRecord(int hash)
	{
		BpTreeMRecNode mrn = this.head;

		while (mrn != null) {
			if (mrn.ownhash == hash) return mrn;
			mrn = mrn.next;
		}

		return null;
	}
}
