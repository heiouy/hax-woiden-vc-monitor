#jar包及其配置文件目录
DIRECTORY=/root/workspaces/hax-woiden-vc-monitor
#jar包名称
JAR_FILE_NAME=hax-woiden-vc-monitor-1.0-SNAPSHOT.jar

if [ ! -d `echo ${DIRECTORY}/logs/` ];then
        mkdir ${DIRECTORY}/logs/
fi

LOG_DATE=`date "+%Y%m%d%H"`
LOG_PATH=${DIRECTORY}/logs/log_info_${LOG_DATE}.log

nohup java -DpropertiesURI=${DIRECTORY}/monitor.properties -jar ${DIRECTORY}/${JAR_FILE_NAME} > ${LOG_PATH} &
