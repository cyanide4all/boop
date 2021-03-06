package com.example.cya.boop.core;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cya on 2/22/17.
 */

public class Usuario implements Serializable {
    //Nombre completo
    private String nombre;
    //Pequeña presentación personal de rellenar perfil
    private String bio;
    //Fecha de nacimiento en principio para restringción de edad. Puede que luego se permita
    // ocultarlo para por si mujer subnormal
    private Date fechaNac;
    //Popularidad del usuario
    private int karma;
    //Radio de accion
    private int radio;
    //Lista de boops a los que asisto
    private ArrayList<String> boopsQueAsisto;

    //Constructor vacío por tocarle los huevos a oskaru
    public Usuario (){
        this.boopsQueAsisto = new ArrayList<>();
    }

    //
    //GETTERS & SETTERS (Algunos retornan excepciones al intentar settear imposibles
    //
    public String getBio() {
        return bio;
    }

    public int getRadio() {
        return radio;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public String getNombre() {
        return nombre;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    //Crea un perfil de usuario con clave idUsuario
    public void crear(String idUsuario) {
        //Firebaseamientos para funcar
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Usuarios");
        myRef.child(idUsuario).setValue(this);
    }

    public ArrayList<String> getBoopsQueAsisto() {
        return boopsQueAsisto;
    }

    public int getKarma() {return this.karma;}

    public void setKarma (int karma) {this.karma = karma;}

    public void incrementarKarma ()
    {
        this.karma += Boop.KARMA_VALUE;
    }

    public void decrementarKarma ()
    {
        this.karma -= Boop.KARMA_VALUE;
        //if(karma < 0)     //Todo vigilar si carma es muy bajo mirar de banear
        //    karma = 0;
    }

    public void asistir(String boopID){
        boopsQueAsisto.add(boopID);
    }

    public void noAsistir(String boopID){
        boopsQueAsisto.remove(boopsQueAsisto.indexOf(boopID));
    }

    public UploadTask uploadPhoto(Uri file_url, String key){
        // Usamos la clave para crear un nodo en storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://boop-4ec7a.appspot.com");
        StorageReference boopRef = storageRef.child("Booppeople/"+key);

        // subimos la imagen
        return boopRef.putFile(file_url);
    }

    public static Task<Uri> getDownloadPhoto(String key){
        // metodo mientras no se me ocurre como coger la propia key del objeto
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://boop-4ec7a.appspot.com");
        StorageReference boopRef = storageRef.child("Booppeople/"+key);
        return boopRef.getDownloadUrl();
    }
}
