package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by OWNE on 3/26/2018.
 */

public class GroupSelectFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.course_select, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {



    }

}
