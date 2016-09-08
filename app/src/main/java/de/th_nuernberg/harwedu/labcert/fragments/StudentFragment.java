package de.th_nuernberg.harwedu.labcert.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import de.th_nuernberg.harwedu.labcert.CONFIG;
import de.th_nuernberg.harwedu.labcert.R;
import de.th_nuernberg.harwedu.labcert.database.DataSource;
import de.th_nuernberg.harwedu.labcert.database.DbHelper;
import de.th_nuernberg.harwedu.labcert.javamail.MailSenderAsync;
import de.th_nuernberg.harwedu.labcert.objects.Progress;
import de.th_nuernberg.harwedu.labcert.objects.Requirement;
import de.th_nuernberg.harwedu.labcert.objects.Student;
import de.th_nuernberg.harwedu.labcert.pdf.PdfFile;

/**
 *
 */

public class StudentFragment extends Fragment {
    public static Context context;
    private static Student student;
    private static ArrayList<Requirement> requirements;


    public StudentFragment() {
        // Required empty public constructor
    }

    //TODO Entfernen und Requirements in Student aufrufen
    public static StudentFragment newInstance(Student param) {
        StudentFragment fragment = new StudentFragment();
        student = param;
        return fragment;
    }

    public static StudentFragment newInstance(Student param, ArrayList<Requirement> reqList) {
        StudentFragment fragment = new StudentFragment();
        student = param;
        requirements = reqList;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);

        final TextView studentTxt = (TextView) rootView.findViewById(R.id.textview_student);
        final EditText commEditTxt = (EditText) rootView.findViewById(R.id.edittext_comm);
        LinearLayout linear = (LinearLayout)rootView.findViewById(R.id.linear_buttons_student);
        Button saveDataButton = (Button) rootView.findViewById(R.id.button_save_comment);
        Button createPdfButton = (Button) rootView.findViewById(R.id.button_create_pdf);
        Button delStudentButton = (Button) rootView.findViewById(R.id.button_del_student);
        Button sendMailButton = (Button) rootView.findViewById(R.id.button_send_mail);

        studentTxt.setText(student.getSurname());
        commEditTxt.setText(student.getComment());

        saveDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentString = commEditTxt.getText().toString();

                DataSource dataSource = new DataSource(getActivity());
                dataSource.openW();
                dataSource.updateCommentStudent(student.getId(), commentString);
                dataSource.close();
                getActivity().getFragmentManager().popBackStack();

                toastMsg("Kommentar gespeichert");
            }
        });

        createPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfFile pdfCreator = new PdfFile();
                try {
                    pdfCreator.createPdf(getActivity(), student, false);
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

        delStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataSource dataSource = new DataSource(getActivity());
                dataSource.openW();
                dataSource.deleteStudent(student);
                dataSource.close();

                getActivity().getFragmentManager().popBackStack();

                toastMsg("Student gelöscht");
            }
        });

        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pdf erzeugen
                PdfFile pdfCreator = new PdfFile();
                String fileAttachment = "";
                try {
                    fileAttachment = pdfCreator.createPdf(getActivity(), student, true);
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }

                //Mail versenden
                MailSenderAsync sendMailTask = new MailSenderAsync(CONFIG.getContext(),
                        "Betreffzeile",
                        "Email Text",
                        CONFIG.getEMAIL(),
                        student.getEmail(),
                        fileAttachment);
                sendMailTask.execute();
                //getActivity().getFragmentManager().popBackStack();
                toastMsg("Mail versendet");
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // TODO "Durchreichen von Daten" vereinfachen
        // Tatsächlichen Progress einfügen
        int j = 0;
        for(final Requirement req : requirements)
        {
            LinearLayout ll = new LinearLayout(getActivity());
            ll.setOrientation(LinearLayout.VERTICAL);
            ll.setPadding(0,10,0,0);

            TextView typeTV = new TextView(getActivity());
            typeTV.setTextSize(18);
            typeTV.setText(req.getType());
            ll.addView(typeTV);


            DataSource ds = new DataSource(getContext());
            ds.openR();
            ArrayList<Progress> progressList = ds.getProgress(req.getLab_name(), req.getGroup(),
                    req.getTerm(), req.getType(), student.getMatr());
            ds.close();
            TextView progressTV = new TextView(getActivity());
            progressTV.setTextSize(16);
            progressTV.setText("Status: " + progressList.size() + " / " + req.getCount());
            ll.addView(progressTV);

            final Button btn = new Button(getActivity());

            btn.setId(j+1);
            btn.setText(req.getType() + " setzen");

            btn.setLayoutParams(params);
            btn.getBackground().setColorFilter(Color.parseColor("#EF5350"), PorterDuff.Mode.SRC_ATOP);
            btn.setTextColor(0xFFFFFFFF);
            btn.invalidate();
            final int index = j;
            // Set click listener for button
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DataSource dataSource = new DataSource(getContext());
                    dataSource.openW();
                    // TODO Score anpassen
                    dataSource.insertProg(student.getLabName(), student.getGroup(), student.getTerm(),
                            req.getType(), student.getMatr(), "1");
                    dataSource.close();
                    toastMsg(req.getType() + " gesetzt");
                }
            });

            ll.addView(btn);

            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF); //white background
            border.setStroke(1, 0xFF000000); //black border with full opacity
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                ll.setBackgroundDrawable(border);
            } else {
                ll.setBackground(border);
            }

            // Layout in XML
            linear.addView(ll);
            j++;
        }
        return rootView;

    }

    private void toastMsg(String msg) {
        Context context = getActivity().getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

}
