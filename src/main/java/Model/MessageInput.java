package Model;

public class MessageInput{
    private int posted_by;
    private long time_posted_epoch;
    private String message_text;
    public void setMessageText(String message_text){
        this.message_text = message_text;
    }
    public String getMessageText(){
        return message_text;
    }
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
