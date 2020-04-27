https://www.bilibili.com/video/BV1qJ411w7du?from=search&seid=5314948672627868821
fdset 
https://www.cnblogs.com/wuyepeng/p/9745573.html

用户态与内核态
https://www.bilibili.com/video/BV16J411p7f1

线程/协程/异步
https://www.bilibili.com/video/BV1S4411Z7M2

go 协程池
https://studygolang.com/articles/15477


TCP协议如何保证可靠性
确保传输可靠性的方式
TCP协议保证数据传输可靠性的方式主要有：
校验和
确认应答/序列号
超时重传
连接管理
流量控制
拥塞控制

https://blog.csdn.net/wodewutai17quiet/article/details/82252454


CSRF攻击与防御（写得非常好）Cross Site Request Forgery
https://blog.csdn.net/xiaoxinshuaiga/article/details/80766369

https://www.bilibili.com/video/BV1L4411a7RN?from=search&seid=17220403211335477566


TCP timewait 过多问题
https://www.cnblogs.com/softidea/p/5741192.html

如发现系统存在大量TIME_WAIT状态的连接，通过调整内核参数解决：
编辑文件/etc/sysctl.conf，加入以下内容：

net.ipv4.tcp_syncookies = 1
net.ipv4.tcp_tw_reuse = 1
net.ipv4.tcp_tw_recycle = 1
net.ipv4.tcp_fin_timeout = 30
然后执行 /sbin/sysctl -p 让参数生效。

net.ipv4.tcp_syncookies = 1 表示开启SYN Cookies。当出现SYN等待队列溢出时，启用cookies来处理，可防范少量SYN攻击，默认为0，表示关闭；
net.ipv4.tcp_tw_reuse = 1 表示开启重用。允许将TIME-WAIT sockets重新用于新的TCP连接，默认为0，表示关闭；
net.ipv4.tcp_tw_recycle = 1 表示开启TCP连接中TIME-WAIT sockets的快速回收，默认为0，表示关闭。
net.ipv4.tcp_fin_timeout 修改系默认的 TIMEOUT 时间
