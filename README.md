# Slick Integration Example/Experiment

A simple attempt at incorporating Slick into a Play project, with some basic caching.  The application makes available the names and ages of people it has stored in its database.

Use the following command to run locally:
```$xslt
sbt run
```

The application will start up (with an H2 database instance in the background).  The following endpoints should now be available for GET requests:
- localhost:9000/persons
- localhost:9000/persons/{id}

The first endpoint should return the data for all the people currently in its cache.  The second endpoint will return the data for person with the given (integer) id, if such a person exists.

The database starts off empty.  You can add people to the database by POSTing JSON of the form
```$xslt
{
  "name": <String>,
  "age": <Integer>
}
```
to the
- localhost:9000/backdoor

endpoint

Note that the application will refresh its cache of people every 30 seconds, so people added through this endpoint might not be immediately visible to the other endpoints.