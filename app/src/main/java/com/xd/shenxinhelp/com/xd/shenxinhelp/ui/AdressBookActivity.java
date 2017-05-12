package com.xd.shenxinhelp.com.xd.shenxinhelp.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xd.shenxinhelp.R;
import com.xd.shenxinhelp.adapter.ContactAdapter;
import com.xd.shenxinhelp.com.xd.shenxinhelp.httpUtil.AppUtil;
import com.xd.shenxinhelp.model.Contact;
import com.xd.shenxinhelp.netutils.OkHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AdressBookActivity extends AppCompatActivity {

    private String userID;
    private SharedPreferences sp;
    private ListView lv_contact;
    private Map<String,Contact> selfContact=null;
    static  Map<String,Contact> allRegistedPeopleMap = new HashMap<>();
    private Map<String,Contact> myAddedContact=new HashMap<>();//已经关联的账号
    //private Map<String,Contact> allRegistedPeople= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        sp=getSharedPreferences("ShenXinBang",Context.MODE_PRIVATE);
        userID = sp.getString("userid", "1");

        lv_contact = (ListView)findViewById(R.id.lv_contact_list);
        selfContact=getAllPhoneContacts(this);

        getAllRegistedPeople();
    }

    //获取手机通讯录的联系人
    public static Map<String,Contact> getAllPhoneContacts(Context context){
        Map<String,Contact> selfContactsMap= new HashMap<>();
        int id = -1;
        ContentResolver cr = context.getContentResolver();
        String[] mContactsProjection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID
        };

        String contactsId;
        String phoneNum;
        String name;
        long photoId;
        byte[] photoBytes = null;

        //查询contacts表中的所有数据
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                contactsId = cursor.getString(0);
                phoneNum = cursor.getString(1);
                name = cursor.getString(2);
                photoId = cursor.getLong(3);

                /*if(photoId > 0){//有头像
                    Cursor cursorPhoto = cr.query(ContactsContract.RawContactsEntity.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                            ContactsContract.RawContactsEntity.CONTACT_ID + " = ? and " + ContactsContract.RawContactsEntity.MIMETYPE + " = ? and " + ContactsContract.RawContactsEntity.DELETED + " = ?",
                            new String[]{contactsId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, "0"},
                            null);
                    if(cursorPhoto.moveToNext()){
                        photoBytes = cursorPhoto.getBlob(0);
                    }
                    cursorPhoto.close();
                }else{
                    photoBytes = null;
                }*/

                // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
                phoneNum = phoneNum.replaceAll("^(\\+86)", "");
                phoneNum = phoneNum.replaceAll("^(86)", "");
                phoneNum = phoneNum.replaceAll("-", "");
                phoneNum = phoneNum.replaceAll(" ", "");
                phoneNum = phoneNum.trim();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phone",phoneNum);
                map.put("name",name);
                Contact contact = new Contact();
                contact .setName(name);
                contact .setTel(phoneNum);
                contact.setIsAdded("0");
                //contact .setId(String.valueOf(id));
                //contact .setContactPhoto(photoBytes);
                //contact .setModelType(2);
                selfContactsMap.put(phoneNum,contact);
                id -= 1;
            }
        }
        cursor.close();
        return selfContactsMap;
    }

    //获取已经注册身心帮账号的人
    private void getAllRegistedPeople(){
        //final  Map<String,Contact> registedPeopleMap = new HashMap<>();
        OkHttp.get(AppUtil.GetRegisteredShenxinbangFriends + "?search_type=" + "1", new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                Log.e("www",str);
            }

            @Override
            public void onResponse(String str) {
                try{
                    getRelationshipList();
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

    //获取已经添加的亲密关系
    private void getRelationshipList(){

        OkHttp.get(AppUtil.GetMyAllRelationship + "?studentID="+userID, new OkHttp.ResultCallBack() {
            @Override
            public void onError(String str, Exception e) {
                //dismissRequestDialog();
                Log.e("getRelationshipList", str);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String str) {
                JSONArray myRelationships=null;

                try {
                    Log.e("analyzeRelationshipList",str);
                    JSONObject jsonObject = new JSONObject(str);
                    String reCode = jsonObject.getString("reCode");
                    if ("SUCCESS".equals(reCode)){
                        myRelationships = jsonObject.getJSONArray("myRelationships");

                        for (int i = 0; i < myRelationships.length(); i++) {
                            JSONObject jb = myRelationships.getJSONObject(i);
                            Contact contact = new Contact();
                            contact.setTel(jb.getString("parentAccount"));
                            contact.setName(jb.getString("parentName"));
                            contact.setIsAdded("1");
                            myAddedContact.put(jb.getString("parentAccount"),contact);
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
                            //listValue.add(registedContactMap.get(key).);
                            Contact contact1=new Contact();
                            contact1.setName(registedContactMap1.get(key).getName());
                            contact1.setTel(registedContactMap1.get(key).getTel());
                            contact1.setIsAdded(registedContactMap1.get(key).getIsAdded());
                            listValue.add(contact1);
                        }

                        ContactAdapter.ListBtnListener mListener = new ContactAdapter.ListBtnListener() {
                            @Override
                            public void myOnClick(int position, View v) {
                               // Constants.TEL=listValue.get(position).getTel();
                                Intent intent=new Intent();
                                String data=listValue.get(position).getTel();;
                                intent.putExtra("tel",data);
                                setResult(152, intent);
                                finish();
                            }
                        };
                        lv_contact.setAdapter(new ContactAdapter(getApplicationContext(),listValue,mListener));
                        if(listValue.size()<=0){
                            Toast.makeText(getApplicationContext(),"您当前还没有亲友注册身心帮",Toast.LENGTH_LONG).show();
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
