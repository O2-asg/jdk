package epackage;

// EMEs-aware list
public class EList {
	ListNode head;
	ConnectionRecorder cr;

	// EMEs can occur here
	public EList()
	{
		this.head = new ListNode();
		this.cr = new ConnectionRecorder(this.head, null);
	}

	private EList(EList original)
	{
		this.head = original.head;
		this.cr = original.cr;
	}

	public EList cloneInstance()
	{
		return new EList(this);
	}

	// check if list is broken
	// (i.e., can read/write head addr)
	boolean is_brokenList(EList lst)
	{
		try
		{
			lst.head = lst.head;
			return false;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			return true;
		}
	}

	// check if node is broken
	// (i.e., can read/write)
	boolean is_brokenNode(ListNode node)
	{
		if (node == null) return false;

		try
		{
			// read/write check
			node.obj = node.obj;
			node.hash = node.hash;
			node.next = node.next;

			return false; // not broken node
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			return true; // broken node
		}
	}

	// add new entry to Recorder
	void addEntry(ListNode n, ListNode n_next)
	{
		try
		{
			ConnectionRecorder newentry = new ConnectionRecorder(n, n_next);
			newentry.next = this.cr.next;
			this.cr.next = newentry;
			this.cr.next_ndaddr = n; // always add node to head.next
		}
		// ignore Recorder's error
		catch (ECCuncorrectableMemoryError eme)
		{
			System.out.println("addEntry failed");
		}
	}

	// delete Recorder of target
	// also rewrites Recorder of previous node of target
	void delEntry(ListNode target)
	{
		if (target == this.head) return;

		try
		{
			ConnectionRecorder m = this.cr;

			// targetCR is ConnectionRecorder of target. -> to be skipped
			// prevCR is ConnectionRecordee of previous node of target. ->
			// prevCR.next_ndaddr will be overwritten to targetCR.next_ndaddr.
			ConnectionRecorder targetCR = null, prevCR = null;

			while (m.next != null) {
				if (m.next.ndaddr == target) { // m.next is ConnectionRecorder of target
					targetCR = m.next;
					m.next = targetCR.next; // skip
					continue;
				}
				if (m.next.next_ndaddr == target) // m.next is CR of previous node of target
					prevCR = m.next;
				if (targetCR != null && prevCR != null) {
					// overwrite prevCR's next_ndaddr
					prevCR.next_ndaddr = targetCR.next_ndaddr;
					break;
				}
				m = m.next;
			}
		}
		// ignore Recorder's error
		catch (ECCuncorrectableMemoryError eme)
		{
			System.out.println("delEntry failed");
		}
	}

	// get previous node from Recorder
	// only used for node discard (skip)
	ListNode getPrevNode(ListNode n) /* #UNUSED */
	{
		try
		{
			ConnectionRecorder m = this.cr;

			if (n == this.head) return null; // previous node of head node doesn't exist

			while (m != null) {
				if (m.next_ndaddr == n) // found
					return m.ndaddr;
				m = m.next;
			}

			return null; // not found

		}
		catch (ECCuncorrectableMemoryError eme)
		{
			System.out.println("getPrevNode failed");
			return null;
		}
	}

	// get next node from Recorder
	// only used for node discard (skip)
	ListNode getNextNode(ListNode n)
	{
		try
		{
			ConnectionRecorder m = this.cr;

			while (m != null) {
				if (m.ndaddr == n) // found
					return m.next_ndaddr;
				m = m.next;
			}

			return null; // not found
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			System.out.println("getNextNode failed");
			return null;
		}
	}

	// replace broken head node of list
	// since head is broken, accessing head.next is dangerous
	void replaceHead()
	{
		ListNode newhead = null, next = null;
		try
		{
			newhead = new ListNode();
			next = this.cr.next_ndaddr; // avoid accessing head.next

			if (next != null) {
				newhead.next = next;
				this.head = newhead;
				this.cr.ndaddr = this.head; // update entry
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(newhead))
				replaceHead(); // try again!
		}
	}

	// skip broken node and
	// delete entry of broken node
	void skip_brokenNode(ListNode broken_node) /* #UNUSED */
	{
		ListNode prevnode = getPrevNode(broken_node);
		ListNode nextnode = getNextNode(broken_node);
		if (prevnode != null && nextnode != null) {
			prevnode.next = nextnode; // skip
			delEntry(broken_node); // delete entry
		}
	}

	// internal deletion function
	// get node.next from ConnectionRecorder
	void delete_node_from_list(ListNode target)
	{
		if (target == this.head) {
			replaceHead();
			return;
		}

		ListNode n = this.head;
		ListNode tnext = null;

		try
		{
			while (n != null) {
				if (n.next == target) {
					tnext = getNextNode(target);
					if (tnext != null)
						n.next = tnext;
					break;
				}
				n = n.next;
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(n)) { // found broken node before target
				delete_node_from_list(n); // recursive call
				delete_node_from_list(target); // try again!
			} else {  } // ignore

			return;
		}

		// m-list update
		// callee handles EME
		delEntry(target);
	}

	// add node to EMEs-aware list
	public void addNode(Object obj, int hash)
	{
		ListNode newnode = null, head_next = null;

		try
		{
			// normal operation
			newnode = new ListNode(obj, hash);
			head_next = this.head.next;

			newnode.next = head_next; // set next
			this.head.next = newnode; // insert
		}
		catch (ECCuncorrectableMemoryError eme)	// at newnode or this.head
		{
			// broken head
			if (is_brokenNode(this.head)) {
				replaceHead();
				addNode(obj, hash); // try again!
				return;
			}
			// broken newnode
			if (is_brokenNode(newnode)) {
				addNode(obj, hash); // try again!
				return;
			}
		}

		// update m-list
		// callee handles EME
		addEntry(newnode, head_next);
	}

	// delete node from EMEs-aware list
	// key is hashCode
	public void delNode(int hash)
	{
		ListNode n = this.head, prev = this.head;

		try
		{
			// normal operation
			n = n.next;
			while (n != null && n.hash != hash) {
				prev = n;
				n = n.next;
			}

			if (n != null) {
				prev.next = n.next;
			}
		}
		catch (ECCuncorrectableMemoryError eme) // at this.head or any other node
		{
			// error when R/W this.head.next
			if (is_brokenNode(this.head)) {
				replaceHead();
				delNode(hash); // try again!
			} else if (is_brokenNode(n)) { // error with node n
				delete_node_from_list(n);
				delNode(hash); // try again!
			} else if (is_brokenNode(prev)) { // error when writing to prev.next
				delNode(hash); // try again! this time EME is at node n.
			} else { /* ignore other EME */ }

			return;
		}

		// m-list update
		// callee handles EME
		delEntry(n);
	}

	// show EMEs-aware list
	public void showList()
	{
		ListNode n = this.head;
		try
		{
			// normal operation
			n = n.next; // skip head node
			while (n != null) {
				System.out.printf("Object %s, hashCode is %x\n", n.obj.toString(), n.hash);
				n = n.next;
			}
		}
		catch (ECCuncorrectableMemoryError eme) // at this.head or any other node
		{
			if (is_brokenNode(this.head)) { // error when reading this.head.next
				replaceHead();
				showList(); // try again!
			} else if (is_brokenNode(n)) {
				delete_node_from_list(n);
				System.out.println("showList() aborted: caught EME");
				System.out.println("sorry, some information has lost");
			} else { /* ignore other EME */ }

			return;
		}
	}

	// search and get object from given hashcode
	public Object getObject(int hash)
	{
		ListNode n = this.head;

		try
		{
			// normal operation
			n = n.next;
			while (n != null) {
				if (n.hash == hash)
					return n.obj;
				n = n.next;
			}

			return null;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(this.head)) {
				replaceHead();
				return getObject(hash); // try again!
			} else if (is_brokenNode(n)) {
				delete_node_from_list(n);
				return null;
			} else { /* ignore other EME */ }

			return null;
		}

	}
}
