CREATE USER orderservice;
CREATE DATABASE orderdb;
GRANT ALL PRIVILEGES ON DATABASE orderdb to orderservice;

CREATE USER customerservice;
CREATE DATABASE customerdb;
GRANT ALL PRIVILEGES ON DATABASE customerdb to customerservice;

CREATE USER kitchenservice;
CREATE DATABASE kitchendb;
GRANT ALL PRIVILEGES ON DATABASE kitchendb to kitchenservice;

CREATE user accountingservice;
CREATE DATABASE accountingdb;
GRANT ALL PRIVILEGES ON DATABASE accountingdb to accountingservice;
