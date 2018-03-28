package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by Manish on 28/03/2018.
 */

public class MessagesGroup {

    private String message, type;
    private long time;
    private boolean seen;
    private String from;
    private String GID;
    private String groupName;
    private String messengersName;

    public MessagesGroup() {
    }

    public MessagesGroup(String message, String type, long time, boolean seen, String from, String GID, String groupName, String messengersName) {
        this.message = message;
        this.type = type;
        this.time = time;
        this.seen = seen;
        this.from = from;
        this.GID = GID;
        this.groupName = groupName;
        this.messengersName = messengersName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMessengersName() {
        return messengersName;
    }

    public void setMessengersName(String messengersName) {
        this.messengersName = messengersName;
    }
}
