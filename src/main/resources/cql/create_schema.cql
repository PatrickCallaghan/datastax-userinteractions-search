create keyspace if not exists datastax WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };
 
use datastax;

create table if not exists user_interactions(
id uuid,
clientid text,
correlationid text,
date timestamp,
details text,
event_type text,
user_agent text,
user_agent_filterd text,
userid text,
reference text,
PRIMARY KEY (id)
);

create search index on user_interactions;

truncate user_interactions;