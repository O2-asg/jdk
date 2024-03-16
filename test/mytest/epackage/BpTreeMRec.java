package epackage;

class BpTreeMRec {
	BpTreeMRecList tbl[];

	public static final int BUCKET_SIZE = 1021;

	private int hashToIndex(int hash)
	{
		return hash % BUCKET_SIZE;
	}

	BpTreeMRec(int hash)
	{
		int idx = hashToIndex(hash);

		this.tbl = new BpTreeMRecList[BUCKET_SIZE];
		this.tbl[idx] = new BpTreeMRecList(hash, true); // hash, is_leaf
	}

	void add_rec(BpTreeNode node, boolean is_leaf)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) {
			this.tbl[idx] = new BpTreeMRecList(hash, is_leaf);
		} else {
			this.tbl[idx].addNode(hash, is_leaf);
		}
	}

	BpTreeMRecNode remove_rec(BpTreeNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) { // error
			return null;
		}

		BpTreeMRecNode mrn = this.tbl[idx].delNode(hash);

		return mrn;
	}

	void update_children_rec(BpTreeNode node)
	{
		if (node == null) return;

		int hash = node.hashCode();
		int idx = hashToIndex(hash);
		BpTreeNode[] children = node.nodes;
		int size = node.size;

		if (this.tbl[idx] == null) { // error
			return;
		}

		this.tbl[idx].updateChildren(hash, children, size);
	}

	void update_parent_rec(BpTreeNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);
		BpTreeNode parent = node.parent;

		if (this.tbl[idx] == null) { // error
			return;
		}

		this.tbl[idx].updateParent(hash, parent);
	}

	BpTreeMRecNode get_rec(BpTreeNode node)
	{
		int hash = node.hashCode();
		int idx = hashToIndex(hash);

		if (this.tbl[idx] == null) {
			return null;
		}

		return this.tbl[idx].getRecord(hash);
	}
}
