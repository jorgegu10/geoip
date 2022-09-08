FROM postgres:12.9-alpine

ENV POSTGRES_PASSWORD=admin

COPY init-db.sql /docker-entrypoint-initdb.d/init-db.sql