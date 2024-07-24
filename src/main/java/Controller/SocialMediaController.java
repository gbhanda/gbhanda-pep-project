package Controller;

import Service.AccountService;
import Service.MessageService;
import Model.*;
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

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void createAccountHandler (Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AccountInput accountInput= mapper.readValue(context.body(), AccountInput.class);
        
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
        AccountInput accountInput= mapper.readValue(context.body(), AccountInput.class);
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
        MessageInput messageInput= mapper.readValue(context.body(), MessageInput.class);
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
        if(message != null){
            context.json(mapper.writeValueAsString(message));
        }
    }

    private void deleteMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessageById(messageId);
        if(message != null){
            context.json(mapper.writeValueAsString(message)); 
        } 
    }

    private void updateMessageByIdHandler(Context context) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        UpdateMessageInput messageInputIncomplete= mapper.readValue(context.body(), UpdateMessageInput.class);
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