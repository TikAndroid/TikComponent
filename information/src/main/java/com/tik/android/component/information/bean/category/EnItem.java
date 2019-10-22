package com.tik.android.component.information.bean.category;

import android.os.Parcel;
import android.os.Parcelable;

public class EnItem implements Parcelable {
	private int parent;
	private int count;
	private String name;
	private int id;

	protected EnItem(Parcel in) {
		parent = in.readInt();
		count = in.readInt();
		name = in.readString();
		id = in.readInt();
	}

	public static final Creator<EnItem> CREATOR = new Creator<EnItem>() {
		@Override
		public EnItem createFromParcel(Parcel in) {
			return new EnItem(in);
		}

		@Override
		public EnItem[] newArray(int size) {
			return new EnItem[size];
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
			"EnItem{" + 
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
