package Model;

public class MessageInput extends MessageInputIncomplete{
    private int posted_by;
    private long time_posted_epoch;

    public void setPosted_by(int posted_by){
        this.posted_by = posted_by;
    }
    public int getPosted_by(){
        return posted_by;
    }
    public void setTime_posted_epoch(long time_posted_epoch){
        this.time_posted_epoch = time_posted_epoch;
    }
    public long getTime_posted_epoch(){
        return time_posted_epoch;
    }
}
