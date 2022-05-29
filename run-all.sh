#!/bin/bash

# Run each service in separated terminal

props=../local.env
container_name=some-postgres

if [ "$( docker container inspect -f '{{.State.Running}}' $container_name )" == "false" ]; then
  echo "Starting postgres..."
  docker rm $(docker ps -a -q)
  docker run -p 5432:5432 --name some-postgres -e POSTGRES_PASSWORD=root -d postgres
fi

export $(cat $props | xargs)

for d in ./*/;
do
	(cd "$d" && kitty sh -c "mvn spring-boot:run" &)
done

unset $(grep -v '^#' ${props} | sed -E 's/(.*)=.*/\1/' | xargs)

exit
