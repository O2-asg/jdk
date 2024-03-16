#include "jni.h"
#include "jvm.h"

#include "java_lang_ECCuncorrectableMemoryException.h"

JNIEXPORT jint JNICALL
Java_java_lang_ECCuncorrectableMemoryException_getBrokenObjectHash(JNIEnv *env, jobject throwable)
{
	return JVM_GetBrokenObjectHash(env, throwable);
}
