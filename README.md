sklikapi
========

SKlik Java API

Java API for Seznam's project SKlik (www.sklik.cz). It allows simple manipulation with SKlik accounts.
The project is being developed, the current version is 0.5, that means most of the methods are not available, but they are easy to implement.

Example of use:

``` java
Client client = new Client();
client.login("username", "password");

Group group = new Group();
group.setName("group456");
group.setCpc(1500);

GroupDAO groupDAO = new GroupDAO(client);
group.create(4745, group);

group.remove(778);


CampaignDAO campaignDAO = new CamapignDAO(client);
List<Campaign> campaigns = campaignDAO.listCampaigns();

```
