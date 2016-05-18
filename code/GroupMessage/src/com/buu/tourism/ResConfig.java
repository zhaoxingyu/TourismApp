package com.buu.tourism;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ResConfig {
	
	// 单利
	private static ResConfig instance = null;
	
	private ResConfig() { }

	public synchronized static ResConfig getInstance() {
		if (instance == null) {
			instance = new ResConfig();
		}
		return instance;
	}
	
	//
	
	
	private Context context ; 
	
	private HashMap<String, Integer> headRes = new HashMap<String, Integer>() ; 
	
	public void initRes(Context context){
		
		this.context = context ; 
		
		headRes.put( "h_0001" , R.drawable.h_0001) ;
		headRes.put( "h_0002" , R.drawable.h_0002) ;
		headRes.put( "h_0003" , R.drawable.h_0003) ;
		headRes.put( "h_0004" , R.drawable.h_0004) ;
		headRes.put( "h_0005" , R.drawable.h_0005) ;
		headRes.put( "h_0006" , R.drawable.h_0006) ;
		headRes.put( "h_0007" , R.drawable.h_0007) ;
		headRes.put( "h_0008" , R.drawable.h_0008) ;
		headRes.put( "h_0009" , R.drawable.h_0009) ;
		headRes.put( "h_0010" , R.drawable.h_0010) ;
		headRes.put( "h_0011" , R.drawable.h_0011) ;
		headRes.put( "h_0012" , R.drawable.h_0012) ;
		headRes.put( "h_0013" , R.drawable.h_0013) ;
		headRes.put( "h_0014" , R.drawable.h_0014) ;
		headRes.put( "h_0015" , R.drawable.h_0015) ;
		headRes.put( "h_0016" , R.drawable.h_0016) ;
		headRes.put( "h_0017" , R.drawable.h_0017) ;
		headRes.put( "h_0018" , R.drawable.h_0018) ;
		headRes.put( "h_0019" , R.drawable.h_0019) ;
		headRes.put( "h_0020" , R.drawable.h_0020) ;
		
	}
	
	
	public int getDrawableId(String key){
		return headRes.get( "h_" + key) ;
    }

    public String getKey(int value) {
        Set<Entry<String, Integer>> entrySet = headRes.entrySet();
        for (Entry<String, Integer> entry : entrySet) {
            int drawableId = value;
            int tmp = entry.getValue();
            if (drawableId == tmp) {
                String key = entry.getKey();
                key = key.replace("h_", "");
                return key;
            }
        }
        return "001";
    }
}
