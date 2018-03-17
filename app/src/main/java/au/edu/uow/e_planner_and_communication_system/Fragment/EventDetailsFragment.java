package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import au.edu.uow.e_planner_and_communication_system.R;

/**
 * Created by Athens on 2018/03/16.
 */

public class EventDetailsFragment extends Fragment {

    String eventNameFromEvents = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null) {
            container.removeAllViews();
        }

        eventNameFromEvents = getArguments().getString("eventname");
        //inflater
        return inflater.inflate(R.layout.eventdetails, container, false);
    }


    public void onViewCreated(View view, Bundle savedInstanceState) {
        TextView eventTitle = view.findViewById(R.id.eventTitle);
        eventTitle.setText(eventNameFromEvents);

        Button backButton = view.findViewById(R.id.backEventDetailsBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newFragment = new EventsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                transaction.replace(R.id.eventdetailsFrame, newFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
    }



}
