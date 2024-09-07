-- Create the database and use it
CREATE DATABASE coinzy_db;
USE coinzy_db;

-- Create the users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100)
);

-- Create the accounts table
CREATE TABLE accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_type VARCHAR(50),
    balance DECIMAL(10, 2),
    liabilities DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the income_sources table
CREATE TABLE income_sources (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    source_name VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the incomes table
CREATE TABLE incomes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_id INT,
    income_date DATE,
    income_source VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Create the expense_categories table
CREATE TABLE expense_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    category_name VARCHAR(100) UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create the expenses table
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    account_id INT,
    expense_date DATE,
    expense_category INT,
    remark VARCHAR(100),
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (expense_category) REFERENCES expense_categories(id)
);

-- Create the transactions table
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT,
    type VARCHAR(10),
    amount DECIMAL(10,2),
    statement VARCHAR(255),
    time TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(id)
);

-- Create the triggers
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

-- Create the budgets table
CREATE TABLE budgets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    expense_category INT,
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (expense_category) REFERENCES expense_categories(id)
);

-- Create the target_amounts table
CREATE TABLE target_amounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
