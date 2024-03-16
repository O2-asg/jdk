package epackage;

// EMEs-aware list node
// obj and hash can be any data
class ListNode {
	int key;
	Object obj;
	ListNode next;

	// constructor
	ListNode(int key, Object value)
	{
		this.key = key;
		this.obj = value;
		next = null;
	}

	// constructor for head of list
	ListNode()
	{
		this.key = 0;
		this.obj = null;
		next = null;
	}
}
