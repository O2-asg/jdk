package epackage;

// EMEs-aware chain hash
public class EChainHash_v2 {
	// size can be tunable
	// should be a prime number?
	public static final int BUCKET_SIZE = 1021;

	// EMEs occur when accessing tbl[idx]
	// two arrays should be different object (do not copy like "tbl = backup_tbl" !)
	// uses the same EList.head and EList.cr
	EList_v2 tbl[];
	EList_v2 backup_tbl[]; // m-list

	// EMEs can occur here
	public EChainHash_v2()
	{
		this.tbl = new EList_v2[BUCKET_SIZE];
		this.backup_tbl = new EList_v2[BUCKET_SIZE];
	}

	// broken table check
	boolean is_brokenArray(EList_v2 lst[])
	{
		try
		{
			for (int i = 0; i < BUCKET_SIZE; i++)
				lst[i] = lst[i];

			return false; // not broken
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			return true; // broken
		}
	}

	// create a new table from argument
	EList_v2[] reallocate_table(EList_v2 lst[])
	{
		EList_v2 ret[] = null;

		try
		{
			ret = new EList_v2[BUCKET_SIZE];

			for (int i = 0; i < BUCKET_SIZE; i++) {
				if (lst[i] != null)
					ret[i] = lst[i].cloneInstance();
			}

			return ret;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenArray(lst)) return null;
			if (is_brokenArray(ret)) return reallocate_table(lst); // try again!

			return null;
		}
	}

	// store new key and value
	public void hash_store(Object obj, int hashcode)
	{
		int idx = hashcode % BUCKET_SIZE;

		try
		{
			if (this.tbl[idx] == null) {
				this.tbl[idx] = new EList_v2(obj, hashcode);
				this.backup_tbl[idx] = this.tbl[idx].cloneInstance();
				return;
			}

			this.tbl[idx].addNode(obj, hashcode);

			if (this.tbl[idx].head != this.backup_tbl[idx].head) { // head replaced
				this.backup_tbl[idx].head = this.tbl[idx].head;
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenArray(this.tbl)) {
				this.tbl = reallocate_table(this.backup_tbl);
				if (this.tbl == null) {
					System.out.println("fatal: backup table is broken");
				}
			}
		}
	}

	// delete key and value using key
	public void hash_delete(int hashcode)
	{
		int idx = hashcode % BUCKET_SIZE;

		try
		{
			if (this.tbl[idx] == null)
				return;

			this.tbl[idx].delNode(hashcode);
			if (this.tbl[idx].head != this.backup_tbl[idx].head) { // head replaced
				this.backup_tbl[idx].head = this.tbl[idx].head;
			}
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenArray(this.tbl)) {
				this.tbl = reallocate_table(this.backup_tbl);
				if (this.tbl == null) {
					System.out.println("fatal: backup table is broken");
				}

			}
		}
	}

	// get object from hashcode (key)
	public Object hash_get(int hashcode)
	{
		int idx = hashcode % BUCKET_SIZE;

		try
		{
			if (this.tbl[idx] == null)
				return null;

			Object ret = this.tbl[idx].getObject(hashcode);
			if (this.tbl[idx].head != this.backup_tbl[idx].head) { // head replaced
				this.backup_tbl[idx].head = this.tbl[idx].head;
			}

			return ret;
		}
		catch (ECCuncorrectableMemoryError eme)
		{
			if (is_brokenArray(this.tbl)) {
				this.tbl = reallocate_table(this.backup_tbl);
				if (this.tbl == null) return null;
				return hash_get(hashcode); // try again!
			}

			return null;
		}
	}
}
