package epackage;

class BpTreeMRecNode {
	int ownhash;
	int size;
	BpTreeNode nodes[];
	BpTreeNode parent;
	boolean is_leaf;
	BpTreeMRecNode next;

	BpTreeMRecNode(int ownhash, boolean is_leaf)
	{
		this.ownhash = ownhash;
		this.size = 0;
		this.nodes = new BpTreeNode[BpTreeNode.degree+1];
		this.parent = null;
		this.is_leaf = is_leaf;
		this.next = null;
	}
}
