package epackage;

// m-rec for linked list
// initialized with original EList
class ListMRec {
	ListMRecList tbl[];

	public static final int BUCKET_SIZE = 1021;

	private int hashToIndex(int hash)
	{
		return hash % BUCKET_SIZE;
	}

	// hash is EList.head.hashCode()
	ListMRec(int hash)
	{
		int idx = hashToIndex(hash);

		this.tbl = new ListMRecList[BUCKET_SIZE];
		this.tbl[idx] = new ListMRecList(hash, null);
	}

	void add_rec(ListNode node, ListNode nextnode)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) {
			this.tbl[idx] = new ListMRecList(hash, nextnode);
		} else {
			this.tbl[idx].addNode(hash, nextnode);
		}
	}

	void remove_rec(ListNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) { // error
			return;
		}

		this.tbl[idx].delNode(hash);
	}

	// removes record of broken (or deleted) ListNode and
	// returns record of broken node
	// hash is brokenNode.hashCode()
	ListMRecNode remove_and_get_rec(ListNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) { // error
			return null;
		}

		ListMRecNode nextnode = this.tbl[idx].delAndRetNode(hash); // delete m-rec

		return nextnode;
	}

	// updates record of prev
	void update_rec(ListNode prev, ListNode nextnode)
	{
		int idx = hashToIndex(prev.hashCode());

		if (this.tbl[idx] == null) return;

		this.tbl[idx].updateNextNode(nextnode, prev.hashCode());
	}
}
