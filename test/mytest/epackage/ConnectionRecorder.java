package epackage;

// used for list-node discard (skip)
// an entry holds a list-node's address
// and the next list-node address
class ConnectionRecorder {
	ListNode ndaddr;
	ListNode next_ndaddr;
	ConnectionRecorder next;

	// constructor for an entry
	ConnectionRecorder(ListNode n, ListNode n_next)
	{
		this.ndaddr = n;
		this.next_ndaddr = n_next;
		this.next = null;
	}
}
