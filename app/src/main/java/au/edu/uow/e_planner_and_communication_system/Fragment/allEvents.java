package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by Athens on 2018/03/16.
 */

public class allEvents {

    public String date;
    public String event_description;
    public String event_name;

    public allEvents() {
    }

    public allEvents(String date, String event_description, String event_name) {

        this.date = date;
        this.event_description = event_description;
        this.event_name = event_name;

    }

    public String getDate() {
        return date;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

}
