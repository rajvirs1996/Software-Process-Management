package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by Manish on 14/03/2018.
 */

public class allUsers
{

    public String name;
    public String user_image;
    public String user_status;
    private String user_thumb_image;
    private  String isAdmin ;
    private String isStudent;
    private  String isTeachingStaff;

    public allUsers(){

    }

    public allUsers(String name, String user_image, String user_status,String user_thumb_image, String isAdmin, String isStudent, String isTeachingStaff) {
        this.name = name;
        this.user_image = user_image;
        this.user_status = user_status;
        this.user_thumb_image = user_thumb_image;
        this.isAdmin = isAdmin;
        this.isStudent = isStudent;
        this.isTeachingStaff = isTeachingStaff;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getIsStudent() {
        return isStudent;
    }

    public void setIsStudent(String isStudent) {
        this.isStudent = isStudent;
    }

    public String getIsTeachingStaff() {
        return isTeachingStaff;
    }

    public void setIsTeachingStaff(String isTeachingStaff) {
        this.isTeachingStaff = isTeachingStaff;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getUser_thumb_image() { return  user_thumb_image;}
    public void  setUser_thumb_image(String user_thumb_image){this.user_thumb_image = user_thumb_image;}


}
