-- You can use this file to load seed data into the database using SQL statements
insert into Member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')
insert into Member (id, name, email, phone_number) values (1, 'Bob Smith', 'bob.smith@mailinator.com', '2125551232')

insert into Candidate (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.com', '2125551212')
insert into Candidate (id, name, email, phone_number) values (1, 'Bob Smith', 'bob.smith@mailinator.com', '2125551232')

insert into MasterQuestion (id, question) values (0, 'What is your favorite color?')