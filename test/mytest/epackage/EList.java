package epackage;

// EMEs-aware list
public class EList {
	ListNode head;
	ListMRec mr;

	// EMEs can occur here
	public EList(int key, Object value)
	{
		this.head = new ListNode(key, value);
		this.mr = new ListMRec(this.head.hashCode());
	}

	private EList(EList original)
	{
		this.head = original.head;
		this.mr = original.mr;
	}

	public EList cloneInstance()
	{
		return new EList(this);
	}

	// check ListNode (broken or not)
	// (i.e., can read/write)
	boolean is_brokenNode(ListNode node)
	{
		if (node == null) return false;

		try
		{
			// read/write check
			node.key = node.key;
			node.obj = node.obj;
			node.next = node.next;

			return false; // not broken
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			return true; // broken
		}
	}

	// add new record to Recorder
	void add_rec(ListNode node, ListNode nextnode)
	{
		if (node == null) return; // error

		try
		{
			this.mr.add_rec(node, nextnode);
		}
		// ignore Recorder's error
		catch (ECCuncorrectableMemoryException eme)
		{
			System.out.println("add_rec failed: broken m-rec");
		}
	}

	// delete record of target
	void remove_rec(ListNode target)
	{
		if (target == null) return;

		try
		{
			this.mr.remove_rec(target);
		}
		// ignore Recorder's error
		catch (ECCuncorrectableMemoryException eme)
		{
			System.out.println("remove_rec failed: broken m-rec");
		}
	}

	void update_rec(ListNode node, ListNode nextnode)
	{
		try
		{
			this.mr.update_rec(node, nextnode);
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			System.out.println("update_rec failed: broken m-rec");
		}
	}

	// discard broken EList.head
	// since head is broken, we can't access this.head.next
	void discardHead()
	{
		ListMRecNode mrn = null;
		try
		{
			// remove and get record of head
			mrn = this.mr.remove_and_get_rec(this.head);
			this.head = mrn.nextnode;
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			System.out.println("unknown error");
		}
	}

	// internal deletion function
	// get node.next from m-rec
	void discardNode(ListNode target)
	{
		if (target == this.head) {
			discardHead();
			return;
		}

		ListNode n = this.head;
		ListMRecNode mrn = null;

		try
		{
			while (n != null) {
				if (n.next == target) {
					mrn = this.mr.remove_and_get_rec(target);
					update_rec(n, mrn.nextnode);
					n.next = mrn.nextnode;
					break;
				}
				n = n.next;
			}
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			if (is_brokenNode(n)) { // found broken node before target
				discardNode(n); // recursive call
				discardNode(target); // try again!
			} else {  } // ignore

			return;
		}
	}

	// add node to EMEs-aware list
	public void addNode(int key, Object value)
	{
		ListNode newnode = null, oldhead = this.head;

		try
		{
			// normal operation
			newnode = new ListNode(key, value);

			newnode.next = oldhead; // set next to old head
			this.head = newnode; // set head to newnode

			// record newly allocated ListNode
			add_rec(newnode, oldhead);
		}
		catch (ECCuncorrectableMemoryException eme)	// at newnode
		{
			// broken newnode
			if (is_brokenNode(newnode)) {
				addNode(key, value); // try again!
				return;
			}
		}
	}

	// delete node from EMEs-aware list
	public void delNode(int key)
	{
		ListNode n = this.head, prev = this.head;

		try
		{
			// normal operation
			if (this.head.key == key) {
				this.head = this.head.next;
				remove_rec(n);
				return;
			}

			n = n.next;
			while (n != null) {
				if (n.key == key) {
					break;
				}
				prev = n;
				n = n.next;
			}

			if (n != null) {
				prev.next = n.next;
				remove_rec(n);
				update_rec(prev, n.next);
			}
		}
		catch (ECCuncorrectableMemoryException eme) // at this.head or any other node
		{
			// error when R/W this.head.next
			if (is_brokenNode(this.head)) {
				discardHead();
				delNode(key); // try again!
			} else if (is_brokenNode(n)) { // error with node n
				discardNode(n);
				delNode(key); // try again!
			} else if (is_brokenNode(prev)) { // error when writing to prev.next
				discardNode(prev);
				delNode(key); // try again!
			} else { /* ignore other EMEs */ }

			return;
		}
	}

	// show EMEs-aware list
	public void showList()
	{
		ListNode n = this.head;
		try
		{
			// normal operation
			while (n != null) {
				System.out.printf("Object %s, key is %d\n", n.obj.toString(), n.key);
				n = n.next;
			}
		}
		catch (ECCuncorrectableMemoryException eme) // at this.head or any other node
		{
			if (is_brokenNode(this.head)) { // error when reading this.head.next
				discardHead();
				showList(); // try again!
			} else if (is_brokenNode(n)) {
				discardNode(n);
				System.out.println("showList() aborted: caught EME");
				System.out.println("sorry, some information has lost");
			} else { /* ignore other EME */ }

			return;
		}
	}

	// search and get object from given hashcode
	public Object getObject(int key)
	{
		ListNode n = this.head;

		try
		{
			// normal operation
			while (n != null) {
				if (n.key == key)
					return n.obj;
				n = n.next;
			}

			return null;
		}
		catch (ECCuncorrectableMemoryException eme)
		{
			if (is_brokenNode(this.head)) {
				discardHead();
				return getObject(key); // try again!
			} else if (is_brokenNode(n)) {
				discardNode(n);
				return null;
			} else { /* ignore other EME */ }

			return null;
		}

	}
}
