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

-- Create the users table
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

-- Create the financial_categories table
CREATE TABLE financial_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(100) UNIQUE,
    category_type ENUM('Income', 'Expense', 'Loan')
);

-- Create the incomes table
CREATE TABLE incomes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    category_id INT,
    amount DECIMAL(10, 2),
    income_date DATE,
    recurrence_pattern ENUM('none', 'daily', 'weekly', 'monthly') DEFAULT 'none',
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES financial_categories(id) ON DELETE SET NULL
);

-- Create the expenses table
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    category_id INT,
    amount DECIMAL(10, 2),
    expense_date DATE,
    recurrence_pattern ENUM('none', 'daily', 'weekly', 'monthly') DEFAULT 'none',
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES financial_categories(id) ON DELETE SET NULL
);

-- Create the loans table
CREATE TABLE loans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2),
    repayment_date DATE,
    repayment_frequency ENUM('Monthly', 'One-Time'),
    is_given BOOLEAN,
    recipient_name VARCHAR(100),
    note TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the wallets table
CREATE TABLE wallets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    wallet_name VARCHAR(100),
    balance DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the transactions table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    wallet_id INT,
    type ENUM('Income', 'Expense', 'Loan'),
    amount DECIMAL(10, 2),
    description TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (wallet_id) REFERENCES wallets(id) ON DELETE SET NULL
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

-- Create the triggers
DELIMITER //

-- Trigger for inserting a transaction after an income record is inserted
CREATE TRIGGER afterIncomeInsert
AFTER INSERT ON incomes
FOR EACH ROW
BEGIN
    INSERT INTO transactions(wallet_id, type, amount, description)
    VALUES (NULL, 'Income', NEW.amount, CONCAT('Income recorded: ', NEW.amount));
END;
//

-- Trigger for inserting a transaction after an expense record is inserted
CREATE TRIGGER afterExpenseInsert
AFTER INSERT ON expenses
FOR EACH ROW
BEGIN
    INSERT INTO transactions(wallet_id, type, amount, description)
    VALUES (NULL, 'Expense', NEW.amount, CONCAT('Expense recorded: ', NEW.amount));
END;
//

-- Trigger for inserting a transaction when a loan is added or updated
CREATE TRIGGER afterLoanInsert
AFTER INSERT ON loans
FOR EACH ROW
BEGIN
    IF NEW.is_given THEN
        INSERT INTO transactions(wallet_id, type, amount, description)
        VALUES (NULL, 'Loan Given', NEW.amount, CONCAT('Loan given to: ', NEW.recipient_name));
    ELSE
        INSERT INTO transactions(wallet_id, type, amount, description)
        VALUES (NULL, 'Loan Taken', NEW.amount, CONCAT('Loan taken: ', NEW.note));
    END IF;
END;
//

DELIMITER ;

-- Insert sample users with different statuses and varied created_at dates in 2024
INSERT INTO users (name, email, username, password, role_id, status, created_at)
VALUES
    -- Admins
    ('Alice Johnson', 'alice1@example.com', 'alicejohnson', 'password123', 1, 'approved', '2024-01-15'),
    ('Bob Smith', 'bob1@example.com', 'bobsmith', 'password123', 1, 'approved', '2024-02-20'),
    ('Carol White', 'carol1@example.com', 'carolwhite', 'password123', 1, 'pending', '2024-03-25'),
    
    -- Members
    ('Grace Wilson', 'grace1@example.com', 'gracewilson', 'password123', 2, 'approved', '2024-07-20'),
    ('Hank Adams', 'hank1@example.com', 'hankadams', 'password123', 2, 'rejected', '2024-08-25'),
    ('Ivy Scott', 'ivy1@example.com', 'ivyscott', 'password123', 2, 'pending', '2024-09-30'),
    ('Jack Thomas', 'jack1@example.com', 'jackthomas', 'password123', 2, 'blocked', '2024-10-15');
    
-- Insert sample financial categories
INSERT INTO financial_categories (category_name, category_type)
VALUES
    ('Salary', 'Income'),
    ('Rent', 'Expense'),
    ('Utilities', 'Expense'),
    ('Loan Repayment', 'Loan');

-- Insert sample incomes
INSERT INTO incomes (user_id, category_id, amount, income_date, recurrence_pattern, note)
VALUES
    (1, 1, 3000.00, '2024-09-01', 'monthly', 'Monthly salary'),
    (2, 1, 1500.00, '2024-09-01', 'monthly', 'Monthly salary');

-- Insert sample expenses
INSERT INTO expenses (user_id, category_id, amount, expense_date, recurrence_pattern, note)
VALUES
    (1, 2, 1200.00, '2024-09-05', 'monthly', 'Monthly rent'),
    (2, 3, 200.00, '2024-09-06', 'monthly', 'Monthly utilities');

-- Insert sample loans
INSERT INTO loans (user_id, amount, repayment_date, repayment_frequency, is_given, recipient_name, note)
VALUES
    (1, 500.00, '2024-12-01', 'One-Time', TRUE, 'Charlie Brown', 'Loan given to Charlie Brown'),
    (2, 1000.00, '2024-12-01', 'One-Time', FALSE, NULL, 'Loan taken');

-- Insert sample wallets
INSERT INTO wallets (user_id, wallet_name, balance)
VALUES
    (1, 'Main Wallet', 5000.00),
    (2, 'Savings Wallet', 3000.00);

-- Insert sample transactions
INSERT INTO transactions (wallet_id, type, amount, description)
VALUES
    (1, 'Income', 3000.00, 'Salary for September'),
    (2, 'Expense', 1200.00, 'Rent payment');

-- Insert sample announcements
INSERT INTO announcements (title, message, created_by)
VALUES
    ('Welcome!', 'Welcome to the Coinzy Finance System!', 1),
    ('Update', 'Please note that system maintenance will occur on the weekend.', 2);
