## Starting the Web App Locally
` $ yarn start `

## Building the application
` $ yarn build `

## Building the container
` $ docker build -f Dockerfile -t localregistry.lc:30500/frontend . `

## Running the container
` $ docker run -d -p 80:80 localregistry.lc:30500/frontend `

## Pushing the container
` $ docker push localregistry.lc:30500/frontend `