package de.tg76.smartparking;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by adm_toto on 05/10/2015.
 */
public class ServerRequests {

    //Show loading bar when server request is executed
    ProgressDialog progressDialog;
    //Connection time in mill sec before disconnect
    public static final int CONNECTION_TIME = 1000*15;
    public static final String SERVER_ADDRESS = "http://52.17.188.91/";

    //Constructor
    public ServerRequests(Context context){
        //Instantiate dialog progress
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
    }

    //Storing user to DB
    public void storeUserDataInBackground(User user, GetUserCallback userCallback){
        //Start progress bar
        progressDialog.show();
        new StoreUserDataAsyncTask(user, userCallback).execute();
    }

    //Fetching user from DB
    public void fetchUserDataInBackground(User user, GetUserCallback callback){
        //Start progress bar
        progressDialog.show();
    }

    //Background task
    public class StoreUserDataAsyncTask extends AsyncTask<Void,Void, Void>{

        //Holds user data
        User user;
        //Interface GetuserCallback
        GetUserCallback userCallback;

        //Constructor
        public StoreUserDataAsyncTask(User user, GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        //Run in the background when StoreUserDataAsyncTask starts - Accessing the server
        protected Void doInBackground(Void... params) {
            //Data which are send to server
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("name", user.name));
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));
            dataToSend.add(new BasicNameValuePair("email", user.email));

            HttpParams httpRequestParams = new BasicHttpParams();
            //Time to wait before the post is executed
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            //Time to wait to receive anything from server
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            //Create client to establish HTTP connection
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            }catch (Exception e){
              e.printStackTrace();
            }

            return null;
        }

        @Override
        //When AsyncTask is finish
        protected void onPostExecute(Void aVoid) {
            //Stop progress bar displayed when AsyncTask is finish
            progressDialog.dismiss();
            userCallback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    //Returns a user
    public class fetchUserDataAsyncTask extends AsyncTask<Void,Void, User> {
        //Holds user data
        User user;
        //Interface GetuserCallback
        GetUserCallback userCallback;

        //Constructor
        public fetchUserDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {
            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("username", user.username));
            dataToSend.add(new BasicNameValuePair("password", user.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            //Time to wait before the post is executed
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIME);
            //Time to wait to receive anything from server
            HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIME);

            //Create client to establish HTTP connection
            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            User returndUser = null;
            try{
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                //Stores the data fetched from the database
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);
                //jObject holds the user data
                JSONObject jObject = new JSONObject(result);

                if(jObject.length()==0){
                    returndUser = null;
                }else {
                    String name = jObject.getString("name");
                    String email = jObject.getString("email");

                    returndUser = new User(name, email, user.username, user.password);

                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return returndUser;
        }

        @Override
        //When AsyncTask is finish - returns a user
        protected void onPostExecute(User returndUser) {
            //Stop progress bar displayed when AsyncTask is finish
            progressDialog.dismiss();
            //Send the returenduser to the done callback
            userCallback.done(returndUser);
            super.onPostExecute(returndUser);
        }
    }
}
