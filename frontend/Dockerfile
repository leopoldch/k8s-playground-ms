FROM node:alpine as builder
COPY . /g4008-frontend
WORKDIR /g4008-frontend
RUN npm install
RUN npm run build

FROM nginx:1.15-alpine
COPY --from=builder /g4008-frontend/build /usr/share/nginx/html
EXPOSE 80
