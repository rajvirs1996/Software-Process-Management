package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by OWNE on 3/26/2018.
 */

public class StudentModel {
    public String SID;

    public StudentModel(){}

    public StudentModel(String SID) {
        this.SID = SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getSID() {
        return SID;
    }
}
