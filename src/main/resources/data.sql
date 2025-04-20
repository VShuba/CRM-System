-- Create user. Password(10 rounds): my_1secret1_password
INSERT INTO users (id, email, login, password, role)
VALUES (1, 'test@gmail.com', 'test@gmail.com',
        '$2a$10$vIm9sed1.P4N7dTL.d4SZer2HQYj4Yjlkttb/mOEYhrS4LRfQ5viq', 'ADMIN');

-- Create organization
INSERT INTO organizations (id, name)
VALUES (1, 'Test Organization');

-- Create branch (location)
INSERT INTO branches (id, name, organization_id)
VALUES (1, 'Main Branch', 1);

-- Create rooms
INSERT INTO rooms (id, name, branch_id)
VALUES (1, 'Room A', 1),
       (2, 'Room B', 1);

-- Create services
INSERT INTO services (id, service_name, service_color, branch_id)
VALUES (1, 'Haircut', '#FF5733', 1),
       (2, 'Massage', '#33FF57', 1);
