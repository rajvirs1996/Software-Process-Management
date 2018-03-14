package au.edu.uow.e_planner_and_communication_system.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import au.edu.uow.e_planner_and_communication_system.R;


/**
 * Created by Tony on 20/2/2018.
 */

public class LogoutFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.logout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        SharedPreferences settings = this.getActivity().getSharedPreferences("UserInfo",0);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.clear().apply();
        FirebaseAuth.getInstance().signOut();
        getActivity().finish();
    }
}
