This project was the backend part of a punch-in system that was designed to be similar to other competitors in the hr field.

It had features for the main agents in a company that would involve time controls, such as the employee, 
managers and administrators.

The employee could punch-in as many times in the day as he wanted so long as it was an even number of punches.
The manager could approve or disapprove punch alteration requests and could visualize the employee's punch mirror.
The administrators could add and remove employees and managers as needed.

The database was modeled as a simple 1-n relationship between most agents, in the end I settled on this format:

Company 1-n Adminstrators 1-n Managers 1-n Employees 1-n Punches

It felt natural to model the database in this way and the queries were relatively simple in the end so I think it was a good choice.

The endpoints are what you would expect from a punch-in system, feel free to browse the files that end with .*Controller.java to see them.
Obviously a CRUD of most agents and also an endpoint for report generation.

If you have any interest in this project feel free to contact me @ matheusrotta7@gmail.com
