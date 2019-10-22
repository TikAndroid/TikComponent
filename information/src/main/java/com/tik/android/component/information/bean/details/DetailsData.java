package com.tik.android.component.information.bean.details;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailsData implements Parcelable {
	private Next next;
	private Meta meta;
	private boolean sticky;
	private int id;
	private String type;
	private String title;
	private String excerpt;
	private String dateGmt;
	private String content;
	private long timestamp;

	protected DetailsData(Parcel in) {
		sticky = in.readByte() != 0;
		id = in.readInt();
		type = in.readString();
		title = in.readString();
		excerpt = in.readString();
		dateGmt = in.readString();
		content = in.readString();
		timestamp = in.readLong();
	}

	public static final Creator<DetailsData> CREATOR = new Creator<DetailsData>() {
		@Override
		public DetailsData createFromParcel(Parcel in) {
			return new DetailsData(in);
		}

		@Override
		public DetailsData[] newArray(int size) {
			return new DetailsData[size];
		}
	};

	public void setNext(Next next){
		this.next = next;
	}

	public Next getNext(){
		return next;
	}

	public void setMeta(Meta meta){
		this.meta = meta;
	}

	public Meta getMeta(){
		return meta;
	}

	public void setSticky(boolean sticky){
		this.sticky = sticky;
	}

	public boolean isSticky(){
		return sticky;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setExcerpt(String excerpt){
		this.excerpt = excerpt;
	}

	public String getExcerpt(){
		return excerpt;
	}

	public void setDateGmt(String dateGmt){
		this.dateGmt = dateGmt;
	}

	public String getDateGmt(){
		return dateGmt;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setTimestamp(long timestamp){
		this.timestamp = timestamp;
	}

	public long getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
		return 
			"CategoryData{" +
			"next = '" + next + '\'' + 
			",meta = '" + meta + '\'' + 
			",sticky = '" + sticky + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			",title = '" + title + '\'' + 
			",excerpt = '" + excerpt + '\'' + 
			",date_gmt = '" + dateGmt + '\'' + 
			",content = '" + content + '\'' + 
			",timestamp = '" + timestamp + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (sticky ? 1 : 0));
		dest.writeInt(id);
		dest.writeString(type);
		dest.writeString(title);
		dest.writeString(excerpt);
		dest.writeString(dateGmt);
		dest.writeString(content);
		dest.writeLong(timestamp);
	}
}
