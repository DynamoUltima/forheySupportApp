package app.com.forheypanel.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

import app.com.forheypanel.model.NotificationDataModel;

/**
 * Created by nayram on 6/1/15.
 */
public class LocalDatabase {

    private static String DATABASE= "panelDB";
    private static String NOTIFICATION="tbl_notifictions";
    private static String ID="id";
    private static String TYPE="type";
    private static String ORDER_ID="order_id";
    private static String MESSAGE="message";
    private static String STATE="state";
    private static String USER_ID="user_id";
    private static String EMAIL="email";
    private static String NAME="name";
    private static String IMAGE="image";
    private static String TITLE="title";
    private static String CLIENT_ID="client_id";

    String TAG=getClass().getName();

    private static final int DATABASE_VERSION = 3;

    private DbHelper ourhelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper{

        public DbHelper(Context ctx){
            super(ctx,DATABASE,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create Table "+NOTIFICATION+" ("+ID+ " Integer Primary key autoincrement, "+TYPE+" text, "+ORDER_ID+" text, "+MESSAGE+" text, "
                    +STATE+" integer, "+USER_ID+" text, "+EMAIL+" text, "+NAME+" text, "+IMAGE+" text, "+CLIENT_ID+" INTEGER, "+TITLE+" text);");


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (newVersion){
                case 2:
                    try {
                        db.execSQL("ALTER TABLE " + NOTIFICATION + " ADD COLUMN " + NAME + " TEXT");
                    }catch (SQLiteException ex){
                        ex.printStackTrace();
                    }
                    try {
                        db.execSQL("ALTER TABLE "+NOTIFICATION+" ADD COLUMN "+IMAGE+" TEXT");
                    }catch (SQLiteException ex){
                        ex.printStackTrace();
                    }

                    try{
                        db.execSQL("ALTER TABLE "+NOTIFICATION+" ADD COLUMN "+EMAIL+" TEXT");
                    }catch (SQLiteException ex){
                        ex.printStackTrace();
                    }

                    break;
                case 3:
                    try{
                        db.execSQL("ALTER TABLE "+NOTIFICATION+" ADD COLUMN "+CLIENT_ID+" INTEGER");
                    }catch (SQLException ex){
                        ex.printStackTrace();
                    }
                    try {
                        db.execSQL("ALTER TABLE " + NOTIFICATION + " ADD COLUMN " + NAME + " TEXT");
                    }catch (SQLiteException ex){
                        ex.printStackTrace();
                    }
                    try {
                        db.execSQL("ALTER TABLE "+NOTIFICATION+" ADD COLUMN "+IMAGE+" TEXT");
                    }catch (SQLiteException ex){
                        ex.printStackTrace();
                    }

                    try{
                        db.execSQL("ALTER TABLE "+NOTIFICATION+" ADD COLUMN "+EMAIL+" TEXT");
                    }catch (SQLiteException ex){
                        ex.printStackTrace();
                    }

            }
        }
    }

    public LocalDatabase (Context ctx){
        ourContext=ctx;
    }

    public LocalDatabase open(){
        ourhelper= new DbHelper(ourContext);
        ourDatabase=ourhelper.getWritableDatabase();
        return this;
    }

    public void close(){
        ourhelper.close();
    }

    public long addNotification(String type,String OrderID,String message,int state,String user_id,String title){
        ContentValues contentValues=new ContentValues();
        contentValues.put(TYPE,type);
        contentValues.put(ORDER_ID,OrderID);
        contentValues.put(MESSAGE,message);
        contentValues.put(STATE, state);
        contentValues.put(USER_ID, user_id);
        contentValues.put(TITLE, title);
        return ourDatabase.insert(NOTIFICATION,null,contentValues);

    }

    public long addHeyGirlCheckIn(String type,String email,String name,String image,String title,String message,String user_id){
        ContentValues contentValues=new ContentValues();
        contentValues.put(TYPE,type);
        contentValues.put(EMAIL,email);
        contentValues.put(NAME,name);
        contentValues.put(IMAGE,image);
        contentValues.put(TITLE,title);
        contentValues.put(MESSAGE,message);
        contentValues.put(USER_ID,user_id);
        return ourDatabase.insert(NOTIFICATION,null,contentValues);
    }

    public long addNewSignUp(String type,String title,String message,int client_id,String user_id){
        ContentValues contentValues=new ContentValues();
        contentValues.put(TYPE,type);
        contentValues.put(TITLE,title);
        contentValues.put(MESSAGE,message);
        contentValues.put(CLIENT_ID,client_id);
        contentValues.put(USER_ID,user_id);
        return ourDatabase.insert(NOTIFICATION,null,contentValues);
    }

    public void changeState(int state,int id){
        ContentValues contentValues=new ContentValues();
        contentValues.put(STATE,state);
        ourDatabase.update(NOTIFICATION, contentValues, ID + " =? ", new String[]{String.valueOf(id)});
    }

    public long updateNotification(String orderId,String message,int state,String type,String title){
        ContentValues contentValues=new ContentValues();
        contentValues.put(TYPE,type);
        contentValues.put(MESSAGE,message);
        contentValues.put(STATE, state);
        contentValues.put(TITLE, title);
        return ourDatabase.update(NOTIFICATION, contentValues, ID + " =? ", new String[]{orderId});
    }

   public ArrayList<NotificationDataModel> getAllNotifications(String user_id){
       ArrayList<NotificationDataModel> dataModels=new ArrayList<NotificationDataModel>();
       String[] column=new String[]{ID,MESSAGE,STATE,TYPE,ORDER_ID,TITLE,NAME,IMAGE,EMAIL,CLIENT_ID};
       Cursor cursor=ourDatabase.query(NOTIFICATION,column,USER_ID+" =? ",new String[]{user_id},null,null,null);
       int id=cursor.getColumnIndex(ID);
       int type=cursor.getColumnIndex(TYPE);
       int message=cursor.getColumnIndex(MESSAGE);
       int state=cursor.getColumnIndex(STATE);
       int order_id=cursor.getColumnIndex(ORDER_ID);
       int title=cursor.getColumnIndex(TITLE);
       int name=cursor.getColumnIndex(NAME);
       int email=cursor.getColumnIndex(EMAIL);
       int image=cursor.getColumnIndex(IMAGE);
       int client_img=cursor.getColumnIndex(CLIENT_ID);
       for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
           Log.e(TAG,"get from db "+cursor.getString(name));
           dataModels.add(new NotificationDataModel(cursor.getString(message),cursor.getString(order_id),cursor.getInt(id),cursor.getInt(state),cursor.getString(type),
                   cursor.getString(title),cursor.getString(email),cursor.getString(image),cursor.getString(name),cursor.getInt(client_img)));

       }

      return dataModels;
   }

    public int countList(){
        String sql = "SELECT COUNT(*) FROM " + NOTIFICATION+" WHERE state = "+0;
        SQLiteStatement statement = ourDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return ((int) count);
    }

    public NotificationCompat.InboxStyle getNotificationList(String user_id){
        NotificationCompat.InboxStyle notiStyle = new NotificationCompat.InboxStyle();
        String[] column=new String[]{MESSAGE,TITLE};
        Cursor cursor=ourDatabase.query(NOTIFICATION,column,USER_ID+" =? and "+STATE+" =?",new String[]{user_id,String.valueOf(0)},null,null,null);
        int msg=cursor.getColumnIndex(MESSAGE);
        int title=cursor.getColumnIndex(TITLE);
        for (cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()){
            notiStyle.addLine(cursor.getString(title)+"  "+cursor.getString(msg));
        }
        return notiStyle;
    }

    public boolean notificationExists(String server_code){
        boolean result =false;
        String column[]=new String[]{ORDER_ID};

        Cursor cursor=ourDatabase.query(NOTIFICATION, column, ORDER_ID + " = ? ", new String[]{server_code}, null, null, null);
        int serverId=cursor.getColumnIndex(ORDER_ID);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            if (cursor.getString(serverId).contains(server_code)){
                result=true;
                return result;
            }
        }
        return result;
    }



    public int clearNotifications(){
       return ourDatabase.delete(NOTIFICATION,"1",null);

    }


}
