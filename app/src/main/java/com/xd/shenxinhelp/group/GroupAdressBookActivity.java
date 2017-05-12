package com.xd.shenxinhelp.group;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.ContactAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.HttpUtil;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.ResponseHandler;
import com.xd.shenxinhelp.model.Contact;
import com.xd.shenxinhelp.model.GroupDetail;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.xd.shenxinhelp.com.xd.shenxinhelp.ui.AdressBookActivity.getAllPhoneContacts;

public class GroupAdressBookActivity extends AppCompatActivity {

    private String userID;
    private SharedPreferences sp;
    private ListView lv_contact;
    private GroupDetail groupDetail;
    private Map<String,Contact> selfContact=null;
    static  Map<String,Contact> allRegistedPeopleMap = new HashMap<>();
    private Map<String,Contact> myAddedContact=new HashMap<>();//已经关联的账号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_adress_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        sp=getSharedPreferences("ShenXinBang", Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");

        groupDetail= (GroupDetail) getIntent().getSerializableExtra("detail");

        lv_contact = (ListView)findViewById(R.id.lv_contact_list);
        selfContact=getAllPhoneContacts(this);

        getAllRegistedPeople();
    }

    //获取已经注册身心帮账号的人
    private void getAllRegistedPeople(){
        OkHttp.get(AppUtil.GetRegisteredShenxinbangFriends + "?search_type=" + "0", new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("www",str);
            }

            @Override
            public void onResponse(String str) {
                try{
                    getGroupMember();
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        JSONArray ja = jsonObject.getJSONArray("telephones");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jb = ja.getJSONObject(i);
                            Contact contact = new Contact();
                            contact.setTel(jb.getString("phone_numer"));
                            contact.setName(jb.getString("name"));
                            contact.setIsAdded("0");
                            allRegistedPeopleMap.put(jb.getString("phone_numer"),contact);
                        }
                        //dismissRequestDialog();
                    }
                    else {
                        //dismissRequestDialog();
                        Log.e("Fail", jsonObject.getString("message"));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    //获取圈子里已经有的小伙伴
    public void getGroupMember() {
        //String urlget =  AppUtil.GetRingMember  + "?ringID="+groupDetail.getId()+"&type="+groupDetail.getType()+"&top=100";
            OkHttp.get(AppUtil.GetRingMember  + "?ringID="+groupDetail.getId()+"&type="+groupDetail.getType()+"&top=100", new OkHttp.ResultCallBack() {
                @Override
                public void onError(String str, Exception e) {
                    //dismissRequestDialog();
                    Log.e("getGroupMemberList", str);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(String str) {
                    JSONArray myGroupMembers=null;

                    try {
                        Log.e("analyzeGroupMembersList",str);
                        JSONObject jsonObject = new JSONObject(str);
                        String reCode = jsonObject.getString("reCode");
                        if ("SUCCESS".equals(reCode)){
                            myGroupMembers = jsonObject.getJSONArray("students");

                            for (int i = 0; i < myGroupMembers.length(); i++) {
                                JSONObject jb = myGroupMembers.getJSONObject(i);
                                Contact contact = new Contact();
                                contact.setTel(jb.getString("account"));
                                contact.setName(jb.getString("name"));
                                contact.setIsAdded("1");
                                myAddedContact.put(jb.getString("account"),contact);
                            }
                            //dismissRequestDialog();

                            Map<String,Contact> registedContactMap=new HashMap<>();//通讯录联系人中已经注册身心帮的
                            for (String phone:selfContact.keySet()){
                                Contact contact=new Contact();
                                if (allRegistedPeopleMap.containsKey(phone)){
                                    contact.setTel(selfContact.get(phone).getTel());
                                    contact.setName(selfContact.get(phone).getName());
                                    if(myAddedContact.containsKey(phone)){
                                        contact.setIsAdded("1");
                                    }else
                                        contact.setIsAdded("0");
                                }
                                registedContactMap.put(phone,contact);
                            }
                            Map<String,Contact> registedContactMap1=new HashMap<String, Contact>();
                            for(String phone:registedContactMap.keySet()){
                                if(registedContactMap.get(phone).getName()!=null){
                                    registedContactMap1.put(registedContactMap.get(phone).getName(),registedContactMap.get(phone));
                                }

                            }

                            final List<Contact> listValue = new ArrayList();
                            Iterator it = registedContactMap1.keySet().iterator();
                            while (it.hasNext()) {
                                String key = it.next().toString();
                                Contact contact1=new Contact();
                                contact1.setName(registedContactMap1.get(key).getName());
                                contact1.setTel(registedContactMap1.get(key).getTel());
                                contact1.setIsAdded(registedContactMap1.get(key).getIsAdded());
                                listValue.add(contact1);
                            }
                            Collections.sort(listValue);
                            ContactAdapter.ListBtnListener mListener = new ContactAdapter.ListBtnListener() {
                                @Override
                                public void myOnClick(int position, View v) {
                                    Intent intent=new Intent();
                                    String data=listValue.get(position).getTel();;
                                    intent.putExtra("tel",data);
                                    Log.e("www","data:"+data);
                                    setResult(153, intent);
                                    finish();
                                }
                            };
                            lv_contact.setAdapter(new ContactAdapter(getApplicationContext(),listValue,mListener));
                            if(listValue.size()<=0){
                                Toast.makeText(getApplicationContext(),"您当前还没有好友注册身心帮",Toast.LENGTH_LONG).show();
                            }


                        }
                        else {
                            //dismissRequestDialog();
                            Log.e("Fail", jsonObject.getString("message"));
                        }
                    }
                    catch (Exception e){
                        //dismissRequestDialog();
                        e.printStackTrace();
                    }
                }
            });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
