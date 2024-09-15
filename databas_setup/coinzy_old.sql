-- Create the database and use it
CREATE DATABASE coinzy_db;
USE coinzy_db;

-- Create the roles table
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE
);

-- Insert default roles (Admin, Member)
INSERT INTO roles (role_name) VALUES ('Admin'), ('Member');

-- Create the users table with ON DELETE CASCADE
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role_id INT,
    status ENUM('pending', 'approved', 'rejected', 'blocked') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL
);

-- Create the accounts table with ON DELETE CASCADE
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_type VARCHAR(50),
    balance DECIMAL(10, 2),
    liabilities DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the income_sources table with ON DELETE CASCADE
CREATE TABLE income_sources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    source_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the incomes table with ON DELETE CASCADE
CREATE TABLE incomes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_id INT,
    income_date DATE,
    income_source VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- Create the expense_categories table with ON DELETE CASCADE
CREATE TABLE expense_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    category_name VARCHAR(100) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the expenses table with ON DELETE CASCADE
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_id INT,
    expense_date DATE,
    expense_category VARCHAR(100),
    remark VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- Create the transactions table with ON DELETE CASCADE
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    type VARCHAR(10),
    amount DECIMAL(10,2),
    statement VARCHAR(255),
    time TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

-- Create the budgets table with ON DELETE CASCADE
CREATE TABLE budgets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    expense_category VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the target_amounts table with ON DELETE CASCADE
CREATE TABLE target_amounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the announcements table
CREATE TABLE announcements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    message TEXT,
    created_by INT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL
);

-- Create triggers
DELIMITER //

-- Trigger for deleting related records when an account is deleted
CREATE TRIGGER beforeAccountDelete
BEFORE DELETE ON accounts
FOR EACH ROW
BEGIN
    IF OLD.id IS NOT NULL THEN
        DELETE FROM expenses WHERE account_id = OLD.id;
        DELETE FROM incomes WHERE account_id = OLD.id;
        DELETE FROM transactions WHERE account_id = OLD.id;
    END IF;
END;
//

-- Trigger for inserting a transaction after an income record is inserted
CREATE TRIGGER afterIncomeInsert
AFTER INSERT ON incomes
FOR EACH ROW
BEGIN
    INSERT INTO transactions(account_id, type, amount, statement, time)
    VALUES (NEW.account_id, 'Income', NEW.amount, CONCAT('Income recorded: ', NEW.income_source, ' - Amount: ', NEW.amount), CURRENT_TIMESTAMP);
END;
//

-- Trigger for inserting a transaction after an expense record is inserted
CREATE TRIGGER afterExpenseInsert
AFTER INSERT ON expenses
FOR EACH ROW
BEGIN
    INSERT INTO transactions(account_id, type, amount, statement, time)
    VALUES (NEW.account_id, 'Expense', NEW.amount, CONCAT('Expense recorded: ', NEW.expense_category, ' - Amount: ', NEW.amount), CURRENT_TIMESTAMP);
END;
//

DELIMITER ;
-- Ensure bcrypt is used to hash the password for 'huzk'
-- Seed initial users with different statuses and 'created_at' dates

-- Update user with id 1
UPDATE users 
SET username = 'huzk', 
    password = '$2a$10$wIjKl5Krw.qjVu6F/2uy9O2IXyOh5eYl0wM6Rq.p8vCQX3/rUL.s2' -- bcrypt hash for 'p@ssword'
WHERE id = 1;

-- Seed additional users with random dates and statuses
DELIMITER //
CREATE PROCEDURE seed_more_users()
BEGIN
    DECLARE i INT DEFAULT 2;
    DECLARE status VARCHAR(10);
    WHILE i <= 50 DO
        -- Assign random statuses
        SET status = CASE 
            WHEN i % 4 = 0 THEN 'approved' 
            WHEN i % 4 = 1 THEN 'pending' 
            WHEN i % 4 = 2 THEN 'blocked' 
            ELSE 'rejected' 
        END;
        
        -- Insert user with hashed password
        INSERT INTO users (name, email, username, password, role_id, status, created_at)
        VALUES (
            CONCAT('User', i),
            CONCAT('user', i, '@example.com'),
            CONCAT('user', i),
            '$2a$10$wIjKl5Krw.qjVu6F/2uy9O2IXyOh5eYl0wM6Rq.p8vCQX3/rUL.s2', -- bcrypt hash of 'password123'
            2,
            status,
            DATE_ADD('2024-01-01', INTERVAL FLOOR(RAND() * 365) DAY)
        );
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

-- Call the procedure to populate with 50 users
CALL seed_more_users();


-- Insert at least 10 records into accounts table
INSERT INTO accounts (user_id, account_type, balance, liabilities) 
VALUES 
(1, 'Savings', 1000.00, 200.00),
(2, 'Savings', 500.00, 150.00),
(3, 'Current', 300.00, 100.00),
(4, 'Savings', 400.00, 50.00),
(5, 'Current', 200.00, 300.00),
(6, 'Savings', 800.00, 120.00),
(7, 'Savings', 100.00, 10.00),
(8, 'Current', 900.00, 400.00),
(9, 'Savings', 600.00, 100.00),
(10, 'Current', 700.00, 250.00);

-- Insert at least 10 records into income_sources table
INSERT INTO income_sources (user_id, source_name) 
VALUES 
(1, 'Salary'),
(2, 'Freelancing'),
(3, 'Investment'),
(4, 'Rental'),
(5, 'Part-time Job'),
(6, 'Dividends'),
(7, 'Consulting'),
(8, 'Business'),
(9, 'Grants'),
(10, 'Pension');

-- Insert at least 10 records into incomes table
INSERT INTO incomes (user_id, account_id, income_date, income_source, amount) 
VALUES 
(1, 1, '2024-01-10', 'Salary', 1000.00),
(2, 2, '2024-02-12', 'Freelancing', 500.00),
(3, 3, '2024-03-15', 'Investment', 300.00),
(4, 4, '2024-04-18', 'Rental', 400.00),
(5, 5, '2024-05-22', 'Part-time Job', 200.00),
(6, 6, '2024-06-25', 'Dividends', 800.00),
(7, 7, '2024-07-28', 'Consulting', 100.00),
(8, 8, '2024-08-30', 'Business', 900.00),
(9, 9, '2024-09-10', 'Grants', 600.00),
(10, 10, '2024-10-15', 'Pension', 700.00);

-- Insert at least 10 records into expense_categories table
INSERT INTO expense_categories (user_id, category_name) 
VALUES 
(1, 'Food'),
(2, 'Transportation'),
(3, 'Rent'),
(4, 'Utilities'),
(5, 'Insurance'),
(6, 'Entertainment'),
(7, 'Healthcare'),
(8, 'Education'),
(9, 'Clothing'),
(10, 'Miscellaneous');

-- Insert at least 10 records into expenses table
INSERT INTO expenses (user_id, account_id, expense_date, expense_category, remark, amount) 
VALUES 
(1, 1, '2024-01-15', 'Food', 'Groceries', 100.00),
(2, 2, '2024-02-20', 'Transportation', 'Gas', 50.00),
(3, 3, '2024-03-25', 'Rent', 'March Rent', 300.00),
(4, 4, '2024-04-30', 'Utilities', 'Electricity', 75.00),
(5, 5, '2024-05-10', 'Insurance', 'Car Insurance', 150.00),
(6, 6, '2024-06-15', 'Entertainment', 'Movie Night', 20.00),
(7, 7, '2024-07-20', 'Healthcare', 'Doctor Visit', 100.00),
(8, 8, '2024-08-25', 'Education', 'Books', 50.00),
(9, 9, '2024-09-05', 'Clothing', 'New Jacket', 60.00),
(10, 10, '2024-10-10', 'Miscellaneous', 'Birthday Gift', 40.00);

-- Similarly, insert at least 10 records into the transactions table
-- These will be automatically handled by the income/expense triggers as data is inserted into incomes and expenses.

-- Insert at least 10 records into budgets table
INSERT INTO budgets (user_id, expense_category, amount) 
VALUES 
(1, 1, 200.00),
(2, 2, 150.00),
(3, 3, 400.00),
(4, 4, 100.00),
(5, 5, 250.00),
(6, 6, 300.00),
(7, 7, 350.00),
(8, 8, 500.00),
(9, 9, 100.00),
(10, 10, 150.00);

-- Insert at least 10 records into target_amounts table
INSERT INTO target_amounts (user_id, amount) 
VALUES 
(1, 1000.00),
(2, 2000.00),
(3, 1500.00),
(4, 1200.00),
(5, 1800.00),
(6, 2500.00),
(7, 800.00),
(8, 1000.00),
(9, 2000.00),
(10, 1500.00);
