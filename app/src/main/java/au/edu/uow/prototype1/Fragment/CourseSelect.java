package au.edu.uow.prototype1.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.edu.uow.prototype1.R;

/**
 * Created by Athens on 2/20/2018.
 */

public class CourseSelect extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.course_select, container, false);
    }

}
