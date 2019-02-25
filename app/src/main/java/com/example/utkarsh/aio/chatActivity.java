package com.example.utkarsh.aio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;

public class chatActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private static int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_chat;

    @Override
    public void onResume() {
        super.onResume();}

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SIGN_IN_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Snackbar.make(activity_chat,"Successfully Signed In:welcome",Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else{
                Snackbar.make(activity_chat,"couldn't signin",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chat,"You have Signed Out",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activity_chat=(RelativeLayout)findViewById(R.id.activity_chat);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else{
            Snackbar.make(activity_chat,"welcome"+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            displayChatMessage();
        }
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input=(EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });

    }

    private void displayChatMessage() {
        ListView listOfMessage=(ListView)findViewById(R.id.list_of_messages);
        adapter=new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText,messageUser,messageTime;
                messageText=(TextView)v.findViewById(R.id.message_text);
                messageUser=(TextView)v.findViewById(R.id.message_user);
                messageTime=(TextView)v.findViewById(R.id.message_text);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));

            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
