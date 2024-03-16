// myl

#ifndef SHARE_GC_SHARED_OBJECTTABLE_HPP
#define SHARE_GC_SHARED_OBJECTTABLE_HPP

#include "memory/allocation.hpp"
#include "oops/oopsHierarchy.hpp"

#define BUCKET_SIZE 1021

class objTable;

class objNode: public CHeapObj<mtGC>{
	friend objTable;

	size_t size;
	uintptr_t addr;
	intptr_t hash;
	objNode* next;

public:
	objNode(size_t size, uintptr_t addr, intptr_t hash) {
		this->size = size;
		this->addr = addr;
		this->hash = hash;
		this->next = nullptr;
	}
};

class objTable: public CHeapObj<mtGC>{
	objNode *bucket[BUCKET_SIZE];
public:
	void clearobjTable(void);

	objTable() {
		for (int i = 0; i < BUCKET_SIZE; i++) {
			this->bucket[i] = nullptr;
		}
	}
	~objTable() { clearobjTable(); }

	void addobjNode(size_t size, uintptr_t addr, intptr_t hash);
	void deleteobjNode(uintptr_t addr);
	oop getOopFromAddr(uintptr_t addr);
	void showobjTable(void);
};

#endif
