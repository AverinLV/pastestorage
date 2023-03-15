FROM postgres
ENV POSTGRES_DB paste_db
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_USER myuser
COPY psql_dump.sql /docker-entrypoint-initdb.d/