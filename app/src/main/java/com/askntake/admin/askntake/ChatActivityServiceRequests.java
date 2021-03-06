package com.askntake.admin.askntake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import Adapters.ChatListAdapter;
import AppUtils.AppConstants;
import AppUtils.DataKeyValues;
import AppUtils.ServiceHandler;
import AppUtils.Utils;
import Pojo.ChatPeople;


public class ChatActivityServiceRequests extends AppCompatActivity {


    public int defaultScrenWidth = 800, defaultScrenHeit = 1280;
    public int curScrenHeit;
    public int curScrenWidth;
    StringBuffer buffer;
    Utils utils;

    static String TAG = "GCM DEMO";
    String user_name;
    String userRegId;
    String chattingToName, userIdTo, itemId, productImageUrl, ProductName;

    public static String UserIdFrom;
    String API_KEY = "AIzaSyCpc36pOMJA7k6cxihPk7tycbpqi2eODdM";
    EditText edtMessage;
    ListView chatLV;

    ChatListAdapter chatAdapater;
    ArrayList<ChatPeople> ChatPeoples;
    ImageView back_button;
    TextView action_bar_main_heading;
    String productOwnerId, serviceRequestImage;
    String requestFrom, userMainId;
    boolean back_pressed;

    TextView chatting_text;
    ImageView image;
    View chat_product_header_include_in_chat_pge;

    String product_image_or_profile_img = "Show profile picture";
    String showType = "product";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        curScrenHeit = displaymetrics.heightPixels;
        curScrenWidth = displaymetrics.widthPixels;
        Bundle b = getIntent().getExtras();

        if (b != null) {
            user_name = b.getString("chattingFrom");
            chattingToName = b.getString("chattingToName");
            UserIdFrom = b.getString("userIdFrom");
            userIdTo = b.getString("userIdTo");
            itemId = b.getString("itemId");
            productImageUrl = b.getString("itemImage");
            ProductName = b.getString("itemName");

        }

        requestFrom = b.getString("requestFrom");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        chatting_text = (TextView) findViewById(R.id.chatting_text);


        SharedPreferences fbpref = AppConstants.preferencesData(getApplicationContext());
        userMainId = fbpref.getString(DataKeyValues.OWNER_ID, null);

        //send online status to server

        // new SendOnlineStatusToServer(userMainId, true).execute();


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        chat_product_header_include_in_chat_pge = (View) findViewById(R.id.chat_product_header_include_in_chat_pge);

        //Typeface typefont = Typeface.createFromAsset(getAssets(),"DaunPenh.ttf");
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 121 * curScrenHeit / defaultScrenHeit);
        //RelativeLayout header=(RelativeLayout)findViewById(R.id.inner_header);
        //header.setLayoutParams(rlp);
        image = (ImageView) findViewById(R.id.productImage);
        TextView productName = (TextView) findViewById(R.id.productName);

//		productImageUrl="http://10.0.0.20:8080/askntake/serviceimages/1_service1498628254923.png";

        if (productImageUrl != null) {
            if (!productImageUrl.equalsIgnoreCase("noimage")) {


                Glide.with(getApplicationContext()).load(productImageUrl).override(110, 100).into(image);


                /*Picasso.with(getApplicationContext()).load(productImageUrl)
                        .resize(110, 100)
                        .placeholder(R.drawable.progress_animation)
                        .into(image);*/
                //chat_product_header_include_in_chat_pge.setVisibility(View.VISIBLE);
                chat_product_header_include_in_chat_pge.setVisibility(View.VISIBLE);
            } /*else {

                Picasso.with(getApplicationContext()).load(serviceRequestImage)
                        .resize(110, 100)
                        .placeholder(R.drawable.progress_animation)
                        .into(image);
                chat_product_header_include_in_chat_pge.setVisibility(View.VISIBLE);
            }*/
        }


        edtMessage = (EditText) findViewById(R.id.editText_message);
        productName.setText(ProductName);




        Typeface typefont = Typeface.createFromAsset(getAssets(), "heading_font.ttf");
        View customView = getLayoutInflater().inflate(R.layout.activity_header_upload_page, null);
        action_bar_main_heading = (TextView) customView.findViewById(R.id.action_bar_main_heading);
        rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.addRule(RelativeLayout.RIGHT_OF, R.id.back_button);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        //action_bar_main_heading.setTypeface(typefont,Typeface.BOLD);
        action_bar_main_heading.setLayoutParams(rlp);
        action_bar_main_heading.setTextSize(15);
        action_bar_main_heading.setGravity(Gravity.CENTER_VERTICAL);


        back_button = (ImageView) customView.findViewById(R.id.back_button);
        rlp = new RelativeLayout.LayoutParams(125 * curScrenWidth / defaultScrenWidth, 96 * curScrenHeit / defaultScrenHeit);
        rlp.addRule(RelativeLayout.CENTER_VERTICAL);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        back_button.setLayoutParams(rlp);


        chat_product_header_include_in_chat_pge.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (userMainId != null) {
                    if (productOwnerId.equalsIgnoreCase(userMainId)) {
                        Intent myIntent = new Intent(getApplicationContext(), ServicesRequests_DescriptionActivity.class);
                        myIntent.putExtra("userId", userMainId);
                        myIntent.putExtra("service_id", itemId);
                        myIntent.putExtra("myownproduct", true);
                        startActivity(myIntent);
                    } else {

                        Intent myIntent = new Intent(getApplicationContext(), ServicesRequests_DescriptionActivity.class);
                        myIntent.putExtra("userId", productOwnerId);
                        myIntent.putExtra("service_id", itemId);
                        myIntent.putExtra("myownproduct", false);
                        startActivity(myIntent);
                        /*Intent myIntent=new Intent(getApplicationContext(),ItemDescriptionActivity.class);
                        myIntent.putExtra("userId", userMainId);
						startActivity(myIntent);*/
                    }

                }
            }
        });

        back_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                back_pressed = true;
                new SendOnlineStatusToServer(userMainId, false).execute();


            }
        });


        utils = new Utils(this);

        chatLV = (ListView) findViewById(R.id.listView1);


        ChatPeoples = new ArrayList<ChatPeople>();

        SharedPreferences prefs = AppConstants.preferencesData(getApplicationContext());

        UserIdFrom = prefs.getString(DataKeyValues.USER_USERID, null);

        userRegId = prefs.getString(Utils.PROPERTY_REG_ID, "");


        registerReceiver(broadcastReceiver, new IntentFilter(
                "SERVICE_MESSAGE_RECEIVED"));

        new PopulateMessagesAsynchTask().execute();

        chatLV.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatLV.setStackFromBottom(true);

        chatting_text.setText("Chatting to : " + chattingToName);
    }


    void clearMessageTextBox() {

        edtMessage.clearFocus();
        edtMessage.setText("");


    }

    private void hideKeyBoard(EditText edt) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }


    void getData() {

        ChatPeoples.clear();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            new PopulateMessagesAsynchTask().execute();
        }
    };


    ChatPeople addToChat(String personName, String chatMessage, String toOrFrom) {


        ChatPeople curChatObj = new ChatPeople();
        curChatObj.setPERSON_NAME(personName);
        curChatObj.setPERSON_CHAT_MESSAGE(chatMessage);
        curChatObj.setPERSON_CHAT_TO_FROM(toOrFrom);
        curChatObj.setPERSON_DEVICE_ID("");
        curChatObj.setPERSON_EMAIL("demo@gmail.com");

        return curChatObj;

    }

    public void onClick(final View view) {

        if (view == findViewById(R.id.send)) {

            sendToDb(UserIdFrom, userIdTo, edtMessage.getText().toString(), "Sent", itemId);
            clearMessageTextBox();


        }

    }

    public synchronized void sendToDb(final String UserIdFrom, final String userIdTo, final String messageToSend, final String sendOrRecieve, final String serviceId) {

        if (messageToSend.length() > 0) {

            Log.i(TAG, "sendMessage");

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        ServiceHandler serviceHandler = new ServiceHandler();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("senderId", UserIdFrom);
                        jsonObject.accumulate("recieverId", userIdTo);
                        jsonObject.accumulate("chatMessage", messageToSend.trim());
                        jsonObject.accumulate("sendOrRecieve", sendOrRecieve);
                        jsonObject.accumulate("ServicerequestId", serviceId);
                        String service_result = serviceHandler.makeServiceCall(AppConstants.BASE_URL
                                + "rest/servicerequestchat/saveServiceRequestMessage", "POST", jsonObject);


                        Log.i("responce", "" + service_result);
                        new PopulateMessagesAsynchTask().execute();

                    } catch (Exception e) {
                        Log.d(TAG, "Exception : " + e.getMessage());
                    }
                }
            };

            thread.start();

        }

    }

    private class PopulateMessagesAsynchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            String response = "";
            InputStream inputStream = null;
            try {
                ServiceHandler serviceHandler = new ServiceHandler();
                JSONObject jsonObject = new JSONObject();


                jsonObject.accumulate("senderRegId", UserIdFrom);
                jsonObject.accumulate("reciverRegId", userIdTo);
                jsonObject.accumulate("serviceRequestId", itemId);

                response = serviceHandler.makeServiceCall(AppConstants.BASE_URL
                        + "rest/servicerequestchat/getServiceRequestChatHistory", "POST", jsonObject);


                Log.i("responce", "" + response);
            } catch (Exception ex) {

                Log.d(TAG, "Error : " + ex.getMessage());

                runOnUiThread(new Runnable() {
                    public void run() {
                        utils.showToast("Server Not responding, Please check whether your server is running or not");
                    }
                });

            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                ChatPeoples.clear();
                try {
                    JSONObject mainobj = new JSONObject(response);
                    productOwnerId = mainobj.getString("serviceRequestOwnerId");
                    serviceRequestImage = mainobj.getString("serviceRequestImage");

                    Picasso.with(getApplicationContext()).load(serviceRequestImage)
                            .resize(110, 100)
                            .placeholder(R.drawable.progress_animation)
                            .into(image);
                    chat_product_header_include_in_chat_pge.setVisibility(View.VISIBLE);

                    if (mainobj.has("messages")) {
                        JSONArray mainArray = mainobj.getJSONArray("messages");
                        for (int i = 0; i < mainArray.length(); i++) {
                            JSONObject obj = mainArray.getJSONObject(i);

                            //String productOwnerId1=obj.getString("productOwnerId");
                            ChatPeople people = addToChat("",
                                    obj.getString("message"), obj.getString("sendOrReciev"));
                            ChatPeoples.add(people);
                        }
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    try {
                        JSONObject mainobj = new JSONObject(response);
                        productOwnerId = mainobj.getString("serviceRequestOwnerId");
                        serviceRequestImage = mainobj.getString("serviceRequestImage");

                        Picasso.with(getApplicationContext()).load(serviceRequestImage)
                                .resize(110, 100)
                                .placeholder(R.drawable.progress_animation)
                                .into(image);
                        chat_product_header_include_in_chat_pge.setVisibility(View.VISIBLE);
                        if (mainobj.has("messages")) {
                            JSONObject obj = mainobj.getJSONObject("messages");
                            ChatPeople people = addToChat("",
                                    obj.getString("message"), obj.getString("sendOrReciev"));
                            ChatPeoples.add(people);

                        }


                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }


            }
            if (ChatPeoples.size() > 0) {
                chatAdapater = new ChatListAdapter(ChatActivityServiceRequests.this, ChatPeoples);
                chatLV.setAdapter(chatAdapater);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        back_pressed = true;
        new SendOnlineStatusToServer(userMainId, false).execute();

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.chatting_menu, menu);
        menu.setGroupVisible(R.menu.chatting_menu,false);


        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                back_pressed = true;
                finish();
                return true;


           /* case R.id.show_popup_menu:
                // showPopup();
                View menuItemView = findViewById(R.id.show_popup_menu);
                menuItemView.setVisibility(View.GONE);
                PopupMenu popup = new PopupMenu(ChatActivityServiceRequests.this, menuItemView);

                //PopupMenu popup = new PopupMenu(ServiceDescriptionActivity_Services.this, popup_icon);
                popup.getMenuInflater().inflate(R.menu.chat_pop_up_menu, popup.getMenu());
                popup.getMenu().add(product_image_or_profile_img);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {


                        if (item.getTitle().toString().equalsIgnoreCase("Show profile picture") || item.getTitle().toString().equalsIgnoreCase("Show Product Image")) {
                            if (showType.equalsIgnoreCase("product")) {
                                showType = "profile";
                                product_image_or_profile_img = "Show Product Image";
                            } else {
                                showType = "product";
                                product_image_or_profile_img = "Show profile picture";
                            }

                            *//*if (!OwnerHistory.isEmpty()) {
                                chatLV.setVisibility(View.VISIBLE);
                                chatAdapater = new ChatHistoryAdapter(ChatHistory.this, OwnerHistory, showType);
                                chatLV.setAdapter(chatAdapater);
                            } else {
                                chatLV.setVisibility(View.GONE);
                                no_chat_layout.setVisibility(View.VISIBLE);
                            }*//*


                            //call adapter with images
                        }


                        //Toast.makeText(ItemDescriptionActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;

                    }


                });

                popup.show();
                return true;*/


        }
        return super.onOptionsItemSelected(item);

    }

    private class SendOnlineStatusToServer extends AsyncTask<String, Void, String> {

        String userMainId;
        boolean status;

        public SendOnlineStatusToServer(String userMainId, boolean status) {

            this.userMainId = userMainId;
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            ServiceHandler serviceHandler = new ServiceHandler();
            JSONObject requestObject = getJSONObject();
            String service_result = serviceHandler.makeServiceCall(AppConstants.SendOnlineStatus(), "POST", requestObject);

            return service_result;

        }

        private JSONObject getJSONObject() {
            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject();
                requestObject.accumulate("status", status);
                requestObject.accumulate("userId", userMainId);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return requestObject;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equalsIgnoreCase("success")) {


            } else {
                if (back_pressed) {
                    if (requestFrom != null) {
                        if (requestFrom.equalsIgnoreCase("description_scr")) {
                            Intent myIntent = new Intent(ChatActivityServiceRequests.this, ServiceDescriptionActivity_Services.class);
                            startActivity(myIntent);
                            finish();
                        } else {


                            finish();
                        }
                    } else {
                        finish();
                    }
                }
            }
        }

    }

}