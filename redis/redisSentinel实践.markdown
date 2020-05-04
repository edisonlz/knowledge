#### RedisSential 实践

redis raft实现：
https://blog.csdn.net/sanwenyublog/article/details/53385616

脑裂导致的数据丢失
https://www.cnblogs.com/kaleidoscope/p/9635654.html

##### 一、配置redis

1台master 2台slave
master0 6379
master0-slave 16379
master1-slave  17379

##### sentinel
3台sentinel

##### 相关文档
https://blog.csdn.net/m0_37779570/article/details/82703627




##### 二、redis-sentinel.conf配置项说明如下：
1. sentinel监听端口，默认是26379，可以修改。
port 26379
2. sentinel monitor
告诉sentinel去监听地址为ip:port的一个master，这里的master-name可以自定义，quorum是一个数字，指明当有多少个sentinel认为一个master失效时，master才算真正失效。master-name只能包含英文字母，数字，和“.-_”这三个字符需要注意的是master-ip 要写真实的ip地址而不要用回环地址（127.0.0.1）。

配置示例：
sentinel monitor mymaster 192.168.0.5 6379 2

3. sentinel auth-pass

设置连接master和slave时的密码，注意的是sentinel不能分别为master和slave设置不同的密码，因此master和slave的密码应该设置相同。

配置示例：
sentinel auth-pass mymaster 0123passw0rd

4. sentinel down-after-milliseconds

这个配置项指定了需要多少失效时间，一个master才会被这个sentinel主观地认为是不可用的。 单位是毫秒，默认为30秒
配置示例：
sentinel down-after-milliseconds mymaster 30000

5.sentinel parallel-syncs

这个配置项指定了在发生failover主备切换时最多可以有多少个slave同时对新的master进行同步，这个数字越小，完成failover所需的时间就越长，但是如果这个数字越大，就意味着越多的slave因为replication而不可用。可以通过将这个值设为 1 来保证每次只有一个slave处于不能处理命令请求的状态。

配置示例：
sentinel parallel-syncs mymaster 1



sentinel failover-timeout
failover-timeout 可以用在以下这些方面：

  1. 同一个sentinel对同一个master两次failover之间的间隔时间。
  2. 当一个slave从一个错误的master那里同步数据开始计算时间。直到slave被纠正为向正确的master那里同步数据时。
  3. 当想要取消一个正在进行的failover所需要的时间。  
  4. 当进行failover时，配置所有slaves指向新的master所需的最大时间。不过，即使过了这个超时，slaves依然会被正确配置为指向master，但是就不按parallel-syncs所配置的规则来了。

配置示例：
sentinel failover-timeout mymaster1 20000

7.sentinel的notification-script和reconfig-script是用来配置当某一事件发生时所需要执行的脚本，可以通过脚本来通知管理员，例如当系统运行不正常时发邮件通知相关人员。对于脚本的运行结果有以下规则：

    若脚本执行后返回1，那么该脚本稍后将会被再次执行，重复次数目前默认为10

    若脚本执行后返回2，或者比2更高的一个返回值，脚本将不会重复执行。

    如果脚本在执行过程中由于收到系统中断信号被终止了，则同返回值为1时的行为相同。

    一个脚本的最大执行时间为60s，如果超过这个时间，脚本将会被一个SIGKILL信号终止，之后重新执行。

1).sentinel notification-script

通知型脚本:当sentinel有任何警告级别的事件发生时（比如说redis实例的主观失效和客观失效等等），将会去调用这个脚本，这时这个脚本应该通过邮件，SMS等方式去通知系统管理员关于系统不正常运行的信息。调用该脚本时，将传给脚本两个参数，一个是事件的类型，一个是事件的描述。如果sentinel.conf配置文件中配置了这个脚本路径，那么必须保证这个脚本存在于这个路径，并且是可执行的，否则sentinel无法正常启动成功。

配置示例：

sentinel notification-script mymaster /var/redis/notify.sh

2).sentinel client-reconfig-script

当一个master由于failover而发生改变时，这个脚本将会被调用，通知相关的客户端关于master地址已经发生改变的信息。以下参数将会在调用脚本时传给脚本:

   <master-name> <role> <state> <from-ip> <from-port> <to-ip> <to-port>
1
目前总是“failover”, 是“leader”或者“observer”中的一个。 参数 from-ip, from-port, to-ip, to-port是用来和旧的master和新的master(即旧的slave)通信的。这个脚本应该是通用的，能被多次调用，不是针对性的。

配置示例：

sentinel client-reconfig-script mymaster /var/redis/reconfig
