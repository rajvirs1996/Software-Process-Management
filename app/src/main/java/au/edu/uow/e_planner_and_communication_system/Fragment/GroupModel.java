package au.edu.uow.e_planner_and_communication_system.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OWNE on 3/26/2018.
 */

public class GroupModel {

    public String groupname;
    public String sid;
    public String fullname;

    public GroupModel() {}

    public GroupModel(String groupname, String sid, String fullname){
        this.sid = sid;
        this.fullname = fullname;
        this.groupname = groupname;
    }

    public void setGroupname(String groupname){
        this.groupname = groupname;
    }

    public void setSID(String sid){
        this.sid = sid;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getSid() {
        return sid;
    }

    public String getFullname(){
        return fullname;
    }
}
