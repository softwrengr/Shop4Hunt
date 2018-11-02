package techease.com.shop4hunt.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import techease.com.shop4hunt.R;

public class ThanksFragment extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_thanks, container, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thanks");
        builder.setMessage("this will be your thanks screen \nin coming days it will be completed\nwe are working on this");
        builder.setCancelable(false);
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              getActivity().finish();
            }
        });
        builder.show();
        return view;
    }
}
