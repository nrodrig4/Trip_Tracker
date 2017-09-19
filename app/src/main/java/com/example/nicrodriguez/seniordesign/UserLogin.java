package com.example.nicrodriguez.seniordesign;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserLogin extends AppCompatActivity {

    /*Array Adaptor used for ListView*/
    ArrayAdapter<String> listAdapter;
    List<String> listViewList = new ArrayList<>();

    List<String> id = new ArrayList<>();
    List<String> users = new ArrayList<>();
    List<String> passwords = new ArrayList<>();

    public static String selectedUser;
    public static String tableName;

    TripDatabaseHelper db;

    public AlertDialog.Builder enterPasswordDialog;

    public Button newUserButton;
    public EditText username, password;
    public ListView userList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        newUserButton = (Button) findViewById(R.id.newUserButton);
        userList = (ListView)findViewById(R.id.userList);
//        username = (EditText) findViewById(R.id.username_edit);
//        password = (EditText) findViewById(R.id.password_edit);
        db = new TripDatabaseHelper(UserLogin.this);
        enterPasswordDialog = new AlertDialog.Builder(UserLogin.this);
        enterPasswordDialog.setCancelable(false);


        Cursor res = db.getAllUserData();
        if(res.getCount()>0) {


            /* Obtaining the Data Base values */
            while (res.moveToNext()){
                id.add(res.getString(0));
                users.add(res.getString(1));
                passwords.add(res.getString(2));
            }

            listViewList.addAll(users);

            // Create ArrayAdapter using the trip list.
            listAdapter = new ArrayAdapter<>(UserLogin.this, R.layout.simplerow, listViewList);
            userList.setAdapter(listAdapter);


        }

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProfile(UserLogin.this, view);
                view.setBackgroundResource(R.drawable.on_button_pressed);
            }
        });





        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                enterPassword(UserLogin.this,position,users.get(position), view);
            }
        });

        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteUser(UserLogin.this,i,users.get(i),view);
                return true;
            }
        });

    }

    private void createProfile(final Context context, final View v){
        final View view = getLayoutInflater().inflate(R.layout.fragment_create_new_user,null);
        final EditText User  = view.findViewById(R.id.editUsername);
        final EditText Password  = view.findViewById(R.id.editPassword);
        final EditText PassCheck  = view.findViewById(R.id.editCheckPassword);



        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("Create New Profile")
                .setView(view)
                .setMessage("Fill Out The Following Fields")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {


                        String username = User.getText().toString();
                        String tableName = username.replace(" ","_");
                        String password = Password.getText().toString();
                        String passwordCheck = PassCheck.getText().toString();
                        if(username.length() <1){
                            Toast.makeText(getApplicationContext(),"Need To Input A Username",Toast.LENGTH_LONG).show();
                            v.setBackgroundResource(R.drawable.my_border);
                        }else if(password.length()<1 || passwordCheck.length() < 1){
                            Toast.makeText(getApplicationContext(),"Need To Input A Password",Toast.LENGTH_LONG).show();
                            v.setBackgroundResource(R.drawable.my_border);
                        }else if(password.equals(passwordCheck)){

                            addUserData(db,UserLogin.this,tableName,password);
                            startActivity(new Intent(UserLogin.this,UserLogin.class));
                        }else{

                            Toast.makeText(UserLogin.this,"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
                            v.setBackgroundResource(R.drawable.my_border);

                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        v.setBackgroundResource(R.drawable.my_border);
                        dialog.cancel();
                    }
                });
        builder.show();

    }

    private void addUserData(TripDatabaseHelper db, Context context, String username, String password){
        boolean makeUser = true;
        if(users.size()>0) {
            for (int i = 0; i < users.size(); i++) {
                if (username.equals(users.get(i))) {
                    Toast.makeText(UserLogin.this, "User Already Exists", Toast.LENGTH_LONG).show();
                    makeUser = false;
                    break;
                } else {
                    makeUser = true;
                }
            }
            if(makeUser){
                Boolean isInserted = db.insertUserData(username, password);
                if (isInserted) {
                    db.createUserTable(username);
                    Toast.makeText(context, "User Successfully Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error Adding User", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
        Boolean isInserted = db.insertUserData(username, password);
        if (isInserted) {
             db.createUserTable(username);
             Toast.makeText(context, "User Successfully Added", Toast.LENGTH_SHORT).show();
        } else {
             Toast.makeText(context, "Error Adding User", Toast.LENGTH_SHORT).show();
             }
        }

    }

    private void deleteUser(final Context context, final Integer position, final String username, final View v) {

        v.setBackgroundResource(R.drawable.on_delete_press);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        //.getVisibleRegion().latLngBounds;
        final EditText tripNameEdit = new EditText(context);

        tripNameEdit.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        tripNameEdit.setHint("Password");



        // Build the dialog box
        builder.setTitle("Delete")
                .setView(tripNameEdit)
                .setMessage("Enter User Password To Delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Require a region name to begin the download.
                        // If the user-provided string is empty, display
                        // a toast message and do not begin download.
                        String password = tripNameEdit.getText().toString();
                        if (password.length() < 1) {
                            Toast.makeText(context, "Please Enter A Password", Toast.LENGTH_SHORT).show();

                        } else if (!password.equals(passwords.get(position))) {
                            Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                        } else {
                            DeleteDate(id.get(position).toString(),v,username);
                        }
                        v.setBackgroundResource(R.drawable.my_border);
                    }

                }).setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                v.setBackgroundResource(R.drawable.my_border);
            }
        });
        // Display the dialog
        builder.show();
    }



    private void enterPassword(final Context context, final Integer position, final String username, final View view){


        view.setBackgroundResource(R.drawable.on_button_pressed);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        //.getVisibleRegion().latLngBounds;
        final EditText tripNameEdit = new EditText(context);

        tripNameEdit.setRawInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        tripNameEdit.setHint("Password");


        // Build the dialog box
        builder.setTitle("Hello " + username)
                .setView(tripNameEdit)
                .setMessage("Please Enter Your Password")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Require a region name to begin the download.
                        // If the user-provided string is empty, display
                        // a toast message and do not begin download.
                        String password = tripNameEdit.getText().toString();
                        if (password.length() <1) {
                            Toast.makeText(context, "Please Enter A Password", Toast.LENGTH_SHORT).show();

                        } else if(!password.equals(passwords.get(position))) {
                            Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(context, Main2Activity.class));
                            selectedUser = username.replace("_"," ");
                            db.TABLE_NAME = username;
                            tableName = username;
                            Main2Activity.title = selectedUser+"'s Trip and Speed Tracker";

//                                // Begin download process
//                                int isUpdated = tripDB.updateData(idOfTrip.get(position), trip);
//                                if (isUpdated>0){
//
//                                    Toast.makeText(context, "Trip Name Updated", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(getActivity(), TravelHistoryPopup.class));
//                                }
//                                else {
//                                    Toast.makeText(getContext(), "Error Updating Trip Name", Toast.LENGTH_SHORT).show();
//
//                                }
                            }
                        view.setBackgroundResource(R.drawable.my_border);

                        }

                }).setNegativeButton(getString(R.string.dialog_negative_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        view.setBackgroundResource(R.drawable.my_border);
                    }
                });
        // Display the dialog
        builder.show();

    }

    public void DeleteDate(String ID,View v, String userName){

        Integer deletedRows = db.deleteUserData(ID);
        db.deleteUserTable(userName.replace(" ","_"));
        db.close();
        if(deletedRows>0){
//                    v.setBackgroundColor(Color.parseColor("#222222"));
//                    v.setEnabled(false);


            Toast.makeText(UserLogin.this, "User Successfully Deleted",Toast.LENGTH_SHORT).show();

            //listAdapter.//(tripList.get(position-1));

            listAdapter.notifyDataSetChanged();
            startActivity(new Intent(UserLogin.this,UserLogin.class));

        }else{

            Toast.makeText(UserLogin.this, "User Unsuccessfully Deleted",Toast.LENGTH_SHORT).show();
        }



    }

}
