# Job Board

This is a Job Board system that is used for assigning
employees to jobsites. This application takes advantage of the
[Exzigo API](http://exzigo.com) to provide employees and
jobsites. This application in turn caches that data and allows
assignments based on it.

## Features

Job Board in its current vision will provide 3 main views:

1. Assignment View

   A assignment oriented view that offers little in the 'at a glance'
   category of things.

2. Overall View

   This view exposes the schedule for the company and any notices or
   restrictions for a jobsite.

3. Slide View
   
   This view rotates through the jobsites that currently scheduled for
   and displays them in a 'at-a-glance' manner.

### Assignment View

The assignment view will only be accessible by management officers
that are privy to the PIN necessary for modifying the assignments. At
the start, there will only be a single PIN and no other
users/usernames/emails necessary for accessing the site. In the
future, this may change.

The assigner will provide a page with all active employees listed
along with their assigned jobsites. Next to their names will be a
checkbox that will allow them to be bulk assigned to a jobsite via a
dropdown and submit button. Simple.

There will eventually be an option to group by currently assigned
jobsites, though for now, it will simply be ordered by employee last
name (just kidding, its by id).

### Overview (not implemented yet)

This view, similar to the assignment view, will only allow the viewing
of all jobsites and their assignees. This will be a simple grid setup
where all jobsites will be a grid element with jobsite information and
assignees listed. This grid will be responsive and will flow to fit
large screens.

### Slide View

This view will provide a "rolling marquee" of the day's assigned
jobsites and the assignees.

Essentially this will be a larger display targeted page with a
highlight of the following details of assignment:

- Jobsite Name
- Jobsite Notices (don't have specs for this yet)
- Assigned Employees


This view will eventually include jobsite oriented information such
as weather and any notices from management regarding the particular
jobsite.

## Starting and Configuring


### Configuration options

```

These are set at exec time with -D<name>=<val>

auth.pin - Authentication pin used for login

db.database - The database to use, must be prepared manually.
db.username - user to connect with
db.password - password to use

The hostname and port isn't settable right now, it should be using a
local instance of postgresql.

slide.refresh - Number of times around before the database is checked
for new entries.
slide.time - Seconds in which to keep the slide on screen for

exzigo.username - User to fetch from Exzigo API (email)
exzigo.password - password ..
exzigo.poll.interval - Time between fetches (default 60 minutes)

^^ those are the system properties but you can just use env variables:


export AUTH_PIN="9999"

export WEBSERVER_PORT=80

export DB_DATABASE="jobboard"
export DB_USERNAME="jobboard"
export DB_PASSWORD="jobboard"

export SLIDE_REFRESH=1
export SLIDE_TIME=10

export EXZIGO_USERNAME="email@address.com"
export EXZIGO_PASSWORD="pass1234"

```

### Running

Bam: `java -jar job-board.jar -D...` or `java -jar job-board.jar #
with exported variables`.
