#### запустить в докере 1 ноду кафки
`docker run -d -p 2181:2181 -p 3030:3030 -p 8081:8081 -p 8082:8082 -p 8083:8083 -p 9092:9092 --name kafka -e ADV_HOST=127.0.0.1 landoop/fast-data-dev:2.6.2`

#### дашбоард
`localhost:3030`

#### подключится к кафке
`docker exec -it kafka bash`

#### посмотреть топики в кафке
`kafka-topics --list --zookepeer localhost:2181`

#### создать топик в кафке
`kafka-topics --create --zookepeer localhost:2181 --replication-factor 1 --partitions 1 --topic <topic_name>`