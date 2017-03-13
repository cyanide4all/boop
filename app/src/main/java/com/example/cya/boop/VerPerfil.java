package com.example.cya.boop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cya.boop.core.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VerPerfil extends AppCompatActivity {

    private Button botonLogout;
    private TextView bio;
    private TextView nombre;
    private TextView edad;
    private DatabaseReference mDatabase;
    private Usuario user;
    private String idUser;
    private boolean esMiPerfil;
    private Button botonEditarPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        //Si le viene un userID en los extras del intent, muestra los datos de ese senior, si no los del propio usuario actual
        if(getIntent().getExtras()==null) {
            //Hemos entrado desde nuestro propio boton de ver perfil
            //Getteamos id del usuario actual
            idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //Obviamente estamos en nuestro perfil
            esMiPerfil = true;
        }else{
            //Hemos entrado desde un boop u otro sitio ajeno...
            idUser = getIntent().getExtras().getString("userID");
            //Debemos chekear si estamos en nuestro propio perfil
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(idUser)) {
                esMiPerfil = true;
            }else{
                esMiPerfil = false;
            }
        }

        //Firebasin'
        mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios").child(idUser);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Aqui se meten en la vista las cosas que vienen de la BD
                user = dataSnapshot.getValue(Usuario.class);
                if(user != null){
                    bio = (TextView) findViewById(R.id.VPbio);
                    bio.setText(user.getBio());

                    nombre = (TextView) findViewById(R.id.VPnombre);
                    nombre.setText(user.getNombre());

                    edad = (TextView) findViewById(R.id.VPedad);
                    //TODO Tenemos fecha de nacimiento, conseguimos edad
                    //edad.setText();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("VerPerfil", "onCreateValueEventListener:onCancelled", databaseError.toException());

            }
        });

        botonLogout = (Button) findViewById(R.id.VPlogout);
        botonEditarPerfil = (Button) findViewById(R.id.VPmodificarPerfil);

        if(esMiPerfil) {
            botonLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
            botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(VerPerfil.this, CrearPerfil.class));
                    //TODO ENORME AQUI, ESTO ES PROVISIONAL, puede que no funque y ademas debe hacer cosas al volver y tal
                }
            });
        }else{
            botonLogout.setVisibility(View.INVISIBLE);
            botonEditarPerfil.setVisibility(View.INVISIBLE);
        }

        TextView nombre = (TextView) findViewById(R.id.VPnombre);

    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
