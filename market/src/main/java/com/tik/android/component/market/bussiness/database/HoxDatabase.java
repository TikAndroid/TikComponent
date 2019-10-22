package com.tik.android.component.market.bussiness.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

import com.tik.android.component.bussiness.account.LocalAccountInfoManager;
import com.tik.android.component.libcommon.BaseApplication;
import com.tik.android.component.libcommon.LogUtil;
import com.tik.android.component.libcommon.sharedpreferences.RxSharedPrefer;
import com.tik.android.component.market.bean.UserDataManager;
import com.tik.android.component.market.util.Constant;

/**
 * Created by wangqiqi on 2018/11/20.
 */
@Database(entities = {MarketStockEntity.class}, version = 3, exportSchema = false)
public abstract class HoxDatabase extends RoomDatabase {
    private static volatile HoxDatabase INSTANCE;
    private static String mDBName;

    public abstract MarketStockDao marketStockDao();

    public static HoxDatabase getInstance() {
        if (null == INSTANCE) {
            synchronized (HoxDatabase.class) {
                StringBuilder dbName = new StringBuilder();

                if (null == LocalAccountInfoManager.getInstance().getUser()) {
                    dbName.append(Constant.DB_SUFFIX_NAME);
                } else {
                    dbName.append(LocalAccountInfoManager.getInstance().getUid()).append("_").append(Constant.DB_SUFFIX_NAME);
                }

                mDBName = dbName.toString();

                LogUtil.d("dbName: " + dbName);

                INSTANCE = Room.databaseBuilder(BaseApplication.getAPPContext().getApplicationContext(),
                        HoxDatabase.class, dbName.toString())
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build();
            }
        }

        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table market_stock add column price REAL not null default 0");
            database.execSQL("alter table market_stock add column gains TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Todo 升级后会出现无法及时重新获取asset中数据，导致第一次进入后行情列表为空
            RxSharedPrefer.builder().context(BaseApplication.getAPPContext()).build().edit()
                    .putBoolean(UserDataManager.getInstance().getSharedPreferName(), false)
                    .apply();
            database.execSQL("alter table market_stock add column local INTEGER not null default 0");
            database.execSQL("alter table market_stock add column gainsValue TEXT");
        }
    };

    public void release() {
        if (null != INSTANCE && INSTANCE.isOpen()) {
            INSTANCE.close();
        }

        INSTANCE = null;
    }
}
