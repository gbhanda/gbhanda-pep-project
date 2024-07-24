package Service;


import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account createAccount(String username, String password){
        if(username.isBlank() || password.length() < 4 || accountDAO.userNameExists(username)){
            return null;
        }
        return accountDAO.createAccount(username, password);
    }

    public Account userLogin(String username, String password){
        return accountDAO.login(username, password);
    }
    
}
