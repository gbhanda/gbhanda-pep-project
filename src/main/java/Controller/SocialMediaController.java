package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.Account;
import Model.Message;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::createAccountHandler);
        app.post("login", this::processLoginHandler);
        app.post("messages", this::createNewMessageHandler);
        app.get("messages", this::retriveAllMessagesHandler);
        app.get("messages/{message_id}", this::retriveMessageByIdHandler);
        app.delete("messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("messages/{message_id}", this::updateMessageByIdHandler);
        app.get("accounts/{account_id}/messages", this::retriveAllMessagesByAnAccountHandler);

        return app;
    }

    public class AccountInput{
        private String username;
        private String password;
        public String getUsername(){
            return username;
        }
        public void setUsername(String username){
            this.username = username;
        }
        public String getPassword(){
            return password;
        }
        public void setPassword(String password){
            this.password = password;
        }
    }

    public class MessageInputIncomplete{

        private String message_text;
        public void setMessageText(String message_text){
            this.message_text = message_text;
        }
        public String getMessageText(){
            return message_text;
        }
    }

    public class MessageInput extends MessageInputIncomplete{
        private int posted_by;
        private long time_posted_epoch;
        public void setPostedBy(int posted_by){
            this.posted_by = posted_by;
        }
        public int getPostedBy(){
            return posted_by;
        }
        public void setTimePostedEpoch(long time_posted_epoch){
            this.time_posted_epoch = time_posted_epoch;
        }
        public long getTimePostedEpoch(){
            return time_posted_epoch;
        }
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createAccountHandler (Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AccountInput accountInput= mapper.readValue(context.body().toString(), AccountInput.class);
        Account account = accountService.createAccount(accountInput.getUsername(), accountInput.getPassword());
        if(account == null){
            context.status(400);
        }
        else{
            context.json(mapper.writeValueAsString(account));
        }
    }

    private void processLoginHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        AccountInput accountInput= mapper.readValue(context.body().toString(), AccountInput.class);
        Account account = accountService.userLogin(accountInput.getUsername(), accountInput.getPassword());
        if(account == null){
            context.status(401);
        }
        else{
            context.json(mapper.writeValueAsString(account));
        }
    }

    private void createNewMessageHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        MessageInput messageInput= mapper.readValue(context.body().toString(), MessageInput.class);
        Message message = messageService.createNewMessage(messageInput.getPostedBy(), messageInput.getMessageText(), messageInput.getTimePostedEpoch());
        if(message == null){
            context.status(400);
        }
        else{
            context.json(mapper.writeValueAsString(message));
        }        
    }

    private void retriveAllMessagesHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messages = messageService.getAllMessages();
        context.json(mapper.writeValueAsString(messages));  
    }

    private void retriveMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        context.json(mapper.writeValueAsString(message));
    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(messageId);
        context.json(mapper.writeValueAsString(message));  
    }

    private void updateMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        MessageInputIncomplete messageInputIncomplete= mapper.readValue(context.body().toString(), MessageInputIncomplete.class);
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.updateMessage(messageId, messageInputIncomplete.getMessageText());
        if(message == null){
            context.status(400);
        }
        else{
            context.json(mapper.writeValueAsString(message));
        }  
    }

    private void retriveAllMessagesByAnAccountHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAUser(accountId);
        context.json(mapper.writeValueAsString(messages));  
    }

}