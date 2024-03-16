package epackage;

class BpTreeNodeInfo {
	BpTreeNode ownaddr;
	int size;
	BpTreeNode nodes[];
	BpTreeNode parent;
	boolean is_leaf;
	BpTreeNodeInfo next;

	public BpTreeNodeInfo(BpTreeNode node, int size, boolean is_leaf)
	{
		this.ownaddr = node;
		this.size = size;
		this.nodes = new BpTreeNode[BpTreeNode.degree+1];
		this.is_leaf = is_leaf;
		next = null;
	}
}
