# Welcome to Scheduling-backend project

## I stopped developing this project because I am currently doing the course

Plans mentioned below are not final. In the future, I may adjust them according to my own considerations. But the main concept will not be changed.

### If u want to look at api that my code provides you can go through the instalation process described below
(Also I strongly advise looking at api notes that I left in this README.md file. They are below the definitions section).
### If you just want to look API that my project has it is not necessary to provide any config in application.yml but if you want to test sending email, or want a real database you need to provide configs described in application.yml
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

# Structure of schedule

![Без назви-1](https://user-images.githubusercontent.com/86126779/179581409-eb4ce5c9-6cf2-4cbc-b312-bb462393fca9.png)

# Definitions:
Inner planned stuff - is stuff that is involved in another planned stuff.\
Planned stuff holder - is stuff that holds inner planned stuff.

IPS - stands for inner planned stuff\
PSH - stands for planned stuff holder

PAC - stands for planned action container\
PA - stands for planned action\
PDC - stands for a planned day container\
PD - stands for a planned day\
SC - stands for schedule container\
S - stands for schedule

All (PAC, PA, PDC, PD, SC, S) are PS\

PS - stands for planned stuff

PS can be IPS and PSH at the same time(or one of them)

### List of relations: 
#### Template: PSH(IPS...IPS)
#### PA(PAC)
#### PD(PA, PDC)
#### S(PD, SC)

# API NOTES(6 notes):
1) If you want to add existing PA into PD you can send a request like this(you can add as many PA as you want):
```json
[
    {
        "id": 1,
        "plannedActions": [
            {
                "id": 1
            }
        ]
    }
]
```
2) If you want to add non-existing PA into PD can send a request like this(you can add as many PA as you want):
```json
[
    {
        "id":1,
        "name": "planned day 1",
        "plannedActions": [
            {
                "startsAt": "hh:mm:ss",
                "endsAt": "hh:mm:ss",
                "name": "name",
                "description": "description"
            }
        ]
    }
]
```
3) If you want to delete(when deleting PA from PD it will not delete PA from system) PA from PD you can use an example from note 1 and change PA id with its opposite(you can delete as many PA as you want).
```json
[
    {
        "id": 1,
        "plannedActions": [
            {
                "id": -1
            }
        ]
    }
]
```
#### First 3 notes can be appliad to PD and S pair as well
#### You can also add(existing or new) or delete(existing) PA from PD through S:
```json
[
    {
        "id": 1,
        "days": [
            {
                "id": 1,
                "plannedActions": [
                    {
                        "id": -1
                    },
                    {
                        "id": 2
                    },
                    {
                        "startsAt": "hh:mm:ss",
                        "endsAt": "hh:mm:ss",
                        "name": "name",
                        "description": "description"
                    }
                ]
            }
        ]
    }
]
```
4) If you want to update IPS you can do it in many ways.
  - #### Call the update() method on the IPS service.
  - #### While saving PSH you can specify IPS id and set new values. Logic is built in the way that new values will be applied to IPS with the specified id.
  - #### While updating PSH you can specify IPS id and set new values. Logic is built in the way that new values will be applied to IPS with the specified id.
##### This can be done even if the logical tree is like: PSH->PSH-PSH->IPS.
##### For example, if u want to change the PAC through the S.
##### The logical tree will look like: S->PD->PA->PAC. Where "->" means delegation
##### Surely the logical tree can be entered at any point but must follow the direction of the arrows:
![image](https://user-images.githubusercontent.com/86126779/179602438-b13b99a0-1750-4cfe-aeaa-452c69a10d4b.png)

#### Notes 5 and 6 are applicable for all(PAC, PA, PDC, PD, SC, S);
5) If you are saving PSH(or IPS) you can not specify its id if you do, an exception will be thrown. At the same time, (only if the object is of PSH type as well), if PSH contains IPS you can write the id of IPS(which means that you want to update the existing IPS and if IPS is not in PSH it will be then added), if you don't specify the id of IPS this means that you want to create new IPS and add it to this PSH.\
You can do this in the same logical tree as in note 4(all entering points are supported as well).
### Examples:
- First (saving PA valid): 
```json
[
    {
        "startsAt": "12:50:00",
        "endsAt": "21:50:00",
        "name":"test name 2",
        "plannedActionContainer": {
            "id": 2
        }
    }
]
```
- Second (saving PA invalid): 
```json
[
    {
        "id": 1,
        "startsAt": "12:50:00",
        "endsAt": "21:50:00",
        "name":"test name 2",
        "plannedActionContainer": {
            "id": 2
        }
    }
]
```
- Third (saving S valid):
```json
[
    {
        "name":"schedule 1",
        "days":[
            {
                "name":"day 1",
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "id": 2
                        }
                    }
                ]
            },
            {
                "id": 1,
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "name":"action container 1",
                            "bgColor":"red"
                        }
                    }
                ]
            }
        ]
    }
]
```
- Fourth (saving S invalid):
```json
[
    {
        "id": 1,
        "name":"schedule 1",
        "days":[
            {
                "name":"day 1",
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "id": 2
                        }
                    }
                ]
            },
            {
                "id": 1,
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "name":"action container 1",
                            "bgColor":"red"
                        }
                    }
                ]
            }
        ]
    }
]
```
6) If you are updating PSH(or IPS) you must specidy its id:
### Examples
- First (updating S valid):
```json
[
    {
        "id": 1,
        "name":"schedule 1",
        "days":[
            {
                "name":"day 1",
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "id": 2
                        }
                    }
                ]
            },
            {
                "id": 1,
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "name":"action container 1",
                            "bgColor":"red"
                        }
                    }
                ]
            }
        ]
    }
]
```
- Second (updating S invalid)
```json
[
    {
        "name":"schedule 1",
        "days":[
            {
                "name":"day 1",
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "id": 2
                        }
                    }
                ]
            },
            {
                "id": 1,
                "plannedActions":[
                    {
                        "startsAt":"18:55",
                        "endsAt":"19:50",
                        "name":"action 1",
                        "plannedActionContainer": {
                            "name":"action container 1",
                            "bgColor":"red"
                        }
                    }
                ]
            }
        ]
    }
]
```
