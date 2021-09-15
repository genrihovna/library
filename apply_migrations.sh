#!/usr/bin/env bash

flyway -user=root -password=root -url=jdbc:mysql://localhost:3306/library_management_system -locations=filesystem:./migrations migrate