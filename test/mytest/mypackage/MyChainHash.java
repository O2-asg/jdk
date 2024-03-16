package mypackage;

public class MyChainHash {
	public static final int BUCKET_SIZE = 1021;

	MyList tbl[];

	public MyChainHash()
	{
		this.tbl = new MyList[BUCKET_SIZE];
	}

	public void hash_store(int key, Object obj)
	{
		int idx = key % BUCKET_SIZE;

		if (this.tbl[idx] == null) {
			this.tbl[idx] = new MyList();
		}

		this.tbl[idx].addNode(key, obj);
	}

	public void hash_delete(int key)
	{
		int idx = key % BUCKET_SIZE;

		if (this.tbl[idx] == null)
			return;

		this.tbl[idx].delNode(key);
	}

	public Object hash_get(int key)
	{
		int idx = key % BUCKET_SIZE;

		if (this.tbl[idx] == null)
			return null;

		return this.tbl[idx].getObject(key);
	}
}
