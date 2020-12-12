# Inventory Application

[![Build Status](https://travis-ci.org/elvis824/inventory-app.svg?branch=master)](https://travis-ci.org/elvis824/inventory-app)
[![codecov](https://codecov.io/gh/elvis824/inventory-app/branch/master/graph/badge.svg?token=pS9hx7OSij)](https://codecov.io/gh/elvis824/inventory-app)

## Abstract

This exercise is to create an Inventory Application for a department store.

## Features

The features of the Inventory Application are as follows:
* Inventory management
    * Querying existing inventories
    * Creating new inventory
    * Updating existing inventory details
    * Deleting existing inventory
* Category management
    * Querying existing categories
    * Creating new category
    * Updating existing category details
    * Deleting existing category
* Product management
    * Querying existing products
    * Creating new product under an existing category
        * Validation rule examples:
            1. Product "Shoes" should not be in category "Food"
            2. Product "Cake" should not be in category "Clothes"
    * Updating existing product details
    * Deleting existing product
* Stock management
    * Querying existing stock entries
    * Create new stock entry
    * Updating existing stock entries

## Running the application stack

The application stack is dockerized and can be started up with the following procedure.

1. Change directory to `./setup` in a terminal: `cd setup`
2. Optionally, clean up any existing data for a clean startup: `sudo rm -rf data logs`
3. Start the stack: `sudo docker-compose up -d`
    * inventory-app
        * this is the docker-compose service for the Inventory Application
        * its docker image, namely `inventory_app:test`, will be built if it does not exist
        * if a rebuild of the docker image is desired, run `sudo docker rmi inventory_app:test` before this step
    * inventory-db
        * this is the docker-compose service for underlying database engine, which is PostgreSQL
        * docker images will be pulled from repository as needed
4. Open http://localhost:8080/api/docs/index.html in a browser window for the Swagger UI

## Stopping the application stack

The application stack can be stopped and cleaned up with the following procedure:

1. Change directory to `./setup` in a terminal: `cd setup`
2. Stop the stack: `sudo docker-compose down`
3. Optionally, clean up database data: `sudo rm -rf data`
4. Optionally, clean up log files: `sudo rm -rf logs`
5. Optionally, remove the docker image for Inventory Application: `sudo docker rmi inventory_app:test`