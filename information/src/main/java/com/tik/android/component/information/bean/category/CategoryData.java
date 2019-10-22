package com.tik.android.component.information.bean.category;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CategoryData implements Parcelable {
	private List<EnItem> en;
	private List<ZhItem> zh;

	protected CategoryData(Parcel in) {
	}

	public static final Creator<CategoryData> CREATOR = new Creator<CategoryData>() {
		@Override
		public CategoryData createFromParcel(Parcel in) {
			return new CategoryData(in);
		}

		@Override
		public CategoryData[] newArray(int size) {
			return new CategoryData[size];
		}
	};

	public void setEn(List<EnItem> en) {
		this.en = en;
	}

	public List<EnItem> getEn(){
		return en;
	}

	public void setZh(List<ZhItem> zh){
		this.zh = zh;
	}

	public List<ZhItem> getZh(){
		return zh;
	}

	@Override
 	public String toString(){
		return 
			"CategoryData{" +
			"en = '" + en + '\'' + 
			",zh = '" + zh + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}
}