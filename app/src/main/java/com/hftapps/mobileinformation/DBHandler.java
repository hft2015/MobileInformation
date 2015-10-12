package com.hftapps.mobileinformation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {



    private static final String DB_NAME = "mobileinfo";


    public DBHandler(Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Cr_OP = "create table op (id integer primary key autoincrement,op_id integer,op_name text,sim_type text,logo text);";
        String Cr_CR = "create table cr (id integer primary key autoincrement,cr_id integer,cr_name text);";
        String Cr_RQ = "create table rq (id integer primary key autoincrement,mobile text,op_id integer,cr_id integer);";

        db.execSQL(Cr_OP);
        db.execSQL(Cr_CR);
        db.execSQL(Cr_RQ);

        DefaultContent(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP IF TABLE EXISTS op");
        db.execSQL("DROP IF TABLE EXISTS cr");
        db.execSQL("DROP IF TABLE EXISTS rq");

    }

    private void DefaultContent(SQLiteDatabase db){
        // Insert OP Values

        String op_values = "insert into op (op_id,op_name,sim_type,logo) values " +
                "(1,'Airtel','GSM','airtel'), " +
                "(2,'Vodafone','GSM','vodafone'), " +
                "(3,'BSNL','GSM','bsnl'), " +
                "(4,'Reliance','CDMA','reliance'), " +
                "(5,'Reliance','GSM','reliance'), " +
                "(6,'Aircel','GSM','aircel'), " +
                "(8,'Idea','GSM','idea'), " +
                "(9,'TATA Indicom','GSM','tata'), " +
                "(10,'LOOP Mobile','GSM','loop'), " +
                "(11,'TATA Docomo','GSM','docomo'), " +
                "(12,'VIRGIN','CDMA','virgin'), " +
                "(13,'MTS','GSM','mts'), " +
                "(14,'Virgin','GSM','virgin'), " +
                "(15,'S Tel','GSM','stel'), " +
                "(16,'Uninor','GSM','uninor'), " +
                "(17,'Videocon','GSM','videocon'), " +
                "(999,'Not Available','NONE','no-logo'), " +
                "(18,'','GSM','mts'); " ;

        String cr_values = "insert into cr (cr_id,cr_name) values " +
                "(1,'Andhra Pradesh'), " +
                "(2,'Assam'), " +
                "(3,'Bihar & Jharkhand'), " +
                "(4,'Chennai'), " +
                "(5,'Delhi'), " +
                "(6,'Gujarat'), " +
                "(7,'Haryana'), " +
                "(8,'Himachal Pradesh'), " +
                "(9,'Jammu & Kashmir'), " +
                "(10,'Karnataka'), " +
                "(11,'Kerala'), " +
                "(12,'KolKata'), " +
                "(13,'Maharashtra & Goa'), " +
                "(14,'Madhya Pradesh'), " +
                "(15,'Mumbai'), " +
                "(16,'North East'), " +
                "(17,'Orisa'), " +
                "(18,'Punjab'), " +
                "(19,'Rajasthan'), " +
                "(20,'Tamil nadu'), " +
                "(21,'Uttar Pradesh - East'), " +
                "(22,'Uttar Pradesh - West'), " +
                "(999,'Not Avaliable'), " +
                "(23,'West Bengal'); " ;

        db.execSQL(op_values);
        db.execSQL(cr_values);


    }

    public String[] getRequestedDetails(String mobile,String op,String cr){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("mobile", mobile);
        contentValues.put("op_id", Integer.parseInt(op));
        contentValues.put("cr_id", Integer.parseInt(cr));
        db.insert("rq", null, contentValues);




        Cursor cursor=db.rawQuery("select op_name,cr_name,sim_type,logo from op,cr where op_id=" + op + " and cr_id=" + cr, null);

        String result[] = {" "," "," "," "};

        if(cursor.moveToFirst()){
            do{
                result[0] = cursor.getString(0);
                result[1] = cursor.getString(1);
                result[2] = cursor.getString(2);
                result[3] = cursor.getString(3);
            }while(cursor.moveToNext());
        }

        db.close();
        return result;
    }

    public List<Requests> getAllRequests() {

        List<Requests> requestList = new ArrayList<Requests>();

        String selectQuery = "select rq.id,mobile,op_name,cr_name,sim_type,logo from op,cr,rq where rq.op_id = op.op_id and rq.cr_id = cr.cr_id;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Requests request=new Requests(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
                );
                requestList.add(request);

            } while (cursor.moveToNext());
        }

        return requestList;
    }

}