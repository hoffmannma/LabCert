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

import de.th_nuernberg.harwedu.labcert.CONFIG;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.database.DbHelper;
import de.th_nuernberg.harwedu.labcert.javamail.MailSenderAsync;
import de.th_nuernberg.harwedu.labcert.objects.Student;
import de.th_nuernberg.harwedu.labcert.pdf.PdfFile;

/**
 *
 */

public class StudentFragment extends Fragment {
    public static Context context;
    private static Student student;


    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance(Student param) {
        StudentFragment fragment = new StudentFragment();
        student = param;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);

        final TextView studentTxt = (TextView) rootView.findViewById(R.id.textview_student);
        final EditText commEditTxt = (EditText) rootView.findViewById(R.id.edittext_comm);
        Button saveDataButton = (Button) rootView.findViewById(R.id.button_save_comment);
        Button createPdfButton = (Button) rootView.findViewById(R.id.button_create_pdf);
        Button delStudentButton = (Button) rootView.findViewById(R.id.button_del_student);
        Button sendMailButton = (Button) rootView.findViewById(R.id.button_send_mail);

        studentTxt.setText(student.getStudentData());
        commEditTxt.setText(student.getComment());

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
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        delStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataSource dataSource = new DataSource(getActivity());
                dataSource.deleteStudent(student);

                getActivity().getFragmentManager().popBackStack();

                toastMsg("Student gelöscht");
            }
        });

        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MailSenderAsync sendMailTask = new MailSenderAsync(CONFIG.context,
                        "Betreffzeile",
                        "Email Text",
                        CONFIG.EMAIL,
                        student.getEmail());
                sendMailTask.execute();
                //getActivity().getFragmentManager().popBackStack();
                toastMsg("Mail versendet");
            }
        });
        return rootView;

    }

    private void toastMsg(String msg) {
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }


}
