package com.tik.android.component.account.login.multitype;

import android.support.annotation.NonNull;
import android.support.v4.util.Consumer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
import com.tik.android.component.account.login.bean.Country;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by xiangning on 2018/11/18.
 */
public class CountryItemViewBinder extends ItemViewBinder<Country, CountryItemViewBinder.ViewHolder> {

    public static class CountryItem extends Country {
        CharSequence country;
        CharSequence code;

        public CountryItem() {
        }

        public CountryItem(CharSequence country, CharSequence code) {
            super(String.valueOf(country), String.valueOf(code), "");
            this.country = country;
            this.code = code;
        }

        public void setCountry(CharSequence country) {
            super.setCountry(country);
            this.country = country;
        }

        public void setCode(CharSequence code) {
            super.setCode(code);
            this.code = code;
        }

        public CharSequence getCountry() {
            return country;
        }

        public CharSequence getCode() {
            return code;
        }
    }


    private Consumer<Country> mListener;

    public CountryItemViewBinder(Consumer<Country> onCountryClickListener) {
        this.mListener = onCountryClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.account_country_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Country item) {
        holder.name.setText(item.getCountry());
        holder.code.setText(item.getCode());
        if (mListener != null) {
            holder.itemView.setOnClickListener(v -> {
                // 返回一个新的country，以免带样式的CountryItem被返回给调用者
                mListener.accept(new Country(item));
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.name)
        TextView name;
        @BindView(R2.id.code)
        TextView code;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}