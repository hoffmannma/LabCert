package de.th_nuernberg.harwedu.labcert.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Objects;

import de.th_nuernberg.harwedu.labcert.R;


public class SwitchGroupFragment extends Fragment {

    private static String mGroup;
    private static String checkedStr;

    // Testvariablen
    private String[] grpStrArray = {"Gruppe 1", "Gruppe 2", "Gruppe 3", "Gruppe 4",
            "Gruppe 5", "Gruppe 6"};

    private static int grp_count = 5;

    public SwitchGroupFragment() {
        // Required empty public constructor
    }

    public static SwitchGroupFragment newInstance(String grp) {
        SwitchGroupFragment fragment = new SwitchGroupFragment();
        mGroup = grp;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_switch_group, container, false);

        RadioGroup switchRadioGroup = (RadioGroup) rootView.findViewById(R.id.radiogroup_switch);
        Button switchButton = (Button) rootView.findViewById(R.id.button_switch);
        final TextView switchTextView = (TextView) rootView.findViewById(R.id.textview_switch);

        for(int i =0; i<grp_count;i++)
        {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(grpStrArray[i]);
            radioButton.setTextSize(32);
            switchRadioGroup.addView(radioButton);
        }

        switchRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                for(int i=0; i<radioGroup.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(i);
                    if(btn.getId() == checkedId) {
                        checkedStr = (String) btn.getText();
                        if (!Objects.equals(checkedStr, mGroup))
                            switchTextView.setText("Achtung: Änderung der aktuellen Gruppe");
                        else
                            switchTextView.setText("");
                        return;
                    }
                }
            }
        });

        switchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
/* $$$$
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
