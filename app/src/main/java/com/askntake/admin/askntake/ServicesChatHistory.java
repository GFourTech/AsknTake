package com.askntake.admin.askntake;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.ChatHistoryAdapter;
import AppUtils.AppConstants;
import AppUtils.ServiceHandler;
import Pojo.OwnerHistoryPojo;

/**
 * Created by admin on 4/6/2017.
 */

public class ServicesChatHistory extends AppCompatActivity  implements
        AdapterView.OnItemClickListener {
    WebView webView;
    String showpage = null;
    ArrayList<OwnerHistoryPojo> OwnerHistory;
    public static String UserIdFrom;
    //ImageView back_button,popup_icon;
    int position_clicked;
    ListView chathystory;
    String showType="product";
    String product_image_or_profile_img="Show profile picture";
    ChatHistoryAdapter chatAdapater ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buy_and_sell_chat_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chathystory=(ListView)findViewById(R.id.chathystory);

        OwnerHistory= new ArrayList<OwnerHistoryPojo>();
        Bundle b = getIntent().getExtras();
        if (b != null) {
            UserIdFrom= b.getString("ownerId");
        }
        registerReceiver(broadcastReceiver, new IntentFilter("CHAT_MESSAGE_RECEIVED_IN_HISTORY"));

        new  PopulateOwnerHistorysAsynchTask().execute();
        chathystory.setOnItemClickListener(this);
        chathystory.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chathystory.setStackFromBottom(false);
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle b = intent.getExtras();
            //String message = b.getString("message");
            //String productId=b.getString("productId");
            // UserIdFrom=b.getString("messageFrom");
            new ServicesChatHistory.PopulateOwnerHistorysAsynchTask().execute();
        }
    };

    private class PopulateOwnerHistorysAsynchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            ServiceHandler serviceHandler=new ServiceHandler();
            String response= serviceHandler.makeServiceCall(AppConstants.SERVICE_OWNER_CHAT_HISTORY,"POST",getRequestObject());
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response!=null) {
                OwnerHistory.clear();
                String message="";
                String messageDate="";
                String status="";
                String userName="";
                String productId="";
                String productName="";
                String productImage="";
                String profileImage="";
                String productColor="#000000";
                int last_messageId=0;
                try {
                    JSONObject mainobj= new JSONObject(response);
                    JSONArray historyArray= mainobj.getJSONArray("history");
                    for (int i = 0; i < historyArray.length(); i++) {
                        JSONObject obj= historyArray.getJSONObject(i);

                        JSONObject messageObj= obj.getJSONObject("lastMessage");
                        message=messageObj.getString("message");
                        messageDate=messageObj.getString("messageDate");
                        status=messageObj.getString("status");
                        last_messageId=messageObj.getInt("messageId");


                        productId=obj.getString("serviceId");
                        productName=obj.getString("serviceName");
                        productImage=obj.getString("serviceImage");

                        userName=obj.getString("userName");
                        String userId=obj.getString("userId");

                        profileImage=obj.getString("userImage");

                        productColor=obj.getString("serviceColor");
                        String pasroductColor=productColor;
                        int unread_messages=obj.getInt("unredMessages");
                      //ssss
                       /* if(unread_messages==0)
                        {
                            ShortcutBadger.removeCount(ChatHistory.this);
                        }*/



                        OwnerHistoryPojo pojo = new OwnerHistoryPojo();
                        pojo.setLastMessage(message);
                        pojo.setStatus(status);
                        pojo.setUserName(userName);
                        pojo.setUseId(userId);
                        pojo.setProductId(productId);
                        pojo.setProductName(productName);
                        pojo.setProductImage(productImage);
                        pojo.setProfile_image(profileImage);
                        pojo.setProductColor(productColor);
                        pojo.setUnreadmessages(unread_messages);
                        pojo.setLastmessageid(last_messageId);
                        OwnerHistory.add(pojo);

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    try {
                        JSONObject mainobj= new JSONObject(response);

                        JSONObject obj= mainobj.getJSONObject("history");

                        JSONObject messageObj= obj.getJSONObject("lastMessage");
                        message=messageObj.getString("message");
                        messageDate=messageObj.getString("messageDate");
                        status=messageObj.getString("status");
                        last_messageId=messageObj.getInt("messageId");


                        productId=obj.getString("serviceId");
                        productName=obj.getString("serviceName");
                        productImage=obj.getString("serviceImage");

                        userName=obj.getString("userName");
                        String userId=obj.getString("userId");

                        profileImage=obj.getString("userImage");
                        productColor=obj.getString("serviceColor");
                        int unread_messages=obj.getInt("unredMessages");
                       ///sssss
                        /*if(unread_messages==0)
                        {
                            ShortcutBadger.removeCount(ChatHistory.this);
                        }
*/

                        OwnerHistoryPojo pojo = new OwnerHistoryPojo();
                        pojo.setLastMessage(message);
                        pojo.setStatus(status);
                        pojo.setUserName(userName);
                        pojo.setUseId(userId);
                        pojo.setProductId(productId);
                        pojo.setProductName(productName);
                        pojo.setProductImage(productImage);
                        pojo.setProfile_image(profileImage);
                        pojo.setProductColor(productColor);
                        pojo.setUnreadmessages(unread_messages);
                        pojo.setLastmessageid(last_messageId);

                        OwnerHistory.add(pojo);
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
            if (OwnerHistory.size() > 0) {

              //  no_chat_layout.setVisibility(View.GONE);
                //chatLV.setVisibility(View.VISIBLE);
                chatAdapater = new ChatHistoryAdapter(ServicesChatHistory.this, OwnerHistory,"product");
                chathystory.setAdapter(chatAdapater);
            }
            else
            {
               // chatLV.setVisibility(View.GONE);
               // no_chat_layout.setVisibility(View.VISIBLE);
            }
        }

    }

    private JSONObject getRequestObject()
    {
        JSONObject jsonObject=null ;
        try {
            jsonObject = new JSONObject();
            jsonObject.accumulate("senderRegId", UserIdFrom);
            jsonObject.accumulate("reciverRegId", "");
            jsonObject.accumulate("serviceId", "");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  jsonObject;

    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int clickedPosition, long arg3) {
        OwnerHistoryPojo p = OwnerHistory.get(clickedPosition);
        position_clicked=clickedPosition;
        new sendReadInformationToServer(UserIdFrom,p.getUseId(),p.getLastmessageid(),p.getProductId()).execute();
    }
    private class sendReadInformationToServer extends AsyncTask<String, Void, String>
    {

        String senderId;
        String reciverId;
        int lastMessageId;
        String productId;
        public sendReadInformationToServer(String senderId,String reciverId,int lastMessageId,String productId) {
            this.senderId=senderId;
            this.reciverId=reciverId;
            this.lastMessageId=lastMessageId;
            this.productId=productId;
        }
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {

            JSONObject jsonObj=new JSONObject();
            String response="";
            try {
                jsonObj.accumulate("senderId",reciverId);
                jsonObj.accumulate("reciverId", senderId);
               // jsonObj.accumulate("lastMessageId", lastMessageId);
                jsonObj.accumulate("serviceId", productId);
                ServiceHandler serviceHandler=new ServiceHandler();
                response= serviceHandler.makeServiceCall(AppConstants.ServiceMessagesReadStatus(),"POST",jsonObj);
                JSONObject jsobj=new JSONObject(response);
                if(jsobj!=null)
                {
                    if(jsobj.has("status"))
                    {
                        if(jsobj.getBoolean("status"))
                        {
                            response="success";
                        }
                    }
                    else
                    {
                        response="fail";
                    }
                }

            } catch (Exception e) {
                response="exception";

            }



            return response;

        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result.equalsIgnoreCase("success"))
            {
                OwnerHistoryPojo p = OwnerHistory.get(position_clicked);
                //decrease clicked count in slide menu preference varaible

                Intent intent = new Intent(ServicesChatHistory.this, ChatActivityServices.class);
                intent.putExtra("userIdFrom",UserIdFrom);
                intent.putExtra("userIdTo", p.getUseId());
                intent.putExtra("itemId",p.getProductId());
                intent.putExtra("chattingToName",p.getUserName());
                intent.putExtra("itemName",p.getProductName());
                intent.putExtra("itemImage",p.getProductImage());
                startActivity(intent);
            }

        }

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("CHAT_MESSAGE_RECEIVED_IN_HISTORY"));
        new ServicesChatHistory.PopulateOwnerHistorysAsynchTask().execute();
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_history_menu_items, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            case R.id.show_popup_menu:
                // showPopup();

                View menuItemView = findViewById(R.id.show_popup_menu);
                final PopupMenu popup = new PopupMenu(ServicesChatHistory.this, menuItemView);

                //PopupMenu popup = new PopupMenu(ServiceDescriptionActivity_Services.this, popup_icon);
                popup.getMenuInflater().inflate(R.menu.chat_history_pop_up_menu, popup.getMenu());
                popup.getMenu().add(product_image_or_profile_img);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {




                        if(item.getTitle().toString().equalsIgnoreCase("Show profile picture")||item.getTitle().toString().equalsIgnoreCase("Show Service Image"))
                        {

                            if(showType.equalsIgnoreCase("product"))
                            {
                                showType="profile";
                                product_image_or_profile_img="Show Service Image";
                            }
                            else
                            {
                                showType="product";
                                product_image_or_profile_img="Show profile picture";
                            }

                            if(!OwnerHistory.isEmpty())
                            {
                                chathystory.setVisibility(View.VISIBLE);
                                chatAdapater = new ChatHistoryAdapter(ServicesChatHistory.this, OwnerHistory,showType);
                                chathystory.setAdapter(chatAdapater);
                            }
                            else
                            {
                                chathystory.setVisibility(View.GONE);
                               // no_chat_layout.setVisibility(View.VISIBLE);
                            }


                            //call adapter with images
                        }



                        //Toast.makeText(ItemDescriptionActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
