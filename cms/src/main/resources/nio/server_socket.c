#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
// liunx 原生创建socket
void conn() {
// 网络地址结构 server端
	struct sockaddr_in my_addr;
	my_addr.sin_family = AF_INET;
	my_addr.sin_port = htons(9090);
	my_addr.sin_addr.s_addr = htonl(INADDR_ANY);
	char recv_buf[512]="";
    
    // 客户端地址结构体
	struct sockaddr_in client_addr;
    // 服务端serverSocket文件描述符
	int listenfd = socket(AF_INET,SOCK_STREAM,0);
	// 绑定端口
	bind(listenfd, (struct sockaddr *)&my_addr, sizeof(my_addr));
    listen(listenfd, 128);

	socklen_t cliaddr_len = sizeof(client_addr);
	// 接受客户端连接
    int clientfd = accept(listenfd, (struct sockaddr*)&client_addr, &cliaddr_len);
    while (1) {
        // 读取客户端数据
    	int k = read(clientfd, recv_buf, sizeof(recv_buf));
    	if (k > 0) {
    		printf("%s\n", recv_buf);
    	}
    }
}

int main() {
	conn();
	return 0;
}