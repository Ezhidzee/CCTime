package logic;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hedgehogSoft.carcleantime.InitSettingsActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    public static String DB_NAME = "СitiesDb.db";
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE = "worldСities"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_CITYASC = "city_ascii";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_ISO2 = "iso2";
    public static final String COLUMN_ISO3 = "iso3";
    public static final String COLUMN_ADMIN = "admin_name";
    public static final String COLUMN_CAPITAL = "capital";
    public static final String COLUMN_POP = "population";
    private Context myContext = InitSettingsActivity.getContext();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void createDb(){

        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
            }
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
        finally {
            try{
                if(myOutput!=null) myOutput.close();
                if(myInput!=null) myInput.close();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }

    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
