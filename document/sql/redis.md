# Redis 部署

## docker 部署

### 创建文件夹
```shell
mkdir -p /data/redis/conf
mkdir -p /data/redis/data
```

### 下载镜像
```shell
docker search redis
docker pull redis 
docker image redis
```

### 创建容器
```shell
docker run -d -p 6379:6379 \
--name redis \
--restart=always \
--ip 127.0.0.1 \
-v /data/redis/data:/data \
-v /data/redis/conf/redis.conf:/etc/redis/redis.conf \
-v /data/redis/downloads:/usr/Downloads \
redis \
redis-server /etc/redis/redis.conf \
```

### Redis 交互终端
```shell
# 在容器 redis 中开启一个交互模式的终端
docker exec -it redis /bin/bash
```

### 登录
```shell
# 连接 redis
redis-cli

# 密码登录
auth root
```

### Redis 服务器
```shell
47.108.146.141:6379
```


