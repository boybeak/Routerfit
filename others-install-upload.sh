#!/bin/bash

./gradlew i-router:install && ./gradlew i-router:bintrayUpload &&
./gradlew i-router-compiler:install && ./gradlew i-router-compiler:bintrayUpload &&
./gradlew i-router-register:install && ./gradlew i-router-register:bintrayUpload