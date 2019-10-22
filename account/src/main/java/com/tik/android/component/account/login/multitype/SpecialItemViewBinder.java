package com.tik.android.component.account.login.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tik.android.component.account.R;
import com.tik.android.component.account.R2;
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
public class SpecialItemViewBinder extends ItemViewBinder<SpecialItemViewBinder.SpecialMultiType, SpecialItemViewBinder.ViewHolder> {

    public enum SpecialMultiType {
        HEADER, FOOTER, EMPTY
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.account_country_no_result, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull SpecialMultiType item) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.content)
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

