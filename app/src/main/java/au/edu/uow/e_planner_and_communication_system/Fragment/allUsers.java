package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by Manish on 14/03/2018.
 */

public class allUsers
{

    public String name;
    public String user_image;
    public String user_status;

    public allUsers(){

    }

    public allUsers(String name, String user_image, String user_status) {
        this.name = name;
        this.user_image = user_image;
        this.user_status = user_status;
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



}
