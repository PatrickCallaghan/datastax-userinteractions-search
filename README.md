Messages Demo
======================


To create the a single node cluster with replication factor of 1 for standard localhost setup, run the following

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaSetup"
    
Once the schema is created, create the Solr core for it using dsetool.

	dsetool create_core datastax_user_interactions_demo.user_interaction generateResources=true    
    
To run the insert

	mvn exec:java -Dexec.mainClass="com.datastax.user.interactions.Main"
	
The default runs 10,000 distinct visits (login,event*,logout). To change this use -DnoOfVisits=   eg
	
	mvn exec:java -Dexec.mainClass="com.datastax.user.interactions.Main" -DnoOfVisits=30000
	
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
	
For Spark, run the following in a spark shell to find counts of different attributes

	case class UserInteraction (id: java.util.UUID, clientid: String, correlationid: String, date: java.util.Date, details: String, event_type: String, user_agent: String, user_agent_filterd: Option[String], userid: String, reference: String )

	val table = sc.cassandraTable[UserInteraction]("datastax_user_interactions_demo", "user_interaction").cache;

	table.count

	val googleUsers = table.filter(f => f.user_agent.toLowerCase().contains("google"));
	googleUsers.count

	val logins = table.filter(f => f.event_type.toLowerCase().contains("login"));
	logins.count	

	val loginGoogle = googleUsers.filter(f => f.event_type.toLowerCase().contains("login"));
	loginGoogle.count

	val results = csc.sql("SELECT * from datastax_user_interactions_demo.user_interaction");

New Queries based on session path 

Find sessions that started like 

	select * from datastax.session_paths where forward_path like 'Login-news-balance-preferences%';
	
Find sessions that ended like 

	select * from datastax.session_paths where reverse_path like 'Logout-news-balance-preferences%';
	
Get a distinct list of all the paths in each of the sessions

	select * from datastax.session_paths where solr_query='{"q":"*:*","useFieldCache":true,"facet":{"field":"forward_path"}}';
	

Using Curl we can find the distinct number of users 

	curl -d "q=*:*&rows=0&useFieldCache=true&json.facet={ x:'unique(userid)' }" http://localhost:8983/solr/datastax.user_interactions/select
	
		

