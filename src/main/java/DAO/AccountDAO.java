package DAO;
import Util.ConnectionUtil;
import java.sql.*;
import Model.Account;

public class AccountDAO {

    public boolean userNameExists(String username){
        
        boolean exists = false;
        try(Connection connection = ConnectionUtil.getConnection()){
            String sql = "SELECT EXISTS(SELECT 1 FROM account WHERE username = ?) AS doesExist;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                exists = rs.getBoolean("doesExist");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return exists;
    }

    public boolean userIdExists(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        boolean exists = false;
        try{
            String sql = "SELECT EXISTS(SELECT 1 FROM account WHERE account_id = ?) AS doesExist;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                exists = rs.getBoolean("doesExist");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return exists;
    }

    public Account createAccount(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                int generatedAccountId = (int) resultSet.getLong("account_id");
                return new Account(generatedAccountId, username, password);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public Account login(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * " +
                            "FROM account " +
                            "WHERE username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Account((int)resultSet.getLong("account_id"), username, password);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
