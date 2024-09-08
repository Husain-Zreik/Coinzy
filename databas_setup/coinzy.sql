-- Create the database and use it
CREATE DATABASE coinzy_db;
USE coinzy_db;

-- Create the roles table
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE
);

-- Insert default roles
INSERT INTO roles (role_name) VALUES ('Admin'), ('Manager'), ('Member');

-- Create the users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role_id INT,
    status ENUM('pending', 'approved', 'rejected', 'blocked') DEFAULT 'pending',  -- For admin approval
    owner_id INT, -- Used for Owners to link Members
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE SET NULL,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL -- Links a member to an owner
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
    recurrence_pattern ENUM('none', 'daily', 'weekly', 'monthly') DEFAULT 'none', -- e.g., "daily", "weekly", "monthly", or "none"
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
    recurrence_pattern ENUM('none', 'daily', 'weekly', 'monthly') DEFAULT 'none', -- e.g., "daily", "weekly", "monthly", or "none"
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
    is_given BOOLEAN, -- TRUE if loan is given, FALSE if loan is taken
    recipient_name VARCHAR(100), -- Used if the loan is given
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

-- Create the chats table
CREATE TABLE chats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    initiator_id INT, -- User who starts the chat
    participant_id INT, -- User who is involved in the chat
    FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (participant_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create the messages table
CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    chat_id INT,
    sender_id INT,
    message TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE
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
