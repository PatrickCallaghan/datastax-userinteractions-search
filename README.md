Messages Demo
======================


To create the a single node cluster with replication factor of 1 for standard localhost setup, run the following

    mvn clean compile exec:java -Dexec.mainClass="com.datastax.demo.SchemaSetup"
    
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

	select * from datastax.session_paths where forward_path like '"F1 F2 F3"';
	
Find sessions that ended like 

	select * from datastax.session_paths where reverse_path like '"Logout F3"';
	
Get a distinct list of all the paths in each of the sessions

	select * from datastax.session_paths where solr_query='{"q":"*:*","useFieldCache":true,"facet":{"field":"forward_path"}}';
	
Proximity Search 

	select * from datastax.session_paths_global WHERE solr_query = '{"q":"path:\"A1 A3\"~2"}';
	
Find all customers who followed a workflow 

	select count(*) from datastax.session_paths_global WHERE solr_query = '{"q":"path:\"A1 A2 A3\""}';	
	
Find all customers who followed a workflow but haven't gone into a Branch

	select count(*) from datastax.session_paths_global WHERE solr_query = '{"q":"path:\"A1 A2 A3\" NOT path:BRANCH"}';

Find all sessions that have a certain event 

	select count(*) from datastax.session_paths where forward_path like '"A1"';
	
	select * from datastax.session_paths where forward_path like '"A1"';

Find all incomplete journeys

	select * from datastax.session_paths WHERE solr_query = '{"q":"forward_path:\"B2 B3\" NOT forward_path:\"B2 B3 B4\""}';
	
Find all incomplete journeys in the last day

	select * from datastax.session_paths WHERE solr_query = '{"q":"forward_path:\"B2 B3\" NOT forward_path:\"B2 B3 B4\"", "fq":"date:[NOW-1HOUR TO *]"}';
	
How many people when to a branch with 20 steps of selecting preferences

	select count(*) from datastax.session_paths_global WHERE solr_query = '{"q":"path:\"A1 B1\"~20"}';
	 

Using Curl we can find the distinct number of users 

	curl -d "q=*:*&rows=0&useFieldCache=true&json.facet={ x:'unique(userid)' }" http://localhost:8983/solr/datastax.user_interactions/select
	

		
		
		
		
		
		