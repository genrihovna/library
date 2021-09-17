# How start development

## Start local DB

Go to directory `docs/docker`, then run in CLI

> docker-compose up db

## Run migrations

In cli just run flyway script:

> ./apply_migrations.sh

Or run maven plugin command

> mvn flyway:migrate
