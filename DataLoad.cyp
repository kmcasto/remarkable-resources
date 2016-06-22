//set up for the database. the following entries must be entered one at a time

//drop the database
MATCH (n) detach delete  n;

//load in nodes via email addresses
LOAD CSV WITH HEADERS FROM 'file:///people.csv' AS line
CREATE (:Person {email:line.Email, name:line.Name});

//generate edges by matching emails against senders/recievers
LOAD CSV WITH HEADERS FROM 'file:///emailgen.csv' AS line
MATCH (p1:Person {email:line.SenderEmail})
MATCH (p2:Person {email:line.ReceiverEmail})
MERGE (p1)-[edge:Emailed {subject:line.Subject}]->(p2);

//find experts in java
//this finds all people who emailed the subject java then orders them by
//most sent/recieved and then takes the top five
match (p1:Person )-[s1:Emailed {subject:"java"}]-(p2:Person)
with p1, count(p1) as rels
order by rels desc
limit 5
return p1.name as Name, p1.email as Email, rels as Hits, ID(p1) as ID

//a quick query to display the email address "lo@ibu.il" and everyone they have emailed
//this one is limited to 10 nodes
match (p1:Person {email:"lo@ibu.il"})-[e]-(p2:Person)
return distinct(e.subject), count(e) as count
order by count desc
limit 10




//everything below is useless (kinda)
//the following queries build a graph with subjects as nodes and edges with "sent to's"

MATCH (n) detach delete n;

LOAD CSV WITH HEADERS FROM 'file:///people.csv' AS line
CREATE (:Person {email:line.Email});

LOAD CSV WITH HEADERS FROM 'file:///keywords.csv' AS line
CREATE (:Subject {subject:line.Subject});

LOAD CSV WITH HEADERS FROM 'file:///emailgen.csv' AS line
MATCH (p1:Person {email:line.SenderEmail})
MATCH (sub1:Subject {subject:line.Subject})
MERGE (p1)-[edge:Sent {receiver:line.ReceiverEmail}]->(sub1);

LOAD CSV WITH HEADERS FROM 'file:///emailgen.csv' AS line
MATCH (sub1:Subject {subject:line.Subject})
MATCH (p1:Person {email:line.ReceiverEmail})
MERGE (sub1)-[edge:Received {receiver:line.SenderEmail}]->(p1);


//testing:

match (p1:Person), (s1:Subject {subject:"java"})
where (p1)-[:Sent]->(s1)
OR (s1)-[:Received]->(p1)
with p1, count(s1) as rels, collect(s1) as subjects
where rels > 1
return p1, rels, subjects