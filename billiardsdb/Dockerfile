# Use an official MariaDB base image
FROM mariadb:latest
# Expose the port on which MariaDB will run
EXPOSE 3306
# Copy the SQL dump file into the image
COPY dump-billiards-202401161258.sql /docker-entrypoint-initdb.d/