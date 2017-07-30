package com.messedup.saurabh.mess2;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

/**
 * Created by saurabh on 23/4/17.
 */

public class Users {


    private String college;
    private String contact;
    private String email;
    private String endsub;
    private String groupid;
    private String name;
    private String qrcode;
    private String scannedlunch;
    private String scanneddinner;
    private String uid;
    public final HashMap<String, Object> result = new HashMap<>();

    public Users(String college, String contact, String email, String endsub, String groupid, String name, String qrcode, String scannedlunch, String scanneddinner, String uid) {
        this.college = college;
        this.contact = contact;
        this.email = email;
        this.endsub = endsub;
        this.groupid = groupid;
        this.name = name;
        this.qrcode = qrcode;
        this.scannedlunch = scannedlunch;
        this.scanneddinner = scanneddinner;
        this.uid = uid;



        toMap();
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndsub() {
        return endsub;
    }

    public void setEndsub(String endsub) {
        this.endsub = endsub;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getScannedlunch() {
        return scannedlunch;
    }

    public void setScannedlunch(String scannedlunch) {
        this.scannedlunch = scannedlunch;
    }

    public String getScanneddinner() {
        return scanneddinner;
    }

    public void setScanneddinner(String scanneddinner) {
        this.scanneddinner = scanneddinner;
    }

    public String getuid() {
        return uid;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }



   /* private String age;
    private String college;
    private String contact;
    private String email;
    private String groupid;
    private String name;
    private String qrcode;
    private String uid;






    public Users(String age, String college, String contact, String email, String groupid, String name, String qrcode,String uid) {
        this.age = age;
        this.college = college;
        this.contact = contact;
        this.email = email;
        this.groupid = groupid;
        this.name = name;
        this.qrcode = qrcode;
        this.uid=uid;


        toMap();
    }



    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public void setuid(String uid) {
        this.uid = uid;
    }
    public String getuid() {
        return uid;
    }

*/
    Users()
    {

    }
    Users(String uid)
    {
        this.uid=uid;
    }

    @Exclude

    public HashMap<String, Object> toMap()
    {

        result.put("uid", uid);
        result.put("name", name);
        result.put("groupid", groupid);
        result.put("email", email);
        result.put("contact", contact);
        result.put("college", college);
        result.put("qrcode",qrcode);
        result.put("endsub",endsub);
        result.put("scannedlunch",scannedlunch);
        result.put("scanneddinner",scanneddinner);



        return result;
    }



}
