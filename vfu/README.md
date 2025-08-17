# Vale Food

## Project

The project was split in a few repositories, the [User Management](https://github.com/edilsonjustiniano/vfu)
, [Restaurant Management](https://github.com/edilsonjustiniano/vfr) and [Authenticator](https://github.com/edilsonjustiniano/vfa).

## Overview 

This repository contains the solution implemented by the DM111 discipline from the Pos Graduation course Mobile and 
Cloud Development by Inatel in 2025.

During the classes was implemented a web service to deal with a minimal structure of a food delivery app, i.e. The _CRUD_ 
operations for **Users**, **Restaurants** _(Client and Restaurants)_.

To make these exposed APIs secure it was implemented an **Authentication** API as well using JWT token. 

In addition to the auth API an _Interceptor_ was created and all the endpoints that required the authorization was 
added behind this interceptor. It means, only authenticated users could access the resources and in some scenarios only
authenticated and authorized users were able to perform some operations.

## Integrations

The current project does have the integrations with: _Firebase DB_, _Pub Sub_.

### Firebase

In order to reduce the cost and also reduce the risk of reach the Free tier limit of U$ 300 provided by the Google Cloud
I have decided to use Firebase as our main database solution. That decision helped us to reduce the number of resources
required to be deployed and simplify our deployment letting us move our focus on the code itself.

#### Remember
_You have to generate the service accounts from your Firebase project and paste into the right place, i.e.the folder_
_src/main/resources_

The following entities are stored on the Firebase:

> User
```json
{
  "email": "email@email.com.br",
  "id": "uuid",
  "name": "name",
  "password": "encrypted",
  "type": "REGULAR|RESTAURANT"
}
```

> Restaurant
```json
{
  "id": "uuid",
  "name": "name",
  "desciption": 0,
  "categories": [],
  "products": [
    {
      "name": "",
      "price": 0,
      "category": ""
    }
  ]
}
```


### Pub Sub

One more integration added to the application was the **Pub Sub** that is composed by the topic and their subscriptions.
Whenever a supermarket list has been created, updated or either deleted an event is published through this topic and the
consumer services will be aware of the change. So, the consumer applications can apply their specific logics.


## How to deploy to GAE

To deploy the code to **GAE** (_Google App Engine_), it is necessary first initialize the **gcloud** environment, please run
the following command:

```
gcloud init
```

During the execution of this command, it will be necessary to authenticate at the **Google Cloud** account. After the 
initialization has been completed the deployment will be straightforward you only need to execute the following command.

```
gcloud app deploy --version 1 dm111/app.yaml
```

Some data/confirmation will be requested via terminal at the end of this step and as result of the deploy the link to access
the service via API will be there.

### Deploy multiple services at once

To deploy both services simultaneously, please run the following command:

```
gcloud app deploy --version 1 vale-food-user-management/app.yaml vale-food-restaurant-management/app.yaml
vale-food-auth/app.yaml
```
