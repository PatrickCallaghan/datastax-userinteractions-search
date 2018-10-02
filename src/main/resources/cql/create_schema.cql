create keyspace if not exists datastax WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };
 
use datastax;

create table if not exists user_interactions(
correlationid text,
userid text,
date timestamp,
id uuid,
details text,
event_type text,
user_agent text,
user_agent_filterd text,
clientid text,
reference text,
PRIMARY KEY (userid, correlationid, date)
);

create table if not exists session_paths(
correlationid text,
userid text,
date timestamp,
forward_path text,
reverse_path text,
PRIMARY KEY (correlationid, date)
);

create table if not exists session_paths_global(
userid text,
updated timestamp,
path text,
PRIMARY KEY (userid)
);


create search index on user_interactions;
create search index on session_paths;
create search index on session_paths_global;


ALTER SEARCH INDEX SCHEMA ON datastax.user_interactions ADD types.fieldtype[@class='org.apache.solr.schema.TextField', @name='MyTextField'] WITH '{"analyzer":{"tokenizer":{"class":"solr.StandardFilterFactory"}, "filter":[{"class": "solr.LowerCaseFilterFactory"}]}}';
ALTER SEARCH INDEX SCHEMA ON datastax.session_paths ADD types.fieldtype[@class='org.apache.solr.schema.TextField', @name='MyTextField'] WITH '{"analyzer":{"tokenizer":{"class":"solr.StandardFilterFactory"}, "filter":[{"class": "solr.LowerCaseFilterFactory"}]}}';
ALTER SEARCH INDEX SCHEMA ON datastax.session_paths_global ADD types.fieldtype[@class='org.apache.solr.schema.TextField', @name='MyTextField'] WITH '{"analyzer":{"tokenizer":{"class":"solr.StandardFilterFactory"}, "filter":[{"class": "solr.LowerCaseFilterFactory"}]}}';

ALTER SEARCH INDEX SCHEMA ON datastax.user_interactions SET field[@name='content']@type='MyTextField';

ALTER SEARCH INDEX SCHEMA ON datastax.session_paths SET field[@name='forward_path']@type='MyTextField';
ALTER SEARCH INDEX SCHEMA ON datastax.session_paths SET field[@name='reverse_path']@type='MyTextField';
ALTER SEARCH INDEX SCHEMA ON datastax.session_paths_global SET field[@name='path']@type='MyTextField';

truncate user_interactions;

