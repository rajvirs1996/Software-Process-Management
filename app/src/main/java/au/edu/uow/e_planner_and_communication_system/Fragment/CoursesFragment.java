package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        if (container != null) {
            container.removeAllViews();
        }
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

        DatabaseReference userCheckRef = mDatabse.getReference().child("Courses_Student_List");

        //TODO VERIFY THAT USER IS FROM THE COURSE
        //verify here

        allDatabaseCoursesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot check1SnapShot: dataSnapshot.getChildren())
                {
                    
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

        //
        //TODO hide add button from students
        //


        String currentID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentID);

        final View createCourseBtn = view.findViewById(R.id.createCourseBtn);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               String adminStatus =  dataSnapshot.child("isAdmin").getValue().toString();

               if(adminStatus.equals("false"))
               {
                createCourseBtn.setVisibility(Button.INVISIBLE);
               } else if (adminStatus.equals("true"))
               {
                   createCourseBtn.setVisibility(Button.VISIBLE);
               }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        createCourseBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add new course: ");

                // Set up the input
                final EditText input = new EditText(getContext());
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //get user input
                        Map<String,Object> addToDatabase = new HashMap<>();
                        addToDatabase.put("coursehead", "" );
                        addToDatabase.put("coursename", input.getText().toString());

                        //push to database
                        DatabaseReference tempref = allDatabaseCoursesReference
                                .child(input.getText().toString());

                        tempref.updateChildren(addToDatabase);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


            }

        });
        //Add event end

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
