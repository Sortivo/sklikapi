sklikapi
========

SKlik Java API

Java API for Seznam's project SKlik (www.sklik.cz). It allows simple manipulation with SKlik accounts.
The project is being developed, the current version is 0.5, that means most of the methods are not available, but they are easy to implement.

Example of use:

``` java
Client client = new Client();
client.login("username", "password");

Struct struct = new Struct();
struct.put("name", "blabla.");
struct.put("cpc", 20);

Group group = new Group(client);
group.create(4745, struct);

group.remove(778);


Campaign campaign = new Camapign(client);
// Response is a hash map
Response resp = campaign.listCampaigns();

```
