package epackage;

// m-rec for linked list
// initialized with original EList
class ListMRecR {
	ListMRecRList tbl[];

	public static final int BUCKET_SIZE = 1021;

	private int hashToIndex(int hash)
	{
		return hash % BUCKET_SIZE;
	}

	// hash is EList.head.hashCode()
	ListMRecR(int hash, int key, Object obj)
	{
		int idx = hashToIndex(hash);

		this.tbl = new ListMRecRList[BUCKET_SIZE];
		this.tbl[idx] = new ListMRecRList(hash, null, key, obj);
	}

	void add_rec(ListNode node, ListNode nextnode, int key, Object obj)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) {
			this.tbl[idx] = new ListMRecRList(hash, nextnode, key, obj);
		} else {
			this.tbl[idx].addNode(hash, nextnode, key, obj);
		}
	}

	void remove_rec(ListNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) { // error
			return;
		}

		this.tbl[idx].delNode(hash); // delete m-rec
	}

	// removes record of broken (or deleted) ListNode and
	// returns next node of broken node
	// hash is brokenNode.hashCode()
	ListMRecRNode remove_and_get_rec(ListNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) { // error
			return null;
		}

		ListMRecRNode mrnr = this.tbl[idx].delAndRetNode(hash); // delete m-rec

		return mrnr;
	}

	// updates record of prev
	void update_rec(ListNode prev, ListNode nextnode)
	{
		int idx = hashToIndex(prev.hashCode());

		if (this.tbl[idx] == null) return;

		this.tbl[idx].updateNextNode(nextnode, prev.hashCode());
	}
}
