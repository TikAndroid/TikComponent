package com.tik.android.component.information.bean.category;

import android.os.Parcel;
import android.os.Parcelable;

public class ZhItem implements Parcelable {
	private int parent;
	private int count;
	private String name;
	private int id;

	protected ZhItem(Parcel in) {
		parent = in.readInt();
		count = in.readInt();
		name = in.readString();
		id = in.readInt();
	}

	public static final Creator<ZhItem> CREATOR = new Creator<ZhItem>() {
		@Override
		public ZhItem createFromParcel(Parcel in) {
			return new ZhItem(in);
		}

		@Override
		public ZhItem[] newArray(int size) {
			return new ZhItem[size];
		}
	};

	public void setParent(int parent){
		this.parent = parent;
	}

	public int getParent(){
		return parent;
	}

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"ZhItem{" + 
			"parent = '" + parent + '\'' + 
			",count = '" + count + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(parent);
		dest.writeInt(count);
		dest.writeString(name);
		dest.writeInt(id);
	}
}
