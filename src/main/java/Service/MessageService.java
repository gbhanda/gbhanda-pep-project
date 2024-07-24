package Service;

import Model.Message;
import Model.Account;
import java.util.ArrayList;
import java.util.List;
import DAO.MessageDAO;
import DAO.AccountDAO;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = accountDAO;
    }

    public Message createNewMessage(int postedBy, String messageText, Long timePostedEpoch){
        if(!accountDAO.userIdExists(postedBy) || messageText.isBlank() || messageText.length() > 255){
            return null;
        }
        return messageDAO.createMessage(postedBy, messageText, timePostedEpoch);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId){
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessage(int messageId, String updatedText){
        if(updatedText.isBlank() || updatedText.length() > 255){
            return null;
        }
        return messageDAO.updateMessage(messageId, updatedText);
    }

    public List<Message> getMessagesByAUser(int account_id){
        return messageDAO.getMessagesByAccountId(account_id);
    }
}
