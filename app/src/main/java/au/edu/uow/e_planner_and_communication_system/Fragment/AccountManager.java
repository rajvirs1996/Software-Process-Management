package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.*;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import au.edu.uow.e_planner_and_communication_system.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Athens on 2018/02/27.
 */

public class AccountManager extends Fragment {

    private CircleImageView accountManagerDisplayImagae;
    private TextView accountManagerDisplayName;
    private TextView getAccountManagerDisplaystatus;
    private Button accountManagerChangeName;
    private Button accountManagerChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        accountManagerDisplayImagae = (CircleImageView)

        return inflater.inflate(R.layout.accountmanager, container, false);
    }

}
