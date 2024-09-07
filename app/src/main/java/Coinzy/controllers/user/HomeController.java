// package Coinzy.controllers;

// import Coinzy.models.BudgetModel;
// import Coinzy.models.ExpenseModel;
// import Coinzy.models.IncomeModel;
// import Coinzy.views.user.home.HomeView;

// import java.sql.SQLException;

// public class HomeController {
// private final HomeView view;
// private final BudgetModel budgetModel;
// private final ExpenseModel expenseModel;
// private final IncomeModel incomeModel;

// public HomeController(HomeView view) {
// this.view = view;
// this.budgetModel = new BudgetModel();
// this.expenseModel = new ExpenseModel();
// this.incomeModel = new IncomeModel();
// }

// public void handleAddBudget(String expenseCategory, double amount) {
// try {
// budgetModel.addBudget(UserSession.userId, expenseCategory, amount);
// view.updateComponents();
// } catch (SQLException e) {
// view.showError("Failed to add budget.");
// }
// }

// public void handleRemoveBudget(String expenseCategory) {
// try {
// budgetModel.removeBudget(UserSession.userId, expenseCategory);
// view.updateComponents();
// } catch (SQLException e) {
// view.showError("Failed to remove budget.");
// }
// }

// public void handleAddExpenseCategory(String expenseCategory) {
// try {
// expenseModel.addExpenseCategory(UserSession.userId, expenseCategory);
// view.updateComponents();
// } catch (SQLException e) {
// view.showError("Failed to add expense category.");
// }
// }

// public void handleAddIncomeSource(String incomeSourceName) {
// try {
// incomeModel.addIncomeSource(UserSession.userId, incomeSourceName);
// view.updateComponents();
// } catch (SQLException e) {
// view.showError("Failed to add income source.");
// }
// }
// }
