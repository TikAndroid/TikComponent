package com.tik.android.component.information.bean.list;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Meta implements Parcelable {
	private List<Object> hoxTags;
	private String author;
	private String imageUrl;
	private String origin;

	protected Meta(Parcel in) {
		author = in.readString();
		imageUrl = in.readString();
		origin = in.readString();
	}

	public static final Creator<Meta> CREATOR = new Creator<Meta>() {
		@Override
		public Meta createFromParcel(Parcel in) {
			return new Meta(in);
		}

		@Override
		public Meta[] newArray(int size) {
			return new Meta[size];
		}
	};

	public void setHoxTags(List<Object> hoxTags){
		this.hoxTags = hoxTags;
	}

	public List<Object> getHoxTags(){
		return hoxTags;
	}

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setImageUrl(String imageUrl){
		this.imageUrl = imageUrl;
	}

	public String getImageUrl(){
		return imageUrl;
	}

	public void setOrigin(String origin){
		this.origin = origin;
	}

	public String getOrigin(){
		return origin;
	}

	@Override
 	public String toString(){
		return 
			"Meta{" + 
			"hox_tags = '" + hoxTags + '\'' + 
			",author = '" + author + '\'' + 
			",image_url = '" + imageUrl + '\'' + 
			",origin = '" + origin + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(author);
		dest.writeString(imageUrl);
		dest.writeString(origin);
	}
}