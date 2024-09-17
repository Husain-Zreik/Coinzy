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
    category_name VARCHAR(100),
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


INSERT INTO accounts (user_id, account_type, balance, liabilities) 
VALUES 
(51, 'Sales', 1500.00, 100.00),
(51, 'Marketing', 1200.00, 50.00),
(51, 'Operations', 1800.00, 200.00),
(51, 'Finance', 1300.00, 150.00);

INSERT INTO income_sources (user_id, source_name) 
VALUES 
(51, 'Salary'),
(51, 'Freelance Work'),
(51, 'Investments'),
(51, 'Rental Income'),
(51, 'Consulting'),
(51, 'Dividends'),
(51, 'Side Job'),
(51, 'Grants'),
(51, 'Business Revenue'),
(51, 'Pension');

INSERT INTO incomes (user_id, account_id, income_date, income_source, amount) 
VALUES 
(51, 1, '2024-01-10', 'Salary', 1500.00),
(51, 1, '2024-01-15', 'Freelance Work', 800.00),
(51, 1, '2024-02-10', 'Investments', 600.00),
(51, 1, '2024-02-20', 'Rental Income', 1000.00),
(51, 2, '2024-03-10', 'Consulting', 1200.00),
(51, 2, '2024-03-25', 'Dividends', 900.00),
(51, 2, '2024-04-10', 'Side Job', 300.00),
(51, 2, '2024-04-20', 'Grants', 500.00),
(51, 3, '2024-05-10', 'Business Revenue', 2000.00),
(51, 3, '2024-05-15', 'Pension', 700.00),
(51, 3, '2024-06-05', 'Part-time Job', 400.00),
(51, 3, '2024-06-20', 'Royalties', 1000.00),
(51, 4, '2024-07-10', 'Freelance Projects', 800.00),
(51, 4, '2024-07-25', 'Savings Interest', 250.00),
(51, 4, '2024-08-10', 'Contracting Work', 1200.00);

INSERT INTO expense_categories (user_id, category_name) 
VALUES 
(51, 'Groceries'),
(51, 'Rent'),
(51, 'Utilities'),
(51, 'Transportation'),
(51, 'Insurance'),
(51, 'Healthcare'),
(51, 'Entertainment'),
(51, 'Dining'),
(51, 'Clothing'),
(51, 'Gifts');

INSERT INTO expenses (user_id, account_id, expense_date, expense_category, remark, amount) 
VALUES 
(51, 1, '2024-01-12', 'Groceries', 'Supermarket Shopping', 100.00),
(51, 1, '2024-01-25', 'Rent', 'Monthly Rent', 800.00),
(51, 1, '2024-02-05', 'Utilities', 'Electricity Bill', 60.00),
(51, 1, '2024-02-15', 'Transportation', 'Gas for Car', 50.00),
(51, 1, '2024-02-28', 'Insurance', 'Car Insurance', 150.00),
(51, 2, '2024-03-10', 'Healthcare', 'Doctor Visit', 100.00),
(51, 2, '2024-03-20', 'Entertainment', 'Movie Ticket', 40.00),
(51, 2, '2024-04-01', 'Dining', 'Dinner at Restaurant', 80.00),
(51, 2, '2024-04-15', 'Clothing', 'New Jacket', 120.00),
(51, 2, '2024-05-05', 'Gifts', 'Birthday Gift', 50.00),
(51, 3, '2024-05-20', 'Travel', 'Flight Ticket', 500.00),
(51, 3, '2024-06-02', 'Subscriptions', 'Music Subscription', 10.00),
(51, 3, '2024-06-15', 'Education', 'Online Course', 200.00),
(51, 4, '2024-07-10', 'Maintenance', 'Car Repair', 250.00),
(51, 4, '2024-07-25', 'Miscellaneous', 'Miscellaneous Purchase', 30.00);

INSERT INTO budgets (user_id, expense_category, amount) 
VALUES 
(51, 'Groceries', 500.00),
(51, 'Rent', 1200.00),
(51, 'Utilities', 300.00),
(51, 'Transportation', 200.00),
(51, 'Insurance', 400.00),
(51, 'Healthcare', 600.00),
(51, 'Entertainment', 300.00),
(51, 'Dining', 200.00),
(51, 'Clothing', 500.00),
(51, 'Gifts', 150.00),
(51, 'Travel', 1000.00);


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
(51, 1500.00);
