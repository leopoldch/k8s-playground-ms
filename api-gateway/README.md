## Packaging the application
` $ mvn install`

## Running the application
` $ java -jar gateway-0.0.1-SNAPSHOT.jar --glo4008.logic.api.url=$LOGIC_API_URL --glo4008.feedback.api.url=$FEEDBACK_API_URL` 

## Building the container
` $ docker build -f Dockerfile -t localregistry.lc:30500/api-gateway . `

## Running the container
``` 
$ docker run -d -p 8080:8080 -e LOGIC_API_URL=$LOGIC_API_URL FEEDBACK_API_URL=$FEEDBACK_API_URL  localregistry.lc:30500//api-gateway  
```

#### Native docker support needs the Container IP
CONTAINER_IP: To forward messages to the logic-api container we need to get  its IP. To do so execute:

` $ docker container list`

Copy the id of logic-api container and execute:

` $ docker inspect <container_id> `

The Containers IP address is found under the property NetworkSettings.IPAddress, use it in the RUN command.

#### Docker Machine on a VM 
Get Docker Machine IP by executing:

` $ docker-machine ip `

Use this one in the command.


## Pushing the container
```
$ docker tag localregistry.lc:30500/api-gateway localregistry.lc:30500/api-gateway:submission
$ docker push localregistry.lc:30500/api-gateway:submission 
```


