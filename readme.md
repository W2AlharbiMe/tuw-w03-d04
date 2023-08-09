# Employees System with Validation

### Get All Employees
`GET /api/v1/employees/get`


### Add new employee
`POST /api/v1/employees/save` <br />

example of body:

```json
{
    "id": "1",
    "name": "AbdullahAlharbi",
    "age": 26,
    "position": "supervisor",
    "employmentYear": 2020,
    "annualLeave": 30
}

```

### update employee
`PUT /api/v1/employees/{id}/update`

<br />

example of body:

```json
{
    "id": "1",
    "name": "Abdullah Alharbi",
    "age": 26,
    "position": "supervisor",
    "employmentYear": 2020,
    "annualLeave": 30
}

```


### Delete employee
`DELETE /api/v1/employees/{id}/delete`


### set employee on leave
`PUT /api/v1/employees/{id}/set-on-leave` <br />

no need to send a body.


