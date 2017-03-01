package shu.fragmenttest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by eva on 2017/2/26.
 */

public class LocalDBHelper extends SQLiteOpenHelper{
    public static final String TAG = "LocalDBHelper";
    public static final String CREATE_TABLE_CLOTH = "create table cloth("
            + "id varchar(255),"
            + "cloth_title varchar(255),"
            + "description varchar(255),"
            + "imgUrl varchar(255) primary key,"
            + "Tags varchar(255),"
            + "sdkPath varchar(255),"
            + "user_name varchar(255)"
            + ")"
            ;
    public static final String CREATE_TABLE_TAGCLOTH = "create table tagCloth(\n" +
            "\tid integer primary key autoincrement,\n"+
            "\ttag_title varchar(255) ,\n" +
            "\tcloth_id varchar(255),\n" +
            "\tcloth_imgUrl VARCHAR(255),\n" +
            "\tcloth_sdkPath VARCHAR(255)\n" +
            ")";
//            +"\tFOREIGN key(cloth_id) REFERENCES cloth(id)on delete CASCADE on UPDATE CASCADE\n" +

    public static final String CREATE_TABLE_TAG = "create table tag(\n" +
            "\ttitle varchar(255) PRIMARY KEY\n" +
            ")";

    private Context context;

    public LocalDBHelper (Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version){
        super(context,name,cursorFactory,version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CLOTH);
        db.execSQL(CREATE_TABLE_TAGCLOTH);
        db.execSQL(CREATE_TABLE_TAG);
        Log.d(TAG,"first sqlite initial: table has been created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion){
            //都没有break的
            case 1:
                db.execSQL(CREATE_TABLE_CLOTH);
                db.execSQL(CREATE_TABLE_TAGCLOTH);
                db.execSQL(CREATE_TABLE_TAG);
            case 2:
                db.execSQL(CREATE_TABLE_TAGCLOTH);
                db.execSQL(CREATE_TABLE_TAG);
            case 3:
                db.execSQL(CREATE_TABLE_TAG);
            default:

        }
    }
}
