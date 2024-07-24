package DAO;

import Util.ConnectionUtil;
import Model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message createMessage(int postedBy, String messageText, long timePostedEpoch){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, postedBy);
            preparedStatement.setString(2, messageText);
            preparedStatement.setLong(3, timePostedEpoch);
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()){
                int generatedMessageId = (int) resultSet.getLong("message_id");
                return new Message(generatedMessageId, postedBy, messageText, timePostedEpoch);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<Message>();
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message";
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                messages.add(new Message(
                    resultSet.getInt("message_id"),
                    resultSet.getInt("posted_by"),
                    resultSet.getString("message_text"),
                    resultSet.getLong("time_posted_epoch")
                ));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }
    
    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);            
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new Message(
                    messageId, 
                    resultSet.getInt("posted_by"), 
                    resultSet.getString("message_text"), 
                    resultSet.getLong("time_posted_epoch")
                );
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int messageId){
        Message deletedMessage = getMessageById(messageId);
        if(deletedMessage != null){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);            
            int updatedTable = preparedStatement.executeUpdate();
            if(updatedTable > 0){
                return deletedMessage;
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }}
        return null;
    }

    public Message updateMessage(int messageId, String updateMessageText){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, updateMessageText);
            preparedStatement.setInt(2, messageId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if(rowsUpdated > 0){
                return getMessageById(messageId);
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getMessagesByAccountId(int accountId){
        Connection connection = ConnectionUtil.getConnection();
        try{
            List<Message> messages = new ArrayList<Message>();
            
            String sql = "SELECT * FROM message WHERE posted_by = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, accountId);            
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                messages.add(new Message(
                    resultSet.getInt("message_id"), 
                    resultSet.getInt("posted_by"), 
                    resultSet.getString("message_text"), 
                    resultSet.getLong("time_posted_epoch")
                ));
            }
            return messages;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
