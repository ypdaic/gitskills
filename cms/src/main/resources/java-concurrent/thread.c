//头文件
#include <pthread.h>
#include <stdio.h>

//定义一个变量，接受创建线程后的线程id
pthread_t pid;

//定义线程的主体函数

void * thread_entity(void* arg) {
	printf("i am new Thread! from c");
}

//main方法，程序入口，main和java的main一样会产生一个进程，继而产生一个main线程
int main() {
	//调用操作系统的函数创建线程，注意四个参数
	pthread_create(&pid,NULL,thread_entity,NULL);
	//usleep是睡眠的意思，那么这里的睡眠是让谁睡眠呢？
	//为什么需要睡眠？如果不睡眠会出现什么情况
	usleep(100);
	printf("main\n");
	return 0;
}
