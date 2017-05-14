# FavorDrop - REST API
Live version: `http://52.213.91.0:8080/FavorDrop_war/`

Live version without authentication: `http://52.213.91.0:8080/FavorDrop_war_no_auth/`

For at teste API’et kan Postman benyttes.
Da vi benytter JWT gennem Firebase til authentication, skal man først hente dette ud fra `Session Storage`, eller teste mod API'et uden authentication.

# Setting up for local deployment
## Requirements
IDE: IntelliJ by Netbrains.

## Import the project 
Clone the repository and import to the IDE.

## Setup Application Server
GlassFish is required to run the webservice.

## Serving the API
Run `REST.java`
