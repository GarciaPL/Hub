## Hub

Application has been created for technical test purpose during interview process.

Application reads inbound API called /partners which contains list of employeers available for meetings with specified dates and location. Every person is described by name, surname and email as below.

```json
{
	"firstName": "Name",
	"lastName": "Surname",
	"email": "someone1@email.com",
	"country": "Ireland",
	"availableDates": [
		"2017-05-03",
		"2017-05-06"
	]
}
```
The main purpose of application is to find an employees from every country who are available for meeting within two days in the row. We should send results up to outbound API taking first into consideration dates where ther most attendes can setup a meeting. If it's not possible to find such a combination for meetings within one country, we will send attendeCount as 0, empty list of attendees and null startDate.

Results of our searches should be send up to outbound API called **/countries**. Example of output might be found below.

```json
{
  "countries": [
    {
      "attendeeCount": 1,
      "attendees": [
        "test1@email.com"
      ],
      "name": "Ireland",
      "startDate": "2017-05-02"
    },
    {
      "attendeeCount": 0,
      "attendees": [],
      "name": "Poland",
      "startDate": null
    },
    {
      "attendeeCount": 3,
      "attendees": [
        "test2@email.com",
        "test3@email.com",
        "test4@email.com"
      ],
      "name": "Germany",
      "startDate": "2017-05-03"
    }
  ]
}

```

Logic is invoked by hitting endpoint **/start**

## Technology

#### Stack

- Java - 1.8.0_121
- Spring Boot - 1.5.1.RELEASE

#### Libraries

- Guava - 21.0
- Jackson DataType Joda - 2.8.7

## License
Code released under the  Apache License 2.0. Docs released under Creative Commons.

[![ghit.me](https://ghit.me/badge.svg?repo=GarciaPL/Hub)](https://ghit.me/repo/GarciaPL/Hub)