/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nustana;

import backendless.BackendlessException;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Abdul Rahman
 */
public class ProfileInfo {
    public static final String TABLE = "Users";
    private String profileId;
    private String email;
    private String name;
    private String phoneNumber;
    private String userStatus;
    public ProfileInfo(String profileId){
        this.profileId = profileId;
    }
    public ProfileInfo(String profileId,String email,String name,String phoneNumber){
        this(profileId);
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    public ProfileInfo(JSONObject obj){
        profileId = obj.getString("objectId");
        email = obj.getString("email");
        name = obj.getString("name");
        phoneNumber = obj.getString("phoneNumber");
        userStatus = obj.getString("userStatus");
    }
    public String getProfileId(){
        return profileId;
    }
    public String getEmail(){
        return email;
    }
    public String getName(){
        return name;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public String getUserStatus(){
        return userStatus;
    }
    public void setProfileId(String id){
        this.profileId = id;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setUserStatus(String status){
        this.userStatus = status;
    }
    public void Save() throws IOException, BackendlessException{
        JSONObject obj = new JSONObject();
        obj.put("email", email);
        obj.put("name", name);
        obj.put("phoneNumber", phoneNumber);
        NUSTANA.getClient().UpdateObject(TABLE, obj, profileId);
    }
    public void Load() throws IOException, BackendlessException{
        JSONObject obj = NUSTANA.getClient().GetObject(TABLE, profileId);
        email = obj.getString("email");
        name = obj.getString("name");
        phoneNumber = obj.getString("phoneNumber");
        userStatus = obj.getString("userStatus");
    }
    @Override
    public String toString() {
        return name;
    }
    private static ProfileInfo[] ProcessProfiles(JSONArray objects){
        ProfileInfo[] profiles = new ProfileInfo[objects.length()];
        for(int i = 0; i < objects.length();i++){
            JSONObject obj = objects.getJSONObject(i);
            profiles[i] = new ProfileInfo(obj);
        }
        return profiles;
    }
    public static ProfileInfo[] GetProfiles() throws IOException, BackendlessException{
        JSONArray objects = NUSTANA.getClient().GetObjects(TABLE);
        return ProcessProfiles(objects);
    }
}