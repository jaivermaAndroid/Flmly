package com.flmly.tv.utility;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.leanback.widget.BaseCardView;
import androidx.leanback.widget.Presenter;

import com.bumptech.glide.Glide;
import com.flmly.tv.R;
import com.flmly.tv.model.HomeModel;
import com.flmly.tv.model.SearchModel;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class CategoryPresenter extends Presenter {
    private int mSelectedBackgroundColor = 1;
    private int mDefaultBackgroundColor = 3;
    int ITEM_WIDTH = 480;
    int ITEM_HEIGHT = 285;

    @Override
    public CategoryPresenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mDefaultBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.transparent);
        mSelectedBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.white);
        CustomCardView cardView = new CustomCardView(viewGroup.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setCardType(BaseCardView.CARD_TYPE_MAIN_ONLY);
        cardView.setFocusableInTouchMode(true);
        cardView.setPadding(0,8,8,8);
        updateCardBackgroundColor(cardView, false);
        return new CategoryPresenter.ViewHolder(cardView);


//        mDefaultBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.white);
//        mSelectedBackgroundColor = ContextCompat.getColor(viewGroup.getContext(), R.color.black);
//        ImageCardView imageCardView = new ImageCardView(viewGroup.getContext())
//        {
//            @Override
//            public void setSelected(boolean selected) {
//                updateCardBackgroundColor(this, selected);
//                super.setSelected(selected);
//            }
//        };

//        imageCardView.setMainImageScaleType(FIT_XY);
//        imageCardView.setVisibility(View.VISIBLE);
//        imageCardView.setFocusable(true);
//        imageCardView.setExtraVisibility(View.VISIBLE);
//        imageCardView.setBackgroundColor(viewGroup.getResources().getColor(R.color.red));
//        imageCardView.setPadding(5,5,5,5);
//        imageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ALWAYS);
//        updateCardBackgroundColor(imageCardView,true);
//        imageCardView.setFocusableInTouchMode(true);
//        return new ViewHolder(imageCardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        CustomCardView cardView = (CustomCardView) viewHolder.view;
        SearchModel data = (SearchModel) item;
        cardView.setCardType(BaseCardView.CARD_TYPE_MAIN_ONLY);
        cardView.setMainImageScaleType(CENTER_CROP);
//        cardView.setMainImageAdjustViewBounds(false);
//        if (data.getViewType().equals("PORTRAIT")) {
        cardView.setMainImageDimensions(228, 310);
//        }
//        else {
//            cardView.setMainImageDimensions(620, 320);
//        }
        ((CategoryPresenter.ViewHolder) viewHolder).updateCardViewImage("https://www.flmly.com/" + data.getThumb());
        cardView.setMainImageAdjustViewBounds(false);

    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {

//        ImageCardView imageCardView = (ImageCardView) viewHolder.view;
//        imageCardView.setBadgeImage(null);
//        imageCardView.setMainImage(null);


    }

//    private void updateCardBackgroundColor(ImageCardView view, boolean selected) {
//        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
//        int show = selected ? View.VISIBLE : View.VISIBLE;
//        int playicon = selected ? View.GONE : View.VISIBLE;
//
//    }


    private void updateCardBackgroundColor(CustomCardView imageCardView, boolean selected) {
//        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
        int color = selected ? mSelectedBackgroundColor : mDefaultBackgroundColor;
        imageCardView.setBackgroundColor(color);
    }


    static class ViewHolder extends Presenter.ViewHolder {
        private CustomCardView mCardView;

        public ViewHolder(View view) {
            super(view);
            mCardView = (CustomCardView) view;
        }

        public CustomCardView getCardView() {
            return mCardView;
        }

        protected void updateCardViewImage(String url) {
            Glide.with(mCardView.getContext())
                    .load(url)
                    .into(mCardView.getMainImageView());

        }


    }
}
