package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by OWNE on 3/27/2018.
 */

public class GroupStudentListModel {
    public String isMemberOf;
    public String sid;
    public String fullname;

    public GroupStudentListModel() {
    }

    public GroupStudentListModel(String isMemberOf, String sid,
                                 String fullname) {
        this.isMemberOf = isMemberOf;
        this.fullname = fullname;
        this.sid = sid;
    }

    public void setIsMemberOf(String isMemberOf) {
        this.isMemberOf = isMemberOf;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIsMemberOf() {
        return isMemberOf;
    }

    public String getSid() {
        return sid;
    }

    public String getFullname() {
        return fullname;
    }
}
