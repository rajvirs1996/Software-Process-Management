package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Athens on 2/19/2018.
 */

//Courses
public class CoursesFragment extends Fragment{

    private RecyclerView allCoursesList;
    private DatabaseReference allDatabaseCoursesReference;
    private FirebaseRecyclerOptions<allCourses> options ;
    private FirebaseRecyclerAdapter<allCourses, allCoursesViewHolder> firebaseRecyclerAdapter;
    private FirebaseDatabase mDatabse;
    private Query query;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.courses, container, false);


    }
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mDatabse = FirebaseDatabase.getInstance();

        //List view
        allCoursesList = view.findViewById(R.id.coursesList);
        allCoursesList.setHasFixedSize(true);
        //set layout
        allCoursesList.setLayoutManager(new LinearLayoutManager(getContext()));
        //grab all courses from list
        allDatabaseCoursesReference = mDatabse.getReference().child("Courses");

        //TODO VERIFY THAT USER IS FROM THE COURSE
        //verify here

        options = new FirebaseRecyclerOptions.Builder<allCourses>().
                setQuery(allDatabaseCoursesReference,allCourses.class).build();

        firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<allCourses, allCoursesViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull allCoursesViewHolder holder, int position, @NonNull allCourses model) {
                        //bind object
                        holder.setCoursename(model.getCoursename());
                    }

                    @NonNull
                    @Override
                    public allCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view1 = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_courses,parent,false);
                        return new allCoursesViewHolder(view1);
                    }
                };

        allCoursesList.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }


    public class allCoursesViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public allCoursesViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
        }

        public void setCoursename(final String coursename){
            final Button courseBtn =  mView.findViewById(R.id.courseBtn);
            courseBtn.setText(coursename);

            //TODO ADD BUTTON STUFF HERE
            //pseudocode
            //OnClick start
            // get button text
            // check database for button's 'coursename'
            // start fragment
            //Onclick end
            courseBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Fragment newFragment = new CourseSelectFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("coursename",coursename);
                    newFragment.setArguments(args);

                    transaction.replace(R.id.coursesListFrame, newFragment);
                    transaction.addToBackStack(null);

                    transaction.commit();
                }

            });

        }


    }

}
