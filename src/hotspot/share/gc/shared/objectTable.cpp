#include "objectTable.hpp"
#include "precompiled.hpp"

void objTable::clearobjTable(void)
{
	int i;
	objNode *n, *target;

	for (i = 0; i < BUCKET_SIZE; i++) {
		n = bucket[i];
		while (n != nullptr) {
			target = n;
			n = n->next;
			delete target;
		}
	}
}

void objTable::addobjNode(size_t size, uintptr_t addr, intptr_t hash)
{
	int hash_value = addr % BUCKET_SIZE;

	objNode *newobj = new objNode(size, addr, hash);

	newobj->next = this->bucket[hash_value];
	this->bucket[hash_value] = newobj;
}

void objTable::deleteobjNode(uintptr_t addr)
{
	int hash_value = addr % BUCKET_SIZE;

	objNode *n = this->bucket[hash_value];
	objNode *target = nullptr;

	if (n->addr == addr) {
		this->bucket[hash_value] = n->next;
		return;
	}

	while (n != nullptr) {
		if (n->next->addr == addr) {
			target = n->next;
			n->next = target->next;
			delete target;
			return;
		}
		n = n->next;
	}
}

oop objTable::getOopFromAddr(uintptr_t addr)
{
	int i;
	uintptr_t start, end;
	objNode *o;

	for (i = 0; i < BUCKET_SIZE; i++) {
		o = this->bucket[i];
		while (o != nullptr) {
			start = o->addr;
			end = start + (o->size << LogHeapWordSize);
			if ((start <= addr) && (addr < end)) {
				return cast_to_oop(o->addr);
			}
			o = o->next;
		}
	}

	return nullptr;
}

void objTable::showobjTable(void)
{
	objNode *n;
	FILE *fp = os::fopen("/home/vmuser/jdk/mylogfile.log", "a");

	for (int i = 0; i < BUCKET_SIZE; i++) {
		n = this->bucket[i];
		while (n != nullptr) {
			fprintf(fp, "showobjTable: object hashCode is %lx, addr is %lx, size is %ld\n", \
				n->hash, n->addr, n->size);
			n = n->next;
		}
	}

	fclose(fp);
}
