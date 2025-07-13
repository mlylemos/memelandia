CREATE DATABASE userdb;
CREATE DATABASE categorydb;
CREATE DATABASE memedb;

CREATE USER memelandia_user WITH PASSWORD 'memelandia123';

GRANT ALL PRIVILEGES ON DATABASE userdb TO memelandia_user;
GRANT ALL PRIVILEGES ON DATABASE categorydb TO memelandia_user;
GRANT ALL PRIVILEGES ON DATABASE memedb TO memelandia_user;