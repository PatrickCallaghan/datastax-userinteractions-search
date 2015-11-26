create keyspace if not exists datastax_user_interactions_demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };

use datastax_user_interactions_demo;

create table if not exists user_interaction(
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


truncate user_interaction;
