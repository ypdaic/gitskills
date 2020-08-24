//头文件
#include <pthread.h>
#include <stdio.h>
//导入自己的.h文件
#include "com_daiyanping_cms_concurrent_ZLThread.h"
//定义一个变量，接受创建线程后的线程id
pthread_t pid;

//定义线程的主体函数

void * thread_entity(void* arg) {
	while(1) {
		usleep(100);
		printf("i am new Thread! from c \n");
	}

}

JNIEXPORT void JNICALL Java_com_daiyanping_cms_concurrent_ZLThread_start1(JNIEnv *env, jobject c1) {
	//调用操作系统的函数创建线程，注意四个参数
	pthread_create(&pid,NULL,thread_entity,NULL);
	while(1) {
		usleep(100);
		printf("I am main\n");
	}
}