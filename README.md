# Welcome to Scheduling-backend project
  
Plans mentioned below are not final. In the future, I may adjust them according to my own considerations. But the main concept will not be changed.

### If u want to look at api that my code provides you can go through the instalation process described below.
### Then run the project with property registration.send-email: false. 
### Then enter this url: http://localhost:8080/swagger-ui/index.html
#### The username is: test
#### The password is: 1

# Instalation process
1. ### Clone this repository
2. ### Install all the dependencies using maven
3. ### Provide configuration in .yml files
4. ### Run the project

## Plans
- [x] Registration api
  - [ ] User CRUD (creation is done all other will be implemented duaring implementation of global roles)
  - [x] Validating the client`s entered data
  - [x] Custom Authentication provider
  - [x] Email confirmation
  - [x] Custom exceptions with http codes
- [x] Scheduling api
  - [x] Planned Action Container CRUD
  - [x] Validating the client`s entered data
  - [x] Planned Action CRUD
  - [x] Validating the client`s entered data
  - [x] Planned Day Container CRUD
  - [x] Validating the client`s entered data
  - [x] Planned Day CRUD
  - [x] Validating the client`s entered data
  - [x] Schedule Container CRUD
  - [x] Validating the client`s entered data
  - [x] Schedule CRUD
  - [x] Validating the client`s entered data
  - [x] Attaching User Account to every planned stuff that is created
  - [x] Custom exceptions with HTTP codes
- [x] Writing tests
  - [x] PlannedActionContainerService
  - [x] PlannedActionService
  - [x] PlannedDayContainerService
  - [x] PlannedDayService
  - [x] ScheduleContainerService
  - [x] ScheduleService
- [x] Trying to make code better
- [ ] Implementing members feature
- [ ] Validating the client`s entered data
- [ ] Implementing additional scurity
  - [ ] Every user can acces only its entities
  - [ ] Every user can acsess its entities and entities in which he is member
  - [ ] Implementing roles system for membership in schedule
- [ ] Writing tests for all untested things mentioned above
- [ ] Trying to make code better
- [ ] Implementing global roles system
- [ ] Writing tests
- [ ] Trying to make code better
![Без назви-1](https://user-images.githubusercontent.com/86126779/179522742-5ea4f6e0-ee7a-46e6-b98a-d289afba3bdd.png)

