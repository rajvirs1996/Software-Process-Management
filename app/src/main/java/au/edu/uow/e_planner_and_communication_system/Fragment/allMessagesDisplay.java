package au.edu.uow.e_planner_and_communication_system.Fragment;

/**
 * Created by Manish on 24/03/2018.
 */

//Recycler view ->>>>>for list
public class allMessagesDisplay
{
    public String name;
    public String user_image;
    public String user_status;
    private String user_thumb_image;

    public allMessagesDisplay(){

    }

    public allMessagesDisplay(String name, String user_image, String user_status,String user_thumb_image) {
        this.name = name;
        this.user_image = user_image;
        this.user_status = user_status;
        this.user_thumb_image = user_thumb_image;
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
