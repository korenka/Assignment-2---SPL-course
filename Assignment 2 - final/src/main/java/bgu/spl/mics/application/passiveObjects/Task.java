package bgu.spl.mics.application.passiveObjects;

public class Task {
    private String executerName;
    private String taskDescription;
    private long timestamp;

    public Task(String executerName_, String taskDescription_, long timestamp_){
        executerName=executerName_;
        taskDescription=taskDescription_;
        timestamp=timestamp_;
    }

    public void setExecuterName(String newName){executerName=newName;}
    public void setTaskDescription(String newDescription){taskDescription=newDescription;}
    public void setTimestamp(long newTimestamp){timestamp=newTimestamp;}

    public String getExecuterName() {
        return executerName;
    }
    public String getTaskDescription(){return taskDescription;}
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return
                "executerName='" + executerName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", timestamp=" + timestamp;
    }
}
