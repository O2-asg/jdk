// mdf <- this symbol represents modified by me

#ifndef SHARE_GC_SHARED_OBJECTTABLE_HPP
#define SHARE_GC_SHARED_OBJECTTABLE_HPP

#include "memory/allocation.hpp"
#include "oops/oopsHierarchy.hpp"
#include "runtime/handles.hpp"
#include "runtime/jniHandles.hpp"
#include "runtime/javaThread.hpp"

#define BUCKET_SIZE 1021

class objTable;

class objNode: public CHeapObj<mtGC>{
	friend objTable;

private:
	size_t size; // object size
	jweak obj; // jni weak global reference (address)
	intptr_t hash; // identity hash code
	objNode* next;

public:
	objNode(size_t size, jweak obj, intptr_t hash) {
		this->size = size;
		this->obj = obj;
		this->hash = hash;
		this->next = nullptr;
	}
};

class objTable: public CHeapObj<mtGC>{
private:
	objNode* head;
	void clearobjTable(void);
	void deleteobjNode_internal(objNode* target);
public:
	objTable() {
		this->head = new objNode(0, nullptr, -1); // dummy head
	}
	~objTable() { clearobjTable(); }

	void addobjNode(Thread* thread, oop obj);
	void deleteobjNode(uintptr_t addr);
	intptr_t getHashFromAddr(uintptr_t addr);
	void showobjTable(void);
};

#endif
