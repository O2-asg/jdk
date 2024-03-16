package mypackage;

// list node
// obj and hash can be any data
class ListNode {
	int key;
	Object obj;
	ListNode next;

	// constructor
	ListNode(int key, Object obj)
	{
		this.key = key;
		this.obj = obj;
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
