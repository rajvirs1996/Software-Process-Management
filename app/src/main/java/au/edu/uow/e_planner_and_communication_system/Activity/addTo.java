package au.edu.uow.e_planner_and_communication_system.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import au.edu.uow.e_planner_and_communication_system.R;

public class addTo extends DialogFragment {

    TextView choice;
    TextView groupOrCourse;
    Spinner firstSpinner;
    Spinner secondSpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_add_to,null);

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    choice = (TextView) view.findViewById(R.id.add_to_choice);
    groupOrCourse = (TextView) view.findViewById(R.id.add_to_towhere_text);
    firstSpinner = (Spinner) view.findViewById(R.id.add_to_spinner_1);
    secondSpinner = (Spinner) view.findViewById(R.id.add_to_spinner_2);

    String [] itemsSpinner1 = new String[] {"Please Select","Course","Group"};
    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,itemsSpinner1);
    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
    firstSpinner.setAdapter(adapter1);
    firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            switch (i)
            {
                case 0:
                    break;
                case 1:
                    groupOrCourse.setText("Course");
                    break;
                case 3:
                    break;


            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
    }
}
