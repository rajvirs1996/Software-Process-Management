package au.edu.uow.prototype1.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import au.edu.uow.prototype1.R;

/**
 * Created by Athens on 2/19/2018.
 */

//Courses
public class FourthFragment extends Fragment{
    boolean hasCSCI123 = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.courses, container, false);


    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final LinearLayout coursesList = view.findViewById(R.id.courseslayout);
        final LayoutParams coursesParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final TextView inText = view.findViewById(R.id.courseIn);
        View addBtn = view.findViewById(R.id.courseAddBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String a = inText.getText().toString();
                //on click off 'Add Courses Button'

                //add CSCI123
                if (!hasCSCI123 && a.equals("CSCI123")) {
                    Button newBtn = new Button(getContext());
                    newBtn.setText("CSCI123");
                    coursesList.addView(newBtn,coursesParam);


                    newBtn.setOnClickListener(new Button.OnClickListener() {
                        public void onClick(View v) {
                            // Create new fragment and transaction
                            Fragment newFragment = new CourseSelect();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();

                            // Replace courseslayout with entire fragment containers
                            transaction.replace(R.id.courseslayout, newFragment);
                            transaction.addToBackStack(null);

                            // Commit the transaction
                            transaction.commit();
                        }
                    });


                    inText.setText("");
                    hasCSCI123 = true;
                }

                //add CSCI123 but CSCI123 already added
                else if (hasCSCI123 && a.equals("CSCI123")) {
                    String b = "Already added!";
                    Toast.makeText(getContext(), b, Toast.LENGTH_LONG).show();
                    inText.setText("");
                }

                //wrong course
                else {
                    String b = "Course not found!";
                    Toast.makeText(getContext(), b, Toast.LENGTH_LONG).show();
                    inText.setText("");
                }
            }

        });








    }


}
