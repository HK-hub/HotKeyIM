# 服务器
## N7Cloud:111.92.241.177
ip:111.92.241.177
用户名：root
密码： UAfQC5f1yd0Y
端口：默认

### 部署内容
- RocketMQ 消息队列: 111.92.241.177:9800
- MySQL:111.92.241.177,root,mysql@zy.com


### 服务内容
#### MySQL
```shell
docker run  -d  \
--name mysql8 \
--privileged=true \
--restart=always \
-p 3306:3306 \
-v /data/mysql/data:/var/lib/mysql \
-v /data/mysql/config:/etc/mysql/conf.d  \
-v /data/mysql/logs:/logs \
-e MYSQL_ROOT_PASSWORD=mysql@zy.com \
-e TZ=Asia/Shanghai mysql:8.0.21 \
--lower_case_table_names=1  
```

```shell
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'mysql@zy.com';
```

