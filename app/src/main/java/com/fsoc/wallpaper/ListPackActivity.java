package com.fsoc.wallpaper;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fsoc.wallpaper.model.IdPackList;
import com.fsoc.wallpaper.model.IdPackObj;
import com.fsoc.wallpaper.model.ImgPackList;
import com.fsoc.wallpaper.util.CircleImageView;
import com.fsoc.wallpaper.util.ConnectServer;
import com.fsoc.wallpaper.util.JsonResultHandler;
import com.fsoc.wallpaper.util.SettingsPreference;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListPackActivity extends Activity {
	
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
				final String resutl = ConnectServer.listPackage(ListPackActivity.this);
				
				JsonResultHandler resultHandler = new JsonResultHandler(resutl);
				if (resultHandler.isOk()) {
					JSONObject jsonObject = resultHandler.getjObj();
					JSONArray array = null;
					try {
						array = jsonObject.getJSONArray("result");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					final ImgPackList packList = new ImgPackList();
					/*ImgPackObj imgPackObj = new ImgPackObj();
					imgPackObj.setId("1");
					imgPackObj.setName("Áo dài");
					imgPackObj.setSubname("áo dài 2016");
					imgPackObj.setThumb("drawable://" + R.drawable.bg2);
					packList.add(imgPackObj);*/
					
					if (!array.isNull(0)) {
						packList.addAll(ImgPackList.initFromJsonArray(array.toString()));
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
			}
		}).start();
		
		/*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
			}
			
		});*/

	}
	
	ImgPackAdapter adapter;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode == RESULT_OK) {
			String idPack = data.getStringExtra("id_package");
			
			SettingsPreference preference = SettingsPreference.getInstance(this);
			
			preference.setItemDownloaded("" + requestCode);
			
			IdPackList idPackList = new IdPackList();
			if (IdPackList.initFromJsonArray(preference.getIdList()) != null){ 
				idPackList.addAll(IdPackList.initFromJsonArray(preference.getIdList()));
			}
			
			IdPackObj idPackObj = new IdPackObj("" + requestCode, idPack);
			idPackList.add(idPackObj);
			preference.setIdList(idPackList.toJson());
			
			adapter.notifyDataSetChanged();
		}
		
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
			holder.secondLine.setText(packList.get(position).getSubname());
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
					
					SettingsPreference preference = SettingsPreference.getInstance(activity);
					String[] array = preference.getItemDownloaded().split(",");
					
					if (Arrays.asList(array).contains(packList.get(position).getId()) || packList.get(position).getId().equals("1")) {
						String idSub = "1";
						
						IdPackList idPackList = new IdPackList(); 
						if (IdPackList.initFromJsonArray(preference.getIdList()) != null){ 
							idPackList.addAll(IdPackList.initFromJsonArray(preference.getIdList()));
						}
						
						
						for (IdPackObj obj : idPackList) {
							if (obj.getId().equals(packList.get(position).getId())) {
								idSub = obj.getIdSub();
								break;
							}
						}
						
						//Toast.makeText(activity, "Open pack: " + idSub, Toast.LENGTH_SHORT).show();
						
						preference.setIdPack(idSub);
						
						activity.setResult(RESULT_OK);
						activity.finish();
					}
					else {
					
						try {
							SmsManager smsManager = SmsManager.getDefault();
							smsManager.sendTextMessage(packList.get(position).getSms_to(), 
									null, packList.get(position).getSms_content(), null, null);
						} catch (IllegalArgumentException e) {
							Toast.makeText(activity, "send sms error: empty adress", Toast.LENGTH_SHORT).show();
						}
						
						
						Toast.makeText(activity, "send sms: " + packList.get(position).getSms_content()
								+ " to" + packList.get(position).getSms_to(), Toast.LENGTH_SHORT).show();
						
						//Toast.makeText(activity, "id: " + packList.get(position).getId(), Toast.LENGTH_SHORT).show();
						
						int code = Integer.parseInt(packList.get(position).getId());
						
						//activity.startActivity(new Intent(activity, EnterCodeActivity.class));
						activity.startActivityForResult(new Intent(activity, EnterCodeActivity.class), code);
					
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
}
