package de.th_nuernberg.harwedu.labcert.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;

import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;
import de.th_nuernberg.harwedu.labcert.objects.Student;
import de.th_nuernberg.harwedu.labcert.pdf.PdfFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequirementFragment extends Fragment {

    private static Requirement req;

    public RequirementFragment() {
        // Required empty public constructor
    }

    public static RequirementFragment newInstance(Requirement param) {
        RequirementFragment fragment = new RequirementFragment();
        req = param;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_requirement, container, false);
/*
        final TextView studentTxt = (TextView) rootView.findViewById(R.id.textview_student);
        final EditText commEditTxt = (EditText) rootView.findViewById(R.id.edittext_comm);
        Button saveDataButton = (Button) rootView.findViewById(R.id.button_save_comment);
        Button createPdfButton = (Button) rootView.findViewById(R.id.button_create_pdf);
        Button delStudentButton = (Button) rootView.findViewById(R.id.button_del_student);
        Button delAttdButton = (Button) rootView.findViewById(R.id.button_del_attd);

        studentTxt.setText(req.getStudentData());
        commEditTxt.setText(req.getCommentStudent());

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentString = commEditTxt.getText().toString();

                DataSource dataSource = new DataSource(getActivity());
                dataSource.updateCommentStudent(student.getId(), commentString);

                getActivity().getFragmentManager().popBackStack();

                toastMsg("Kommentar gespeichert");
            }
        });

        createPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PdfFile pdfCreator = new PdfFile();
                try {
                    pdfCreator.createPdf(getActivity(), student);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        delStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataSource dataSource = new DataSource(getActivity());
                dataSource.deleteAttdRecords(student);
                dataSource.deleteStudent(student);

                getActivity().getFragmentManager().popBackStack();

                toastMsg("Student gelöscht");
            }
        });

        delAttdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataSource dataSource = new DataSource(getActivity());
                dataSource.deleteAttdRecords(student);
                Student student_new = dataSource.getStudentByBib(student.getBib());
                studentTxt.setText(student_new.getStudentData());

                toastMsg("Anwesenheitsdaten gelöscht");
            }
        });
        */
        return rootView;

    }

    private void toastMsg(String msg) {
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }


}
