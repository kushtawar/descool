package com.cruxitech.android.descool;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kushtawar on 07/08/16.
 */
public class Validationrules {

    public static String passwordfailuremessage="";

    public boolean isNullorhasNotext_EditText(EditText editText)
    {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError("required");
            return true;
        }else
        {
            editText.setError(null);
            return false;
        }

    }

    public boolean isValidLength_EditText(EditText editText)
    {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() <6) {
            editText.setError("Username must have at least 6 characters");
            return true;
        }else
        {
            editText.setError(null);
            return false;
        }

    }






    public boolean isnullandhasText_Spinner(Spinner spinner)
    {

        View selectedView = spinner.getSelectedView();
        TextView selectedTextView=null;
        if (selectedView != null && selectedView instanceof TextView) {
             selectedTextView = (TextView) selectedView;
            selectedTextView.setError(null);
        }
        String text = spinner.getSelectedItem().toString().trim();

        // length 0 means there is no text
        if (text.length() == 0) {

                selectedTextView.setError("required");
            return true;
            }



        return false;


    }

    public boolean isSelectedValueProper(Spinner spinner)
    {
        View selectedView = spinner.getSelectedView();
        TextView selectedTextView=null;
        selectedTextView = (TextView) selectedView;


        String text = spinner.getSelectedItem().toString().trim();
        if (text.equals(StatusConstants.selectiontext)) {
            selectedTextView.setError("Select proper value");
            return true;

        }
        else {
            selectedTextView.setError(null);
            return true;
        }

    }




    public boolean isPasswordValid(EditText editText)
    {
boolean ispasswordvalidflag=true;
        String password = editText.getText().toString();
        editText.setError(null);
         passwordfailuremessage = "";
        int minimumpwdlength=5;

        if(password.length() < minimumpwdlength) {
            passwordfailuremessage = passwordfailuremessage + "Password length must be at least "+minimumpwdlength+ " characters in length \n";
            ispasswordvalidflag=false;


        }

        Pattern whitespace = Pattern.compile("\\s");
        Matcher hasWhitespace = whitespace.matcher(password);
        boolean foundspace=hasWhitespace.find();

        if(foundspace){

            passwordfailuremessage=passwordfailuremessage + "Password cannot have space "+"\n";
            ispasswordvalidflag=false;
        }

if (ispasswordvalidflag)
{
    editText.setError(null);
}else
{
    editText.setError(passwordfailuremessage);
}

        /*Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Pattern eight = Pattern.compile (".{8}");


        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecial = special.matcher(password);
        Matcher hasEight = eight.matcher(password);

        Log.e("invenapp:password validation:",)
        return hasLetter.find() && hasDigit.find() && hasSpecial.find()
                && hasEight.matches();

          if (pwd.length < 8) return false;
    if (not pwd =~ /[0-9]/) return false;
    if (not pwd =~ /[a-z]/) return false;
    if (not pwd =~ /[A-Z]/) return false;
    if (not pwd =~ /[%@$^]/) return false;
    if (pwd =~ /\s/) return false;



                */


        return ispasswordvalidflag;

    }

    public boolean isEmailValid(EditText editText) {

        String target = editText.getText().toString();
        editText.setError(null);
        if (target == null) {
            editText.setError("required");
            return false;
        } else {
            editText.setError(null);
            boolean isemailvalid= android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

            if(!isemailvalid){
                editText.setError("Not a valid Email Address");

            }else
            {
                editText.setError(null);
            }
           return isemailvalid;
        }




    }





}
