#!/bin/sh

SHELL_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
BASE_DIR="$( cd "$SHELL_DIR/.." && pwd  )"

app_name="entdiy-tomcat"
port="8080"

while getopts p: opt
do
  case $opt in
    p)
      port="$OPTARG"
    ;;    
  esac
done
shift $((OPTIND-1))

app_dir=${BASE_DIR}/nodes/${port}/webapp
war_dir=${BASE_DIR}/war
data_dir=${BASE_DIR}/data
config_dir=${BASE_DIR}/config
log_dir=${BASE_DIR}/nodes/${port}/logs
docker_name=${app_name}-${port}

case "$1" in
    start)
    echo docker run ${docker_name}...
    mkdir -p ${app_dir} ; mkdir -p ${data_dir}/upload; mkdir -p ${config_dir}; mkdir -p ${log_dir}
    deploy_dir="/usr/local/tomcat/webapps"
    docker run --name ${docker_name} -p 8080:${port} --restart=always --privileged=true \
               --link entdiy-redis:redis-server \
               --link entdiy-mysql:mysql-server \
                -v $app_dir:$deploy_dir \
                -v $log_dir:/usr/local/tomcat/logs \
                -v $config_dir/app:/etc/entdiy/config \
                -v $data_dir:/etc/entdiy/data \
                -v $config_dir/tomcat/server.xml:/usr/local/tomcat/conf/server.xml \
                -e JAVA_OPTS="-Xms256m -Xmx4096m -Dspring.profiles.active=production" \
                -e CATALINA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dfile.encoding=utf-8" \
                -d entdiy/oracle-tomcat:8-jre8

    echo docker started for $docker_name.

    maxWaitTimes=50
    url=http://localhost:$port/entdiy/api/pub/ping
    times=0
    echo  Waiting for tomcat webapp startup
    while [ $times -lt $maxWaitTimes ] ; do
      let times++
      sleep 1s
      # 此处故意设计为不断追加新行显示方式而不是一行不停追加，主要是为了友好支持Jenkins界面不断刷新新行输出方式
      echo ${times}s...
      if [ $times -lt 10 ] ; then
        continue
      fi
      response=`curl -s -I $url | grep '200 OK'`
      if [ -n "$response" ] ;then
        break
      fi
    done
    echo Tomcat $port webapp fully startup.
    ;;  
    stop)
    cids=$(docker ps -aq --filter "name=$docker_name")
    if [ "$cids" == "" ]; then
       echo "Not running"
    else
       echo docker stop and rm container $docker_name...
       docker stop -t 10 $cids && docker rm $cids
       echo docker stopped for $docker_name.
    fi
    ;;
    restart)
    $0 -p $port stop
    $0 -p $port start
    ;;
    deploy)
    $0 -p $port stop
    rm -fr ${app_dir}
    mkdir -p ${app_dir}
    cp -v ${war_dir}/* ${app_dir}/.
    $0 -p $port start
    ;;
    status)
    cids=$(docker ps -aq --filter "name=$docker_name")
    if [ "$cids" == "" ]; then
       echo "Not running"
    else
       docker ps -a --filter "name=$docker_name"
    fi
    ;;
    init)
    SQL="CREATE DATABASE entdiy DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;"
    echo Execute: ${SQL}
    docker exec -i mysql mysql -h localhost -u root -pmysqlP@sswd123 <<< "${SQL}"
    ;;
    *)
    echo "Usage: $0 {start|stop|restart|deploy|status}"
    exit 1
    ;;
esac
exit 0
