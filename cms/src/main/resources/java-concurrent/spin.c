#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
 
int sharei = 0;
void increase_num(void);
//定义一把自旋锁
pthread_spinlock_t a_lock;
 
int main()
{
	//初始化自旋锁
  pthread_spin_init(&a_lock, 0);
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
 
void increase_num(void)
{
  long i,tmp;
  for(i =0;i<=9999;++i)
  {
    // lock spin 自旋
    pthread_spin_lock(&a_lock);
    tmp=sharei;
    tmp=tmp+1;
    
    sharei = tmp;
    pthread_spin_unlock(&a_lock);
    
  }
}
