-- Create the default schema for Job-board


-- Cached employees
CREATE TABLE employees (
       id INTEGER UNIQUE NOT NULL,
       first_name VARCHAR(255) NOT NULL,
       last_name VARCHAR(255) NOT NULL,
       active BOOLEAN DEFAULT TRUE,
       PRIMARY KEY (id)
);


-- Cached jobsites
CREATE TABLE jobsites (
       id INTEGER UNIQUE NOT NULL,
       name VARCHAR(255) NOT NULL,
       address VARCHAR(255) NOT NULL,
       city VARCHAR(255) NOT NULL,
       state CHAR(2) NOT NULL,
       PRIMARY KEY (id)
);

-- Employee assignments
CREATE TABLE employees_jobsites (
       employee_id INTEGER NOT NULL UNIQUE,
       jobsite_id INTEGER NOT NULL,
       PRIMARY KEY (employee_id, jobsite_id),
       FOREIGN KEY (employee_id) REFERENCES employees(id),
       FOREIGN KEY (jobsite_id) REFERENCES jobsites(id)
);
