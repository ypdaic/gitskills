#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
 
int sharei = 0;
void increase_num(void);
// add mutex
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
 
int main()
{
  int ret;
  pthread_t thread1,thread2,thread3;
  ret = pthread_create(&thread1,NULL,(void *)&increase_num,NULL);
  ret = pthread_create(&thread2,NULL,(void *)&increase_num,NULL);
  ret = pthread_create(&thread3,NULL,(void *)&increase_num,NULL);
 
  pthread_join(thread1,NULL);
  pthread_join(thread2,NULL);
  pthread_join(thread3,NULL);
 
  printf("sharei = %d\n",sharei);
 
  return 0;
}
//run
 
void increase_num(void)
{
  long i,tmp;
  for(i =0;i<=9999;++i)
  {
	 
    //上锁
    // pthread_mutex_lock(&mutex);
	//有同学问为什么不加锁会小于30000
	//比如当t1 执行到tmp=sharei的时候假设 sharei这个时候=0，那么tmp也等于0
	//然后tmp=tmp+1;结果tmp=1（t1）;这个时候如果t2进入了
	//t2获取的sharei=0；然后重复t1的动作 tmp=1（t2）
	//t2 sharei = tmp;  sharei = 1;
	//然后CPU切回t1 执行 sharei = tmp;  sharei = 1;
	//结果两个线程执行了两遍但是结果还是sharei = 1;（本来要等于2的）
    tmp=sharei;
    tmp=tmp+1;
    
    sharei = tmp;
	//解锁
    // pthread_mutex_unlock(&mutex);
    
  }
}
