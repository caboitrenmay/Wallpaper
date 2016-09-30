package com.fsoc.wallpaper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fsoc.wallpaper.db.FeedReaderSQLite;
import com.fsoc.wallpaper.model.ImgPackList;
import com.fsoc.wallpaper.model.ImgPackObj;
import com.fsoc.wallpaper.util.CircleImageView;
import com.fsoc.wallpaper.util.ConnectServer;
import com.fsoc.wallpaper.util.DownloadPack;
import com.fsoc.wallpaper.util.JsonResultHandler;
import com.fsoc.wallpaper.util.SettingsPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ListPackActivity extends DownloadPack {
	
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		imageLoader = ImageLoader.getInstance();
		
		final ListView listview = (ListView) findViewById(R.id.listview);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {


                final ImgPackList packList = new ImgPackList();
                ImgPackObj imgPackObj = new ImgPackObj();
                imgPackObj.setId("1");
                imgPackObj.setName("Pokemon");
                imgPackObj.setThumb("drawable://" + R.drawable.bg2);
                packList.add(imgPackObj);

                // add db
                packList.addAll(getObjectsDb());

				String resutl = ConnectServer.listPackage(ListPackActivity.this);

				/*resutl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<root>\n" +
						"   <code>1</code>\n" +
						"   <result>\n" +
						"      <element>\n" +
						"         <id>0</id>\n" +
						"         <name>name1</name>\n" +
						"         <pack>http://abc.zip</pack>\n" +
						"         <thumb>http://abcd.jpg</thumb>\n" +
						"      </element>\n" +
						"      <element>\n" +
						"         <id>1</id>\n" +
						"         <name>name1</name>\n" +
						"         <pack>http://abc1.zip</pack>\n" +
						"         <thumb>http://abcd1.jpg</thumb>\n" +
						"      </element>\n" +
						"   </result>\n" +
						"</root>";*/

				JsonResultHandler resultHandler = new JsonResultHandler(resutl);
				if (resultHandler.isOk()) {
					JSONObject jsonObject = resultHandler.getjObj();
					JSONArray array = null;
					try {
						jsonObject = jsonObject.getJSONObject("result");
						array = jsonObject.getJSONArray("element");
					} catch (JSONException e) {
						e.printStackTrace();
					}

					
					if (!array.isNull(0)) {
						packList.addAll(ImgPackList.initFromJsonArray(array.toString()));
					}
				}

                if (packList.size() > 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            adapter = new ImgPackAdapter(ListPackActivity.this, packList);
                            listview.setAdapter(adapter);
                        }
                    });

                }
			}
		}).start();

	}
	
	private ImgPackAdapter adapter;

    @Override
    public void downloadFinish(String id) {
        super.downloadFinish(id);
		if (id.equals("ERROR")) return;

        SettingsPreference preference = SettingsPreference.getInstance(this);
        preference.setItemDownloaded("" + id);

        adapter.notifyDataSetChanged();
    }

	public static class ViewHolder {
		public TextView firstLine;
		public TextView secondLine;
		
		public CircleImageView icon;
		public Button add;

	}
	
	public class ImgPackAdapter extends BaseAdapter {
		private final Activity activity;
		//private final String[] values;
		
		private ImgPackList packList;

		public ImgPackAdapter(Activity activity, ImgPackList imgPackList) {
			this.activity = activity;
			this.packList = imgPackList;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_package, parent, false);
				//convertView = inflater.inflate(R.layout.item_list, null);
				holder = new ViewHolder();
				holder.firstLine = (TextView) convertView.findViewById(R.id.firstLine);
				holder.secondLine = (TextView) convertView.findViewById(R.id.secondLine);
				holder.icon = (CircleImageView) convertView.findViewById(R.id.icon);
				holder.add = (Button) convertView.findViewById(R.id.add);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.firstLine.setText(packList.get(position).getName());
			//icon
			imageLoader.displayImage(packList.get(position).getThumb(), holder.icon);

			SettingsPreference preference = SettingsPreference.getInstance(activity);
			String[] array = preference.getItemDownloaded().split(",");
			
			if (Arrays.asList(array).contains(packList.get(position).getId()) || packList.get(position).getId().equals("1")) {
				holder.add.setText("Set ảnh");
			}
			else {
				holder.add.setText("Thêm ảnh");
			}
			
			holder.add.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

					//new DownloadFileFromURL().execute(packList.get(position).getPack(), packList.get(position).getId());

					SettingsPreference preference = SettingsPreference.getInstance(activity);
					String[] array = preference.getItemDownloaded().split(",");
					
					if (Arrays.asList(array).contains(packList.get(position).getId()) || packList.get(position).getId().equals("1")) {
						preference.setIdPack(packList.get(position).getId());
						activity.setResult(RESULT_OK);
						activity.finish();
					}
					else {
						Toast.makeText(activity, "id: " + packList.get(position).getId(), Toast.LENGTH_SHORT).show();
                        // save to db
                        saveToDb(packList.get(position));

						new DownloadFileFromURL().execute(packList.get(position).getPack(), packList.get(position).getId());
					}
				}
			});
			
			return convertView;
		}

		@Override
		public int getCount() {
			return packList.size();
		}

		@Override
		public Object getItem(int position) {
			return packList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

    private boolean saveToDb(ImgPackObj obj) {
        FeedReaderSQLite sqLite = new FeedReaderSQLite(this);
        if (sqLite.putObject(obj) > 0) {
            sqLite.closeDb();
            return true;
        }
        return false;
    }

    private List<ImgPackObj> getObjectsDb() {
        FeedReaderSQLite sqLite = new FeedReaderSQLite(this);
        return sqLite.getAllObjects();
    }
}
