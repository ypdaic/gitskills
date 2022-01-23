1) 在.ssh目录下创建config文件，然后配置IdentityFile，需要是绝对路径
        
        IdentityFile ~/.ssh/id_rsa_2

2) 为不同的目标服务器设置私钥
    
        Host：
        代码托管平台的别名,但是这个别名和后面要用到的ssh链接 git@github.com:xxx/xxx.git 中的 @ 符号后面的内容要一致，而一般来说github默认提供的就是git@github.com，因此为了方便，github的Host写github.com即可，别取别名了
        HostName：
        代码托管平台真正的IP地址或域名,写域名就行，
        IdentityFile：
        对应的密钥文件路径。必须写绝对路径，windows下可以写 C:\Users\xxx.ssh\yyxtest
        PreferredAuthentications：
        配置登录时用什么权限认证。可设为publickey，password publickey，keyboard-interactive等
        User：
        对应的用户名。
        
        我这里有两个私钥，所以我的配置文件如下
        
        Host git.coding.net
            HostName git.coding.net
            IdentityFile C:\Users\kevin\.ssh\rsa_coding
            PreferredAuthentications publickey
            User yangyanxing
        Host github.com
            HostName github.com
            IdentityFile C:\Users\kevin\.ssh\yyx
            PreferredAuthentications publickey
            User kevinkelin