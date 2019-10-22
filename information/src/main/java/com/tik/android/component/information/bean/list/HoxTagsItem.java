package com.tik.android.component.information.bean.list;

import android.os.Parcel;
import android.os.Parcelable;

public class HoxTagsItem implements Parcelable {
	private String symbol;
	private int score;
	private String en;
	private String cn;
	private String word;

	protected HoxTagsItem(Parcel in) {
		symbol = in.readString();
		score = in.readInt();
		en = in.readString();
		cn = in.readString();
		word = in.readString();
	}

	public static final Creator<HoxTagsItem> CREATOR = new Creator<HoxTagsItem>() {
		@Override
		public HoxTagsItem createFromParcel(Parcel in) {
			return new HoxTagsItem(in);
		}

		@Override
		public HoxTagsItem[] newArray(int size) {
			return new HoxTagsItem[size];
		}
	};

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getEn() {
		return en;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getCn() {
		return cn;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	@Override
	public String toString() {
		return
				"HoxTagsItem{" +
						"symbol = '" + symbol + '\'' +
						",score = '" + score + '\'' +
						",en = '" + en + '\'' +
						",cn = '" + cn + '\'' +
						",word = '" + word + '\'' +
						"}";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(symbol);
		dest.writeInt(score);
		dest.writeString(en);
		dest.writeString(cn);
		dest.writeString(word);
	}
}
