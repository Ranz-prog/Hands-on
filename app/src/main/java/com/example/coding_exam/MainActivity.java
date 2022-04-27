package com.example.coding_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PathEffect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

public class MainActivity extends AppCompatActivity {

    TextInputEditText FName,Email,Bday,Age,Mobile;
    Button submit;
    Dialog dialog;
    Spinner gen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Spinner SpinGender = findViewById(R.id.Gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinGender.setAdapter(adapter);

         FName = findViewById(R.id.FnameEditText);
         Email = findViewById(R.id.EmailAddEditText);
         Mobile = findViewById(R.id.MobileEditText);
         Bday = findViewById(R.id.DateEditText);
         Age = findViewById(R.id.AgeEditText);
         submit = findViewById(R.id.button);
         gen = findViewById(R.id.Gender);
         dialog = new Dialog(this);


        //confirmation dialog box
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validfields();
            }
        });


        // Email Validation
        Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()) {
                    submit.setEnabled(true);
                } else {
                    submit.setEnabled(false);
                    Email.setError("Invalid Email Address");
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Mobile number Validation
        Mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validateMobile(Mobile.getText().toString())) {
                    submit.setEnabled(true);
                } else {
                    submit.setEnabled(false);
                    Mobile.setError("Invalid Mobile Number");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // For Selecting Date (Calendar)
        Bday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int nYear = c.get(Calendar.YEAR);
                int nMonth = c.get(Calendar.MONTH);
                int nDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(view.getContext(),datePickerListener, nYear, nMonth, nDay);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
                Bday.getText().clear();
            }
        });


    }
    //dialog box
    private void openConfirmdialog() {
        dialog.setContentView(R.layout.confirmation_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        dialog.show();
        TextView btnok=dialog.findViewById(R.id.txtok);

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }
    //Validation for empty fields
    private void validfields(){
        String user = FName.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String num = Mobile.getText().toString().trim();
        String bday = Bday.getText().toString().trim();
        String age = Age.getText().toString().trim();


        if(user.isEmpty()) {
            FName.setError("Enter First Name");
        }
        else if(email.isEmpty()){
            Email.setError("Enter Email Address");
        }
        else if(num.isEmpty()){
            Mobile.setError("Enter Mobile Number");
        }
        else if(bday.isEmpty()){
            Bday.setError("Enter Your Birthdate");
        }
        else if(age.isEmpty()){
            Age.setError("Enter Your Birthdate");
        }
        else{
            openConfirmdialog();
        }


    }

    //Age validation
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar userAge = new GregorianCalendar(year,month,day);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    submit.setEnabled(false);
                    Bday.setError("Must be 18 above");
                }
                else{
                    submit.setEnabled(true);
                }

                String format = new SimpleDateFormat("MM/dd/yy").format(userAge.getTime());
                Bday.setText(format);
                Age.setText(Integer.toString(calculateAge(userAge.getTimeInMillis())));
            }
        };

        int calculateAge(long date) {
            Calendar dob = Calendar.getInstance();
            dob.setTimeInMillis(date);

            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }
            return age;
        }

    //Mobile number validation
    public boolean validateMobile(String input){
        Pattern pat = Pattern.compile("[0][9][0-9]{9}");
        Matcher mat = pat.matcher(input);
        return mat.matches();
    }
}