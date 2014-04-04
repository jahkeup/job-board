INSERT INTO employees (id, first_name, last_name, active) VALUES
       (65, 'Mike', 'Spanky', True),
       (90, 'Joan', 'Miceal', False),
       (48, 'Denis', 'Kile', True),
       (32, 'Mark', 'Staples', True);

INSERT INTO jobsites (id, name, address, city, state) VALUES
       (44, 'Joes Bar and Grill', '21 West 8th', 'Renalo', 'KS'),
       (89, 'Uganda Inn', '22 Riwanda Ln', 'Rwanda', 'SA'),
       (81, 'Hanover Hangovers', '99 Whitey Bulger Ave', 'Boston', 'MA');

INSERT INTO employees_jobsites (employee_id, jobsite_id) VALUES
       (65, 44),
       (90, 89),
       (48, 81);
