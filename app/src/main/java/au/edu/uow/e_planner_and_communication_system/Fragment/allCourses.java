package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by Athens on 2018/03/17.
 */

public class allCourses {

    public String coursehead;
    public String coursename;
    public String grouplist;
    public String studentlist;

    public allCourses() {
    }

    public allCourses(String coursehead, String coursename, String grouplist, String studentlist) {

        this.coursehead = coursehead;
        this.coursename = coursename;
        this.grouplist = grouplist;
        this.studentlist = studentlist;

    }

    public String getCoursehead() {
        return coursehead;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getGrouplist() {
        return grouplist;
    }

    public String getStudentlist() {
        return studentlist;
    }

    public void setCoursehead(String coursehead) {
        this.coursehead = coursehead;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setGrouplist(String grouplist) {
        this.grouplist = grouplist;
    }

    public void setStudentlist(String studentlist) {
        this.studentlist = studentlist;
    }
}
