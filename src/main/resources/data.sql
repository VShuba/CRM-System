-- Create user. Password(10 rounds): my_1secret1_password
INSERT INTO users (email, login, password, role)
VALUES ('test@gmail.com', 'test@gmail.com',
        '$2a$10$vIm9sed1.P4N7dTL.d4SZer2HQYj4Yjlkttb/mOEYhrS4LRfQ5viq', 'ADMIN');

-- Create organization
INSERT INTO organizations (name)
VALUES ('Test Organization');

-- Create branch (location)
INSERT INTO branches (name, organization_id)
VALUES ('Main Branch', 1);

-- Create rooms
INSERT INTO rooms (name, branch_id)
VALUES ('Room A', 1),
       ('Room B', 1);

-- Create services
INSERT INTO services (service_name, service_color, branch_id)
VALUES ('Haircut', '#FF5733', 1),
       ('Massage', '#33FF57', 1);
