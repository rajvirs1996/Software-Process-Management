package au.edu.uow.e_planner_and_communication_system.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import au.edu.uow.e_planner_and_communication_system.R;

public class removeFrom extends DialogFragment {

    private TextView choice;
    private TextView groupOrCourse;
    private TextView titleText;
    private TextView groupTextView;
    private Spinner firstSpinner;
    private Spinner secondSpinner;
    private Spinner thirdSpinner;
    private Button confirmButton;
    private Button cancelButton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_add_to, null);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        choice = (TextView) view.findViewById(R.id.add_to_choice);
        groupOrCourse = (TextView) view.findViewById(R.id.add_to_towhere_text);
        titleText = (TextView) view.findViewById(R.id.add_to_title_text);
        groupTextView = (TextView) view.findViewById(R.id.add_to_gorup_text);

        firstSpinner = (Spinner) view.findViewById(R.id.add_to_spinner_1);
        secondSpinner = (Spinner) view.findViewById(R.id.add_to_spinner_2);
        thirdSpinner = (Spinner) view.findViewById(R.id.add_to_third_Spinner);

        confirmButton = (Button) view.findViewById(R.id.add_to_ok_button);
        cancelButton = (Button) view.findViewById(R.id.add_to_cancel_button);

        choice.setVisibility(TextView.INVISIBLE);
        groupTextView.setVisibility(TextView.INVISIBLE);
        secondSpinner.setVisibility(Spinner.INVISIBLE);
        thirdSpinner.setVisibility(Spinner.INVISIBLE);

        titleText.setText("Remove:");
        titleText.setVisibility(TextView.VISIBLE);

        String[] itemsSpinner1 = new String[]{"Please Select", "Course", "Group"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, itemsSpinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        firstSpinner.setAdapter(adapter1);
        firstSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        choice.setVisibility(TextView.INVISIBLE);
                        groupTextView.setVisibility(TextView.INVISIBLE);
                        secondSpinner.setVisibility(Spinner.INVISIBLE);
                        thirdSpinner.setVisibility(Spinner.INVISIBLE);
                        break;
                    case 1:
                        thirdSpinner.setVisibility(Spinner.INVISIBLE);
                        groupTextView.setVisibility(TextView.INVISIBLE);

                        choice.setText("Course");
                        choice.setVisibility(TextView.VISIBLE);
                        secondSpinner.setVisibility(Spinner.VISIBLE);
                        break;
                    case 2:

                        choice.setText("Course");

                        choice.setVisibility(TextView.VISIBLE);
                        secondSpinner.setVisibility(Spinner.VISIBLE);

                        groupTextView.setVisibility(TextView.VISIBLE);
                        thirdSpinner.setVisibility(Spinner.VISIBLE);

                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
