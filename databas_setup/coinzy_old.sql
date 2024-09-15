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
    expense_category INT,
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (expense_category) REFERENCES expense_categories(id) ON DELETE CASCADE
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

-- Seeder Script for Populating the Database with Users
-- Seed random users with different created_at dates
INSERT INTO users (name, email, username, password, role_id, status, created_at) VALUES
('Alice Johnson', 'alice1@example.com', 'alice1', 'password123', 2, 'approved', '2024-01-15 08:30:00'),
('Bob Smith', 'bobsmith@example.com', 'bobsmith', 'password123', 2, 'pending', '2024-02-10 09:00:00'),
('Carol Davis', 'caroldavis@example.com', 'carold', 'password123', 2, 'approved', '2024-03-05 12:45:00'),
('David Evans', 'davide@example.com', 'davidevans', 'password123', 2, 'rejected', '2024-04-12 13:30:00'),
('Emily Harris', 'emilyh@example.com', 'emilyh', 'password123', 2, 'blocked', '2024-05-20 10:15:00'),
('Frank Green', 'frankg@example.com', 'frankg', 'password123', 2, 'approved', '2024-06-07 11:50:00'),
-- Add additional users as needed here
-- For example, add more users with different dates and statuses.

-- Example to insert random users with random dates using a loop
DELIMITER //

CREATE PROCEDURE seed_users()
BEGIN
    DECLARE i INT DEFAULT 7;  -- Starting from 7 as we've already added some users
    WHILE i <= 30 DO
        INSERT INTO users (name, email, username, password, role_id, status, created_at)
        VALUES (
            CONCAT('User', i),
            CONCAT('user', i, '@example.com'),
            CONCAT('user', i),
            'password123',
            2,
            'approved',
            DATE_ADD('2024-01-01', INTERVAL FLOOR(RAND() * 365) DAY)
        );
        SET i = i + 1;
    END WHILE;
END //

DELIMITER ;

-- Call the seeder procedure to populate with random users
CALL seed_users();
