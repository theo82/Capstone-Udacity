package theo.tziomakas.news.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import alarm.AppWidgetAlarm;
import alarm.NewsService;
import theo.tziomakas.news.DetailActivity;
import theo.tziomakas.news.MainActivity;
import theo.tziomakas.news.R;
import theo.tziomakas.news.model.News;

public class NewsAppWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                               int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent templateIntent = new Intent(context, DetailActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        templateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        PendingIntent templatePendingIntent = PendingIntent.getActivity(
                context, 0, templateIntent, 0);

        views.setPendingIntentTemplate(R.id.widget_grid_view,
                templatePendingIntent);
        // Instruct the widget manager to update the widget

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        }
    }



    @Override
    public void onEnabled(Context context) {

        // start alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();


    }

    @Override
    public void onDisabled(Context context) {

        // stop alarm only if all widgets have been disabled
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidgetComponentName = new ComponentName(context.getPackageName(), getClass().getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName);
        if (appWidgetIds.length == 0) {
            // stop alarm
            AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
            appWidgetAlarm.stopAlarm();


        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(intent.getAction().equals(ACTION_AUTO_UPDATE))
        {
            // DO SOMETHING

            //intent = new Intent(context, NewsService.class);
            //context.startService(intent);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


            String json = prefs.getString("newsListData", "");

        }

    }
}
