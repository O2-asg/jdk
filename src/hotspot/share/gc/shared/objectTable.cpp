// mdf: objectTable.cpp

#include "objectTable.hpp"
#include "oops/oop.hpp" // to use identity_hash()
#include "oops/oop.inline.hpp" // to use identity_hash()
#include "runtime/jniHandles.hpp"
#include "runtime/jniHandles.inline.hpp"
#include "precompiled.hpp"

void objTable::clearobjTable(void)
{
	objNode *n;
	oop o;

	n = this->head;
	while (n->next != nullptr) {
		o = JNIHandles::resolve(n->next->obj);
		if (o != nullptr) JNIHandles::destroy_weak_global(n->next->obj);
		n = n->next;
	}
	this->head = nullptr;
}

// only deallocate jni weak Handle (if any)
void objTable::deleteobjNode_internal(objNode* target)
{
	oop o = JNIHandles::resolve(target->obj);

	if (o != nullptr) { // object is still live
		return; // keep tracking
	} else { // object has been dead
		JNIHandles::destroy_weak_global(target->obj);
		target->obj = nullptr;
//		delete target; // this code is problematic
	}
}

void objTable::addobjNode(Thread* thread, oop obj)
{
	jweak weak_obj = JNIHandles::make_weak_global(Handle(thread, obj));
	objNode *newobj = new objNode(obj->size(), weak_obj, obj->identity_hash());

	newobj->next = this->head->next;
	this->head->next = newobj;
}

void objTable::deleteobjNode(uintptr_t addr)
{
	objNode *n = this->head;
	objNode *target = nullptr;

/*	if (n->addr == addr) {
		this->bucket[hash_value] = n->next;
		return;
	}*/
/*	if ((uintptr_t)n->h() == addr) {
		this->head = n->next;
		return;
	}*/

/*	while (n != nullptr) {
		if (n->next->addr == addr) {
			target = n->next;
			n->next = target->next;
			delete target;
			return;
		}
		n = n->next;
	}*/
}

intptr_t objTable::getHashFromAddr(uintptr_t addr)
{
	if (addr == 0) return 0;
	uintptr_t start, end;
	oop tmp;
	objNode *n;

	n = this->head;
	while (n->next != nullptr) {
		tmp = JNIHandles::resolve(n->next->obj);
		if (tmp != nullptr) {
			start = (uintptr_t)p2i(tmp);
			end = start + (n->next->size << LogHeapWordSize);
			if ((start <= addr) && (addr < end)) {
				return n->next->hash;
			}
		} else { // object has been collected
			deleteobjNode_internal(n->next);
			n->next = n->next->next;
			continue;
		}
		n = n->next;
	}

	return 0;
}

void objTable::showobjTable(void)
{
	objNode *n = this->head;
	oop o;
	FILE *fp = os::fopen("/home/vmuser/jdk/mylogfile.log", "a");

	while (n->next != nullptr) {
		o = JNIHandles::resolve(n->next->obj);
		if (o != nullptr) {
			fprintf(fp, "showobjTable: hash is %lx, addr is %lx, before addr is %lx, size is %ld\n",
				n->next->hash, p2i(o), n->next->before_addr, n->next->size);
		} else { // original object has been deleted (collected)
			deleteobjNode_internal(n->next);
			fprintf(fp, "removed a record\n");
			n->next = n->next->next;
			continue;
		}
		n = n->next;
	}

	fclose(fp);
}

void objTable::recordBeforeAddr(uintptr_t addr)
{
	oop tmp;
	objNode *n;

	n = this->head;
	while (n != nullptr) {
		tmp = JNIHandles::resolve(n->obj);
		if ((uintptr_t)p2i(tmp) == addr) {
			n->before_addr = (uintptr_t)p2i(tmp);
			return;
		}
		n = n->next;
	}
}
