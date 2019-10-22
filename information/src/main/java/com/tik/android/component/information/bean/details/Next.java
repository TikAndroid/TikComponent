package com.tik.android.component.information.bean.details;

import android.os.Parcel;
import android.os.Parcelable;

public class Next implements Parcelable {
	private int id;
	private String title;

	protected Next(Parcel in) {
		id = in.readInt();
		title = in.readString();
	}

	public static final Creator<Next> CREATOR = new Creator<Next>() {
		@Override
		public Next createFromParcel(Parcel in) {
			return new Next(in);
		}

		@Override
		public Next[] newArray(int size) {
			return new Next[size];
		}
	};

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return 
			"Next{" + 
			"id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
	}
}
