package epackage;

// EMEs-aware B+-tree
// btni holds a node's addr, child(ren) addr, and leaf or not

// when reading root (e.g., node = this.root), EMEs can occur
// this causes fail stop because instance is broken
// (memory addr that stores root reference is broken)
public class EBpTree_old {
	BpTreeNode root;
	BpTreeNodeInfo btni;

	// EMEs can be occur here
	public EBpTree_old(int key, Object obj)
	{
		this.root = new BpTreeNode(key, obj);
		this.btni = new BpTreeNodeInfo(this.root, 1, true); // (node, size, is_leaf)
		this.btni.nodes[0] = this.root.nodes[0];
	}

	// check node (broken or not)
	boolean is_brokenNode(BpTreeNode node)
	{
		if (node == null) return false;
		// read/write check
		try
		{
			node.keys = node.keys;
			node.obj = node.obj;
			node.size = node.size;
			node.nodes = node.nodes;
			node.parent = node.parent;
			node.is_leaf = node.is_leaf;
			return false;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			return true;
		}
	}

	// fetch and return nodeinfo(M-list's node)
	// nodeinfo(M-list) is LRU structure
	BpTreeNodeInfo search_NodeInfo(BpTreeNode node)
	{
		BpTreeNodeInfo ni = this.btni;

		try
		{
			if (ni.ownaddr == node) return ni;

			while (ni.next != null) { // LRU
				if (ni.next.ownaddr == node) { // found
					BpTreeNodeInfo target = ni.next;
					ni.next = target.next;
					target.next = this.btni;
					this.btni = target;
					return target;
				}
				ni = ni.next;
			}
			return null; // not found
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// ignore btni's error
			return null;
		}
	}

	// update parent information only
	void update_parentInfo(BpTreeNode node)
	{
		BpTreeNodeInfo ni = search_NodeInfo(node);
		try
		{
			if (ni != null)
				ni.parent = node.parent;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			System.out.println("fatal error: EMEs");
		}
	}

	// replace a node using nodeinfo
	BpTreeNode replaceNode(BpTreeNode node)
	{
		int i;
		BpTreeNodeInfo ni = search_NodeInfo(node);
		BpTreeNode newnode = null;

		try
		{
			if (ni != null) {
				// common operation (leaf or mid-node or root)
				newnode = new BpTreeNode();
				BpTreeNodeInfo parent_ni = search_NodeInfo(ni.parent);
				newnode.size = ni.size;
				newnode.parent = ni.parent;
				newnode.is_leaf = ni.is_leaf;

				if (ni.parent != null) { // non root
					for (i = 0; i <= parent_ni.size; i++) {
						if (parent_ni.nodes[i] == node) {
							ni.parent.nodes[i] = newnode;
							if (parent_ni != null) parent_ni.nodes[i] = newnode;
							break;
						}
					}
				}

				if (ni.is_leaf) { // leaf
					for (i = 0; i < ni.size; i++) {
						newnode.nodes[i] = ni.nodes[i];
						newnode.keys[i] = ni.nodes[i].size; // key is stored in this entry
					}
					newnode.nodes[BpTreeNode.degree] = ni.nodes[BpTreeNode.degree];
				}
				else { // non leaf
					for (i = 0; i < ni.size; i++) {
						newnode.nodes[i] = ni.nodes[i];
						ni.nodes[i].parent = newnode;
						update_parentInfo(ni.nodes[i]); // parent changed
						newnode.keys[i] = minimum_key(ni.nodes[i+1]);
					}
					newnode.nodes[i] = ni.nodes[i];
					ni.nodes[i].parent = newnode;
					update_parentInfo(ni.nodes[i]); // parent changed
				}
				ni.ownaddr = newnode;
				if (node == this.root) this.root = newnode;
				return newnode;
			}
			return null;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			System.out.println("fatal error: EMEs");
			return null; // should we try again?
		}
	}

	void addInfo(BpTreeNode node)
	{
		BpTreeNodeInfo newinfo = new BpTreeNodeInfo(node, node.size, node.is_leaf);

		int i;

		try
		{
			for (i = 0; i < node.size; i++) { // deep copy
				newinfo.nodes[i] = node.nodes[i];
			}

			if (node.is_leaf) newinfo.nodes[BpTreeNode.degree] = node.nodes[BpTreeNode.degree];
			else newinfo.nodes[i] = node.nodes[i]; // non leaf
			newinfo.parent = node.parent;

			newinfo.next = this.btni.next;
			this.btni.next = newinfo;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// fatal: node information has not made yet
			System.out.println("fatal error: EMEs");
		}
	}

	// updates nodeinfo
	void update_NodeInfo(BpTreeNode node, boolean is_add)
	{
		BpTreeNodeInfo target_ni = search_NodeInfo(node);
		int i;
		int end;

		try
		{
			if (target_ni != null) {
				if (is_add) end = node.size;
				else end = target_ni.size;
				for (i = 0; i < end; i++) {
					target_ni.nodes[i] = node.nodes[i];
				}
				if (node.is_leaf) target_ni.nodes[BpTreeNode.degree] = node.nodes[BpTreeNode.degree];
				else target_ni.nodes[i] = node.nodes[i]; // non leaf
				target_ni.size = node.size;
				target_ni.parent = node.parent;
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// fatal: tree can be inconsistent state
			// e.g., the same entries can be at two nodes
			System.out.printf("fatal error: EMEs");
		}
	}

	// delete nodeinfo
	void delInfo(BpTreeNode node)
	{
		BpTreeNodeInfo ni;

		try
		{
			ni = this.btni;
			if (ni.ownaddr == node) {
				this.btni = ni.next;
				return;
			}

			while (ni.next != null) {
				if (ni.next.ownaddr == node) {
					ni.next = ni.next.next;
					break;
				}
				ni = ni.next;
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// ignore btni's error
		}
	}

	// returns minimum key of the node
	int minimum_key(BpTreeNode node)
	{
		try
		{
			if (node.is_leaf)
				return node.keys[0];
			else
				return minimum_key(node.nodes[0]); // recursive call
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// EMEs can occur at
			//   - reading node.is_leaf
			//   - reading array head addr (keys[0])
			//   - reading array value (keys[i]) (->iaload)
			//   - reading array head addr (nodes[0])
			if (is_brokenNode(node)) {
				BpTreeNodeInfo ni = search_NodeInfo(node);
				if (ni != null) {
					if (ni.is_leaf) return ni.nodes[0].size; // key is here
					else return minimum_key(ni.nodes[0]);
				}
			}
			// should not reach here
			System.out.println("fatal error: EMEs");
			System.exit(1);
			return -1;
		}
	}

	// search and return node that contains key
	BpTreeNode search_node(int key)
	{
		int i;
		BpTreeNode node = this.root;

		try
		{
			while (!node.is_leaf) {
				for (i = 0; i < node.size; i++) {
					if (node.keys[i] > key) break;
				}

				node = node.nodes[i];
			}

			return node;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// EMEs can occur at
			//   - reading addr of root
			//   - reading node.entries
			//   - reading array value (keys[i])
			//   - reading array value (nodes[i])
			if (is_brokenNode(node)) {
				replaceNode(node);
				return search_node(key); // try again!
			}
			return null;
		}
	}

	// can't use for leaf insertion
	// let EMEs occur and caller has to handle it
	void insert_node_to_node(BpTreeNode addnode, BpTreeNode node)
	{
		int i, key, tmpkey;

		if (node.size == BpTreeNode.degree || node.is_leaf) {
			addnode.parent = node;
			update_parentInfo(addnode); // parent changed
			return;
		}

		key = minimum_key(addnode);

		if (node.size == 0) {
			if (node.nodes[0] == null) {
				node.nodes[0] = addnode;
				addnode.parent = node;
				update_parentInfo(addnode);
				return;
			}

			if (addnode == node.nodes[0]) return; // addnode already exists
			tmpkey = minimum_key(node.nodes[0]);
			if (key < tmpkey) {
				node.nodes[1] = node.nodes[0];
				node.keys[0] = tmpkey;
				node.nodes[0] = addnode;
			} else {
				node.keys[0] = key;
				node.nodes[1] = addnode;
			}
			node.size++;
			addnode.parent = node;
			update_parentInfo(addnode);
			return;
		}

		for (i = 0; i <= node.size; i++)
			if (addnode == node.nodes[i]) return; // addnode already exists

		for (i = node.size; i > 0 && key < node.keys[i-1]; i--) {
			node.keys[i] = node.keys[i-1];
			node.nodes[i+1] = node.nodes[i];
		}

		node.keys[i] = key;
		node.nodes[i+1] = addnode;
		node.size++;

		addnode.parent = node;
		update_parentInfo(addnode);

		// EMEs can occur at
		//   - reading node.entries
		//   - writing addnode.parent
		//   - reading/writing node.nodes[i] (value)
		//   - writing/reading array value (keys[i])
	}

	// used for mid node (non leaf)
	// let EMEs occur and caller has to handle it
	void delete_node_from_node(BpTreeNode delnode, BpTreeNode node)
	{
		if (node.size == 0) return;
		if (node.nodes[0] == delnode) return;

		int i, key;

		key = minimum_key(delnode);
		for (i = 0; i < node.size-1; i++) {
			if (key <= node.keys[i]) {
				node.keys[i] = node.keys[i+1];
				node.nodes[i+1] = node.nodes[i+2];
			}
		}

		node.keys[i] = -1;
		node.nodes[i+1] = null;
		node.size--;

		// EMEs can occur at
		//   - reading/writing node.entries
		//   - reading/writeing array value (keys[i] or nodes[i])
	}

	// used for leaf node
	// let EMEs occur and caller has to handle it
	void delete_key_from_leaf(int key, BpTreeNode node)
	{
		if (node.size == 0 || !node.is_leaf) return;

		int i;
		for (i = 0; i < node.size-1; i++) {
			if (key <= node.keys[i]) {
				node.keys[i] = node.keys[i+1];
				node.nodes[i] = node.nodes[i+1];
			}
		}

		node.keys[i] = -1;
		node.nodes[i] = null;
		node.size--;

		// EMEs can occur at
		//   - reading/writing node.entries
		//   - reading/writing array value (keys[i] or nodes[i])
	}

	// objnode: node that holds reference to object (and key)
	// let EMEs occur and caller has to handle it
	void insert_key_to_leaf(int key, BpTreeNode objnode, BpTreeNode node)
	{

		if (node.size == BpTreeNode.degree && !node.is_leaf) return;

		int i;
		for (i = node.size; i > 0 && key < node.keys[i-1]; i--) {
			node.keys[i] = node.keys[i-1];
			node.nodes[i] = node.nodes[i-1];
		}

		node.keys[i] = key;
		node.nodes[i] = objnode;
		node.size++;

		// EMEs can occur at
		// the same place to delete_key_from_leaf
	}

	// split node (right is node's new child)
	void insert_node_split(BpTreeNode right, BpTreeNode node)
	{
		BpTreeNode left_node = node, right_node = null;
		BpTreeNode p = left_node.parent;
		int i;
		int right_minkey = minimum_key(right);
		int d = BpTreeNode.degree;

		try
		{
			right_node = new BpTreeNode();
			right_node.parent = p;

			for (i = left_node.size-1; right_node.size < d/2; i--) {
				if ((left_node.keys[i] < right_minkey) && !right_node.is_node_included(right)) {
					insert_node_to_node(right, right_node);
					i++;
					continue;
				}

				insert_node_to_node(left_node.nodes[i+1], right_node);
				delete_node_from_node(left_node.nodes[i+1], left_node);
			}

			if (left_node.size == ((d+1)/2 -1))
				insert_node_to_node(right, left_node);

			update_NodeInfo(left_node, false); // entry decreases
			addInfo(right_node);
			insert_nodes_to_node(left_node, right_node, p);
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			// EMEs can occur at
			//   - allocating new BpTreeNode (write access)
			//   - writing new node's parent
			//   - reading node.entries
			if (is_brokenNode(left_node)) {
				BpTreeNode new_left = replaceNode(left_node);
				insert_node_split(right, new_left);
			}
			else if (is_brokenNode(right)) { // writing right.parent
				// right info has to be registered before
				BpTreeNode new_right = replaceNode(right);
				BpTreeNode new_left = replaceNode(left_node); // reset
				insert_node_split(new_right, new_left);
			} else { // should not reach here
				System.out.println("fatal error: EMEs");
				System.exit(1);
			}
		}
	}

	void insert_nodes_to_node(BpTreeNode left, BpTreeNode right, BpTreeNode node)
	{
		try
		{
			int right_minkey = minimum_key(right);

			if (node == null) { // no parent node
				this.root = new BpTreeNode();
				this.root.keys[0] = right_minkey;
				this.root.size = 1;
				this.root.nodes[0] = left;
				this.root.nodes[1] = right;
				left.parent = this.root;
				right.parent = this.root;
				addInfo(this.root);
				update_parentInfo(left);
				update_parentInfo(right);
				return;
			}

			if (node.size < BpTreeNode.degree) {
				insert_node_to_node(right, node);
				update_NodeInfo(node, true); // parent entry increase
				return;
			}

			if (node.size == BpTreeNode.degree) {
				insert_node_split(right, node);
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(right)) {
				BpTreeNode new_right = replaceNode(right);
				insert_nodes_to_node(left, new_right, node); // try again!
			} else if (is_brokenNode(left)) {
				BpTreeNode new_left = replaceNode(left);
				insert_nodes_to_node(new_left, right, node); // try again!
			} else if (is_brokenNode(this.root)) {
				// is this correct operation?
				insert_nodes_to_node(left, right, null); // try again!
			} else { // should not reach here
				System.out.println("fatal error: EMEs");
				System.exit(1);
			}
		}
	}

	void insert_leaf_split(int key, BpTreeNode objnode, BpTreeNode node)
	{
		BpTreeNode left = node, right = null;
		BpTreeNode p = left.parent;
		int i;
		int d = BpTreeNode.degree;

		try
		{
			right = new BpTreeNode();
			right.parent = p;
			right.is_leaf = true;

			for (i = left.size-1; right.size < (d+1)/2; i--) {
				if ((left.keys[i] < key) && !right.is_key_included(key)) {
					insert_key_to_leaf(key, objnode, right);
					i++;
					continue;
				}

				insert_key_to_leaf(left.keys[i], left.nodes[i], right);
				delete_key_from_leaf(left.keys[i], left);
			}

			if (left.size == d/2)
				insert_key_to_leaf(key, objnode, left);

			right.nodes[d] = left.nodes[d];
			left.nodes[d] = right;

			update_NodeInfo(left, false); // entry decreases
			addInfo(right);
			insert_nodes_to_node(left, right, p);
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(left) || is_brokenNode(right)) {
				BpTreeNode new_left = replaceNode(left);
				insert_leaf_split(key, objnode, new_left); // try again!
			} else { // should not reach here
				System.out.println("fatal error: EMEs");
				System.exit(1);
			}
		}
	}

	// rewrite node's key (from before to after)
	void rewrite_key(int before, int after, BpTreeNode node)
	{
		if (node == null) return;

		try
		{
			for (int i = 0; i < node.size; i++) {
				if (node.keys[i] == before) {
					node.keys[i] = after;
					break;
				}
			}

			rewrite_key(before, after, node.parent); // recursive call
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node)) {
				BpTreeNode newnode = replaceNode(node);
				rewrite_key(before, after, newnode); // try again!
			}
		}
	}

	// merge node_a and node_b
	// node_b will be removed
	// let EMEs occur and caller has to handle it
	void merge_nodes(BpTreeNode node_a, BpTreeNode node_b)
	{
		if (node_a == null) return;
		if (node_a.nodes[(BpTreeNode.degree+1)/2] != null) return;

		int i = 0;

		while (node_b.nodes[i] != null) {
			insert_node_to_node(node_b.nodes[i], node_a);
			i++;
		}
	}

	void balance_tree(BpTreeNode node)
	{
		int i;
		BpTreeNode p = null, left = null, right = null;

		try
		{
			p = node.parent;
			if (p == null) { // root node
				if (node.nodes[1] != null) { // two or more sub-trees
					update_NodeInfo(node, false); // entry decreases
				} else { // only one sub-tree
					this.root = node.nodes[0];
					this.root.parent = null;
					update_parentInfo(this.root);
					delInfo(node);
				}
				return; // finish balancing
			}

			if (node.nodes[(BpTreeNode.degree)/2] != null) { // entries >= ROUNDUP((degree+1)/2)
				// node has enough entries
				update_NodeInfo(node, false); // entry decreases
				return;
			} else {
				update_NodeInfo(node, false); // entry decreases
				// fetch two siblings (left and right)
				for (i = 0; i <= p.size; i++) {
					if (p.nodes[i] == node) {
						if (i != 0) left = p.nodes[i-1];
						if (i != p.size) right = p.nodes[i+1];
						break;
					}
				}

				// get child from sibling
				if (left != null) {
					if (left.nodes[(BpTreeNode.degree+1)/2] != null) { // has enough entries
						insert_node_to_node(left.nodes[left.size], node);
						delete_node_from_node(left.nodes[left.size], left);
						rewrite_key(minimum_key(node.nodes[1]), minimum_key(node.nodes[0]), p);
						update_NodeInfo(left, false); // entry decreases
						update_NodeInfo(node, true); // entry increases
						return;
					}
				}
				if (right != null) {
					if (right.nodes[(BpTreeNode.degree+1)/2] != null) { // has enough entries
						insert_node_to_node(right.nodes[0], node);
						for (i = 0; i < right.size; i++) {
							if (i != (right.size-1)) right.keys[i] = right.keys[i+1];
							right.nodes[i] = right.nodes[i+1];
						}
						right.nodes[i] = null;
						right.size--;
						rewrite_key(minimum_key(node.nodes[node.size]), minimum_key(right.nodes[0]), p);
						update_NodeInfo(right, false); // entry decreases
						update_NodeInfo(node, true); // entry increases
						return;
					}
				}

				// merge
				if (left != null) {
					merge_nodes(left, node);
					delete_node_from_node(node, p);
					update_NodeInfo(left, true); // entry increase
					delInfo(node);
					balance_tree(p);
				} else if (right != null) {
					// implies node is leftmost child of node.parent
					merge_nodes(node, right);
					delete_node_from_node(right, p);
					update_NodeInfo(node, true); // entry increases
					delInfo(right);
					balance_tree(p);
				}
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node) || is_brokenNode(left) || is_brokenNode(right) || is_brokenNode(p)) {
				BpTreeNode newnode = replaceNode(node);
				replaceNode(left); // reset
				replaceNode(right); // reset
				replaceNode(p);
				balance_tree(newnode); // try again!
			} else if (is_brokenNode(this.root)) { // from this.root.parent = null;
				replaceNode(this.root);
				this.root.parent = null;
				update_parentInfo(this.root);
				delInfo(node);
			} else { // should not reach here
				System.out.println("fatal error: EMEs");
				System.exit(1);
			}
		}
	}

	// merge leaf_b entries into leaf_a
	// leaf_b will be removed
	// let EMEs occur and caller has to handle it
	void merge_leaves(BpTreeNode leaf_a, BpTreeNode leaf_b)
	{
		if (leaf_a == null || leaf_b == null) return;

		if (leaf_a.parent != leaf_b.parent) return;
		if (leaf_a.size + leaf_b.size > BpTreeNode.degree) return;

		int i;
		for (i = 0; i < leaf_b.size; i++) {
			insert_key_to_leaf(leaf_b.keys[i], leaf_b.nodes[i], leaf_a);
		}
	}

	void delete_key_not_enough(int key, BpTreeNode node)
	{
		int i;
		BpTreeNode p = null, left = null, right = null;

		try
		{
			p = node.parent;

			if (!node.is_leaf) return;

			// get left and right sibling
			for (i = 0; i <= p.size; i++) {
				if (p.nodes[i] == node) {
					if (i != 0) left = p.nodes[i-1];
					if (i != p.size) right = p.nodes[i+1];
					break;
				}
			}

			// attempt to get a key from right
			if (right != null) {
				if (right.size > (BpTreeNode.degree+1)/2) { // enough entries
					insert_key_to_leaf(right.keys[0], right.nodes[0], node);
					delete_key_from_leaf(right.keys[0], right);
					rewrite_key(node.keys[node.size-1], right.keys[0], p);
					update_NodeInfo(node, true); // entry increases
					update_NodeInfo(right, false); //entry decreases
					return;
				}
			}

			// attempt to get a key from left
			if (left != null) {
				if(left.size > (BpTreeNode.degree+1)/2) { // enough entries
					insert_key_to_leaf(left.keys[left.size-1], left.nodes[left.size-1], node);
					delete_key_from_leaf(left.keys[left.size-1], left);
					rewrite_key(node.keys[1], node.keys[0], p);
					update_NodeInfo(node, true); // entry increases
					update_NodeInfo(left, false); // entry decreases
					return;
				}
			}

			// merge
			if (left != null) { // merge into left leaf
				left.nodes[BpTreeNode.degree] = right;
				merge_leaves(left, node);
				delete_node_from_node(node, p);
				update_NodeInfo(left, true); // entry increases
				delInfo(node);
				balance_tree(p);
			} else if (right != null) { // merge into node
			// implies node == node.parent.nodes[0]
				node.nodes[BpTreeNode.degree] = right.nodes[BpTreeNode.degree];
				merge_leaves(node, right);
				delete_node_from_node(right, p);
				update_NodeInfo(node, true); // entry increases
				delInfo(right);
				balance_tree(p);
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node) || is_brokenNode(left) || is_brokenNode(right) || is_brokenNode(p)) {
				BpTreeNode newnode = replaceNode(node);
				replaceNode(left); // reset
				replaceNode(right); // reset
				replaceNode(p);
				delete_key_not_enough(key, newnode); // try again!
			} else { // should not reach here
				System.out.println("fatal error: EMEs");
				System.exit(1);
			}
		}
	}

	void print_node(BpTreeNode node)
	{
		int i;
		for (i = 0; i < node.size; i++) {
			System.out.printf("%d(index:%d), ", node.keys[i], i);
		}
		System.out.println("");
	}

	void p(BpTreeNode node)
	{
		try
		{
			if (node == null) return ;
			if (!node.is_leaf) System.out.printf("parent => ");
			print_node(node);

			for (int i = 0; i <= node.size; i++) {
				if (node.nodes[i] != null && !node.is_leaf) {
					p(node.nodes[i]);
				}
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node)) {
				replaceNode(node);
			}
			System.out.println("EMEs: stopped printing");
			return;
		}
	}

	public void p(){ p(this.root); }
	public void treeinfo_p() // debug
	{
		BpTreeNodeInfo ni;
		ni = this.btni;
		int i;

		while (ni != null) {
			if (!ni.is_leaf) {
				System.out.println("parent => "+ni.ownaddr);
				for (i = 0; i <= ni.size; i++)
					System.out.println("child => "+ni.nodes[i]+" "+ni.nodes[i].keys[0]);
			}
			else System.out.println("leaf => "+ni.ownaddr+" "+ni.ownaddr.keys[0]);
			ni = ni.next;
		}
	}

	public void insert(int key, Object obj)
	{
		BpTreeNode node = search_node(key);

		try
		{
			if (node == null) return;
			if (node.is_key_included(key)) return;

			BpTreeNode insertnode = new BpTreeNode(obj);
			insertnode.size = key; // this entry is used to recover leafnode key

			if (node.size < BpTreeNode.degree) {
				insert_key_to_leaf(key, insertnode, node);
				update_NodeInfo(node, true);
			} else {
				insert_leaf_split(key, insertnode, node);
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node)) {
				BpTreeNode newnode = replaceNode(node);
				insert(key, newnode); // try again!
			}
		}
	}

	public void delete(int key)
	{
		int head_key;
		BpTreeNode node = search_node(key);

		try
		{
			if (node == null) return;
			if (!node.is_key_included(key)) return;

			head_key = node.keys[0];
			delete_key_from_leaf(key, node);

			if (node == this.root) {
				update_NodeInfo(node, false); // entry decreases
			} else if (node.size >= (BpTreeNode.degree+1)/2) {
				if (key == head_key)
					rewrite_key(head_key, node.keys[0], node.parent);
				update_NodeInfo(node, false); // entry decreases
			} else {
				if (key == head_key)
					rewrite_key(head_key, node.keys[0], node.parent);
				update_NodeInfo(node, false); // entry decreases
				delete_key_not_enough(key, node);
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node)) {
				replaceNode(node);
				delete(key); // try again!
			}
		}
	}

	public Object get_object(int key)
	{
		BpTreeNode node = search_node(key);
		try
		{
			if (node == null) return null;
			for (int i = 0; i < node.size; i++) {
				if (node.keys[i] == key) return node.nodes[i].obj;
			}
			return null;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenNode(node)) {
				replaceNode(node);
				return get_object(key); // try again!
			}
			return null;
		}
	}
}
