User Interactions Demo
======================

NOTE - this example requires DataStax Enterprise(DSE) version > 4.6 and the cassandra-driver-core version > 2.1.X

## Scenario

This is short demo which shows how to capture user interactions. The idea is that users use a variety of different apps and we want to track every interaction that they have each of these applications. We can search, sort and filter by any of the attributes of the interaction. The main ones we will show will be based on event_type and date. 

e.g.

Get all interactions of type 'Login'

Get all interactions within the last 10 minutes. 

Get all interactions of type 'Login' within the last 10 minutes sorted in descending date order

Get all interactions where the clientid is 'mobilefx' and the browser agent contains 'OS' within the last day sorted in descending date order

DSE must be running in Search mode as we will be using DSE's Solr Integration to do the searching.

	http://docs.datastax.com/en/datastax_enterprise/4.8/datastax_enterprise/srch/srchInstall.html

## Schema Setup
Note : This will drop the keyspace "datastax_user_interactions_demo" and create a new one. All existing data will be lost. The keyspace is set up to only use one data center.  

The schema can be found in src/main/resources/cql/

Once the schema is create, create the Solr core for it using dsetool.

	dsetool create_core datastax_user_interactions_demo.user_interaction generateResources=true

This project is setup to use Spring Data Cassandra 
	
	http://docs.spring.io/spring-data/cassandra/docs/1.3.1.RELEASE/reference/html/#get-started.up-to-date

To specify contact points use the contactPoints command line parameter e.g. '-DcontactPoints=192.168.25.100,192.168.25.101'
The contact points can take mulitple points in the IP,IP,IP (no spaces).

To create the a single node cluster with replication factor of 1 for standard localhost setup, run the following

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaSetup"
    
To run the insert

	mvn exec:java -Dexec.mainClass="com.datastax.user.interactions.Main"
	
Now we can query data using the Solr Admin interface at 
http://localhost:8983/solr/#/datastax_user_interactions_demo.user_interaction/query
Or we can use the following cql in cqlsh

Get all interactions of type 'Login'
	
	SELECT * FROM user_interaction WHERE solr_query='{"q":"event_type:Login"}' limit 100;

Get all interactions within the 10 minutes

	SELECT * FROM user_interaction WHERE solr_query='{"q":"*:*", "fq":"date:[NOW-10MINUTE TO *]"}' limit 100; 

Get all interactions of type 'Login' within the last 10 minutes sorted in descending date order
	
	SELECT * FROM user_interaction WHERE solr_query='{"q":"*:*", "fq":"date:[NOW-10MINUTE TO *]", "sort":"date desc"}' limit 100;
	
Get all interactions where the clientid is 'mobilefx' and the browser agent contains 'OS' within the last day sorted in descending date order	
	
	SELECT * FROM user_interaction WHERE solr_query='{"q":"clientid:mobilefx", "fq":"date:[NOW-1DAY TO *]", "fq":"user_agent:*OS*", "sort":"date desc"}' limit 100;
	
To remove the tables and the schema, run the following.

    mvn exec:java -Dexec.mainClass="com.datastax.demo.SchemaTeardown"
	
