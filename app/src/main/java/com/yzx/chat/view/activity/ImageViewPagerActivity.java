package com.yzx.chat.view.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.yzx.chat.R;
import com.yzx.chat.base.BaseCompatActivity;
import com.yzx.chat.util.AndroidUtil;
import com.yzx.chat.widget.adapter.ImageSelectedAdapter;
import com.yzx.chat.widget.adapter.LocalImageViewPagerAdapter;
import com.yzx.chat.widget.listener.OnRecyclerViewItemClickListener;
import com.yzx.chat.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by YZX on 2018年01月27日.
 * 每一个不曾起舞的日子 都是对生命的辜负
 */

public class ImageViewPagerActivity extends BaseCompatActivity {

    public static final int RESULT_CODE = 1;
    public static final String INTENT_EXTRA_IMAGE_LIST = "ImageList";
    public static final String INTENT_EXTRA_IMAGE_SELECTED_LIST = "ImageSelectedList";
    public static final String INTENT_EXTRA_CURRENT_POSITION = "CurrentPosition";
    public static final String INTENT_EXTRA_IS_ORIGINAL = "IsOriginal";

    private ViewPager mViewPager;
    private CheckBox mCbSelected;
    private Button mBtnConfirm;
    private RadioButton mRBtnOriginal;
    private RecyclerView mRecyclerView;
    private ImageSelectedAdapter mSelectedAdapter;
    private LocalImageViewPagerAdapter mPagerAdapter;
    private ArrayList<String> mImageList;
    private ArrayList<String> mSelectedList;
    private int mCurrentPosition;
    private boolean isOriginal;


    @Override
    protected int getLayoutID() {
        return R.layout.activitty_image_view_pager;
    }

    @Override
    protected void init() {
        mViewPager = findViewById(R.id.ImageViewpagerActivity_mViewPager);
        mCbSelected = findViewById(R.id.ImageViewPagerActivity_mCbSelected);
        mBtnConfirm = findViewById(R.id.ImageViewpagerActivity_mBtnConfirm);
        mRecyclerView = findViewById(R.id.ImageViewpagerActivity_mRecyclerView);
        mRBtnOriginal = findViewById(R.id.ImageViewpagerActivity_mRBtnOriginal);
        mImageList = getIntent().getStringArrayListExtra(INTENT_EXTRA_IMAGE_LIST);
        mSelectedList = getIntent().getStringArrayListExtra(INTENT_EXTRA_IMAGE_SELECTED_LIST);
        mCurrentPosition = getIntent().getIntExtra(INTENT_EXTRA_CURRENT_POSITION, 0);
        isOriginal = getIntent().getBooleanExtra(INTENT_EXTRA_IS_ORIGINAL, false);
        if (mImageList == null || mImageList.size() == 0) {
            finish();
        }
        if (mSelectedList == null) {
            mSelectedList = new ArrayList<>();
        }
        mPagerAdapter = new LocalImageViewPagerAdapter(this, mImageList);
        mSelectedAdapter = new ImageSelectedAdapter(mSelectedList);

        mRBtnOriginal.setOnClickListener(mOnOriginalClickListener);
        mRBtnOriginal.setChecked(isOriginal);
    }

    @Override
    protected void setup() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration((int) AndroidUtil.dip2px(12), SpacesItemDecoration.HORIZONTAL, true, true));
        mRecyclerView.addOnItemTouchListener(mOnSelectedImageItemClickListener);
        mRecyclerView.setAdapter(mSelectedAdapter);


        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mOnPageChangeListener.onPageSelected(mCurrentPosition);

        mCbSelected.setOnCheckedChangeListener(mOnSelectedChangeListener);
        tryShowSelectedView();
        updateBtnConfirmText();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(INTENT_EXTRA_IMAGE_SELECTED_LIST, mSelectedList);
        intent.putExtra(INTENT_EXTRA_IS_ORIGINAL, isOriginal);
        setResult(RESULT_CODE, intent);
        finish();
    }

    private void tryShowSelectedView() {
        if (mSelectedList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void updateBtnConfirmText() {
        int selectedCount = mSelectedList.size();
        if (selectedCount > 0) {
            mBtnConfirm.setText(String.format(Locale.getDefault(), "%s(%d/%d)", getString(R.string.ImageSelectorActivity_Send), selectedCount, mImageList.size()));
        } else {
            mBtnConfirm.setText(R.string.ImageSelectorActivity_Send);
        }
    }

    private final CompoundButton.OnCheckedChangeListener mOnSelectedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String url = mImageList.get(mCurrentPosition);
            if (isChecked) {
                if (!mSelectedList.contains(url)) {
                    mSelectedAdapter.notifyItemInsertedEx(mSelectedList.size());
                    mSelectedAdapter.setSelected(mSelectedList.size());
                    mSelectedList.add(url);
                    tryShowSelectedView();
                }
            } else {
                int removePosition = mSelectedList.indexOf(url);
                if (removePosition >= 0) {
                    mSelectedAdapter.notifyItemRemovedEx(removePosition);
                    mSelectedList.remove(removePosition);
                    mSelectedAdapter.setSelected(-1);
                    tryShowSelectedView();
                }
            }
            updateBtnConfirmText();

        }
    };

    private final OnRecyclerViewItemClickListener mOnSelectedImageItemClickListener = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(int position, RecyclerView.ViewHolder viewHolder) {
            String url = mSelectedList.get(position);
            int index = mImageList.indexOf(url);
            if (index >= 0) {
                mViewPager.setCurrentItem(index, false);
                mSelectedAdapter.setSelected(position);
            }
        }
    };

    private final ViewPager.SimpleOnPageChangeListener mOnPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            setTitle(String.format(Locale.getDefault(), "%d/%d", position + 1, mImageList.size()));
            mCurrentPosition = position;
            if (mCurrentPosition < mImageList.size()) {
                String currentUrl = mImageList.get(mCurrentPosition);
                int selectedIndex = mSelectedList.indexOf(currentUrl);
                if (selectedIndex >= 0) {
                    mSelectedAdapter.setSelected(selectedIndex);
                    mCbSelected.setChecked(true);
                } else {
                    mSelectedAdapter.setSelected(-1);
                    mCbSelected.setChecked(false);
                }
            }
        }
    };

    private final View.OnClickListener mOnOriginalClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isOriginal = !isOriginal;
            mRBtnOriginal.setChecked(isOriginal);
        }
    };


}
