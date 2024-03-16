package mypackage;

public class MyBpTree {
	BpTreeNode root;

	public MyBpTree(int key, Object obj)
	{
		this.root = new BpTreeNode(key, obj);
	}

	int minimum_key(BpTreeNode node)
	{
		if (node.is_leaf)
			return node.keys[0];
		else
			return minimum_key(node.nodes[0]);
	}

	BpTreeNode search_node(int key)
	{
		int i;
		BpTreeNode node = this.root;

		while (!node.is_leaf) {
			for (i=0; i < node.size; i++) {
				if (node.keys[i] > key) break;
			}

			node = node.nodes[i];
		}

		return node;
	}

	void insert_node_to_node(BpTreeNode addnode, BpTreeNode node)
	{
		int i, key, tmpkey;

		if (node.size == BpTreeNode.degree || node.is_leaf) {
			addnode.parent = node;
			return;
		}

		key = minimum_key(addnode);

		if (node.size == 0) {
			if (node.nodes[0] == null) {
				node.nodes[0] = addnode;
				addnode.parent = node;
				return;
			}

			if (addnode == node.nodes[0]) return;

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
			return;
		}

		for (i = 0; i <= node.size; i++)
			if (addnode == node.nodes[i]) return;

		for (i = node.size; i > 0 && key < node.keys[i-1]; i--) {
			node.keys[i] = node.keys[i-1];
			node.nodes[i+1] = node.nodes[i];
		}

		node.keys[i] = key;
		node.nodes[i+1] = addnode;
		node.size++;
		addnode.parent = node;
	}

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
	}

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
	}

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
	}

	void insert_node_split(BpTreeNode right, BpTreeNode node)
	{
		BpTreeNode left_node = node, right_node = null;
		BpTreeNode p = left_node.parent;
		int i;
		int right_minkey = minimum_key(right);
		int d = BpTreeNode.degree;

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

		insert_nodes_to_node(left_node, right_node, p);
	}

	void insert_nodes_to_node(BpTreeNode left, BpTreeNode right, BpTreeNode node)
	{
		int right_minkey = minimum_key(right);

		if (node == null) {
			this.root = new BpTreeNode();
			this.root.keys[0] = right_minkey;
			this.root.size = 1;
			this.root.nodes[0] = left;
			this.root.nodes[1] = right;
			left.parent = this.root;
			right.parent = this.root;
			return;
		}

		if (node.size < BpTreeNode.degree) {
			insert_node_to_node(right, node);
			return;
		}

		if (node.size == BpTreeNode.degree) {
			insert_node_split(right, node);
		}
	}

	void insert_leaf_split(int key, BpTreeNode objnode, BpTreeNode node)
	{
		BpTreeNode left = node, right = null;
		BpTreeNode p = left.parent;
		int i;
		int d = BpTreeNode.degree;

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

		insert_nodes_to_node(left, right, p);
	}

	void rewrite_key(int before, int after, BpTreeNode node)
	{
		if (node == null) return;

		for (int i = 0; i < node.size; i++) {
			if (node.keys[i] == before) {
				node.keys[i] = after;
				break;
			}
		}
		rewrite_key(before, after, node.parent);
	}

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

		p = node.parent;
		if (p == null) { // root node
			if (node.nodes[1] != null) { // two or more sub-trees
				// do nothing
			} else { // only one sub-tree
				this.root = node.nodes[0];
				this.root.parent = null;
			}
			return; // finish balancing
		}

		if (node.nodes[(BpTreeNode.degree)/2] != null) { // entries >= ROUNDUP((degree+1)/2)
			// node has enough entries, do nothing
			return;
		} else {
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
					return;
				}
			}

			// merge
			if (left != null) {
				merge_nodes(left, node);
				delete_node_from_node(node, p);
				balance_tree(p);
			} else if (right != null) {
				// implies node is leftmost child of node.parent
				merge_nodes(node, right);
				delete_node_from_node(right, p);
				balance_tree(p);
			}
		}
	}

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
				return;
			}
		}

		// attempt to get a key from left
		if (left != null) {
			if(left.size > (BpTreeNode.degree+1)/2) { // enough entries
				insert_key_to_leaf(left.keys[left.size-1], left.nodes[left.size-1], node);
				delete_key_from_leaf(left.keys[left.size-1], left);
				rewrite_key(node.keys[1], node.keys[0], p);
				return;
			}
		}

		// merge
		if (left != null) { // merge into left leaf
			left.nodes[BpTreeNode.degree] = right;
			merge_leaves(left, node);
			delete_node_from_node(node, p);
			balance_tree(p);
		} else if (right != null) { // merge into node
		// implies node == node.parent.nodes[0]
			node.nodes[BpTreeNode.degree] = right.nodes[BpTreeNode.degree];
			merge_leaves(node, right);
			delete_node_from_node(right, p);
			balance_tree(p);
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
		if (node == null) return;
		if (!node.is_leaf) System.out.printf("parent => ");
		print_node(node);

		for (int i = 0; i <= node.size; i++) {
			if (node.nodes[i] != null && !node.is_leaf) {
				p(node.nodes[i]);
			}
		}
	}

	public void p() { p(this.root); }

	public void insert(int key, Object obj)
	{
		BpTreeNode node = search_node(key);

		if (node == null) return;
		if (node.is_key_included(key)) return;

		BpTreeNode insertnode = new BpTreeNode(obj);
		insertnode.size = key; // this entry is used to recover leafnode key

		if (node.size < BpTreeNode.degree) {
			insert_key_to_leaf(key, insertnode, node);
		} else {
			insert_leaf_split(key, insertnode, node);
		}
	}

	public void delete(int key)
	{
		int head_key;
		BpTreeNode node = search_node(key);

		if (node == null) return;
		if (!node.is_key_included(key)) return;

		head_key = node.keys[0];
		delete_key_from_leaf(key, node);

		if (node == this.root) {
			// do nothing
		} else if (node.size >= (BpTreeNode.degree+1)/2) {
			if (key == head_key)
				rewrite_key(head_key, node.keys[0], node.parent);
		} else {
			if (key == head_key)
				rewrite_key(head_key, node.keys[0], node.parent);
			delete_key_not_enough(key, node);
		}
	}

	public Object get_object(int key)
	{
		BpTreeNode node = search_node(key);

		if (node == null) return null;
		for (int i = 0; i < node.size; i++) {
			if (node.keys[i] == key) return node.nodes[i].obj;
		}
		return null;
	}
}
