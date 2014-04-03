# Job Board

This is the Meade Job Board system that is used for scheduling
employees on jobsites. This application takes advantage of the
[Exzigo API](http://exzigo.com) to provide employees and
jobsites. This application in turn caches that data and allows
scheduling based on it.

## Features

Job Board in its current vision will provide 3 main views:

1. Scheduling View

   A scheduling oriented view that offers little in the 'at a glance'
   category of things.

2. Overall View

   This view exposes the schedule for the company and any notices or
   restrictions for a jobsite.

3. Bulletin View
   
   This view rotates through the jobsites that currently scheduled for
   and displays them in a 'at-a-glance' manner.

### Scheduling View

The scheduling view will only be accessible by management officers
that are privy to the PIN necessary for modifying the assignments. At
the start, there will only be a single PIN and no other
users/usernames/emails necessary for accessing the site. In the
future, this may change.

The scheduler will provide a page with all active employees listed
along with their assigned jobsites. Next to their names will be a
checkbox that will allow them to be bulk assigned to a jobsite via a
dropdown and submit button. Simple.

There will eventually be an option to group by currently assigned
jobsites, though for now, it will simply be ordered by employee last
name.

### Overview

This view, similar to the scheduling view, will only allow the viewing
of all jobsites and their assignees. This will be a simple grid setup
where all jobsites will be a grid element with jobsite information and
assignees listed. This grid will be responsive and will flow to fit
large screens.

### Bulletin View

This view will provide a "scrolling marquee" of the day's assigned
jobsites and the assignees.

Essentially this will be a larger display targeted page with a
highlight of the following details of assignment:

- Jobsite Name
- Jobsite Notices
- Assigned Employees


This view will eventually include jobsite oriented information such
as weather and any notices from management regarding the particular
jobsite.


## Program Deployment and Choices

This program is written in [Clojure](http://clojure.org) and its build
tool [Leiningen](http://leiningen.org/) will take care of any and all
dependencies. Clojure is a dynamically typed lisp based on the JVM and
brings all of the best of Java to the table. I wrote this program in
Clojure because I can, if you care to ask why, ask away; but know that
I stand by this decision. With each release, an 'uberjar' will be
compiled and uploaded to the releases section that can be run from any
computer that has a JRE without any dependencies or other files
necessary.

The production and development database is PostgreSQL. There are/will
be migrations and setup in a separate folder following a final
decision on a DB Migration library.


