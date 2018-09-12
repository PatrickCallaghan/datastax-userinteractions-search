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
resverse_path text,
PRIMARY KEY (correlationid, date)
);

create search index on user_interactions;

truncate user_interactions;

