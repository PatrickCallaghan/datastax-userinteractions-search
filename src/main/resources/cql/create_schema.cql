create keyspace if not exists datastax_demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1' };

use datastax_demo;

create table if not exists messages (
	sender text,
	sendee text, 
	date timestamp,
	msg  text,
	PRIMARY KEY ((sender, sendee), date)
) with clustering order by (date desc);


truncate datastax_demo;
