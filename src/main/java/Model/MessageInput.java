package Model;

public class MessageInput{
    private int posted_by;
    private String message_text;
    private long time_posted_epoch;

    public void setPosted_by(int posted_by){
        this.posted_by = posted_by;
    }
    public int getPosted_by(){
        return posted_by;
    }
    public void setMessage_text(String message_text){
        this.message_text = message_text;
    }
    public String getMessage_text(){
        return message_text;
    }
    public void setTime_posted_epoch(long time_posted_epoch){
        this.time_posted_epoch = time_posted_epoch;
    }
    public long getTime_posted_epoch(){
        return time_posted_epoch;
    }
}
