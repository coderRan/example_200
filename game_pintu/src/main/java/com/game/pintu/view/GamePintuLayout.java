package com.game.pintu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


import com.game.pintu.R;
import com.game.utils.ImagePiece;
import com.game.utils.ImageSplitterUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by zdr on 16-3-10.
 * 游戏住界面的布局类
 */
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn = 3;
    /* 容器内边距 */
    private int mPadding = 0;
    /* 每个piece之间的距离 */
    private int mMargin = 0;
    private ImageView[] mGamePintuItems = null;
    private int mItemWidth = 0;


    /* 完整图片的引用 */
    private Bitmap mBitmap = null;
    /* 指向已经切好的图片集合 */
    private List<ImagePiece> mItemBitmaps = null;
    /* 防止初始布局多次执行，当once = false时初始化布局 */
    private boolean once = false;
    /* 游戏面板的宽度，高度与宽度相等 */
    private int mGameWidth = 0;

    /* 游戏完成结束标志 */
    private boolean isGameSuccess = false;
    /* 时间结束，游戏未完成结束标志 */
    private boolean isGameOVer = false;

    public interface GamePintuListener {
        void nextLevel(int nextLevel);

        void timeChanged(int currentTime);

        void gameOver();

    }

    public GamePintuListener gameListener;

    /**
     * 设置回调接口
     *
     * @param gameListener
     */
    public void setOnGameListener(GamePintuListener gameListener) {
        this.gameListener = gameListener;
    }

    /**
     * 设置是开启时间限制
     *
     * @param isTImeEnabled
     */


    private boolean isTImeEnabled = false;

    private int mTime = 0;
    private static final int TIME_CHANGE = 0x0012FF7C;
    private static final int NEXT_LEVEL = 0x0012ff7D;
    private int mlevel = 1;

    public void setIsTImeEnabled(boolean isTImeEnabled) {
        this.isTImeEnabled = isTImeEnabled;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TIME_CHANGE:

                    if (isGameSuccess || isGameOVer) {
                        return;
                    }

                    if (gameListener != null) {
                        gameListener.timeChanged(mTime);
                        if (mTime == 0) {
                            isGameOVer = true;
                            gameListener.gameOver();
                            return;
                        }
                    }
                    if (!isGameSuccess && !isPause) {
                        mTime--;
                    }

                    mHandler.sendEmptyMessageDelayed(TIME_CHANGE, 1000);

                    break;
                case NEXT_LEVEL:
                    mlevel = mlevel + 1;
                    if (gameListener != null) {
                        gameListener.nextLevel(mlevel);
                    } else {
                        nextLevel();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public GamePintuLayout(Context context) {
        this(context, null);
    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        /* 将单位dp转换成px */
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                getResources().getDisplayMetrics());
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());
    }

    public int getmTime() {
        return mTime;
    }

    public void setmTime(int mTime) {
        this.mTime = mTime;
    }

    /**
     * 设置布局的大小，将布局设置为正方形
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /* 取最小值设置布局的宽度和高度 */
        mGameWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {
            /* 设置ImageView(Item)的宽度和高度 */
            initItems();
            /* 切图，排序 */
            initBitmap();

            /* 判断是否开启时间限制 */
            checkTimeEnable();

            //mHandler.sendEmptyMessage(TIME_CHANGE);
            once = true;
        }
        setMeasuredDimension(mGameWidth, mGameWidth);
    }

    private void checkTimeEnable() {
        if (isTImeEnabled) {
            /* 根据关卡的等级设置时间 */
            countTimeBaseLevel();
            mHandler.sendEmptyMessage(TIME_CHANGE);
        }
    }

    private void countTimeBaseLevel() {
        mTime = (int) (Math.pow(2, mlevel) * 30);
    }

    /**
     * 进行切图，并把切好的图片打乱顺序
     */
    private void initItems() {
        if (mBitmap == null) {
            setmBitmap(R.drawable.image_0);
        }
        mItemBitmaps = ImageSplitterUtil.splitImage(mBitmap, mColumn);

            /* 实现乱序 */
        Collections.sort(mItemBitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece lhs, ImagePiece rhs) {

                return Math.random() > 0.5 ? 1 : -1;
            }
        });
            /*
            乱续实现的第二中方法
             Collections.shuffle(mItemBitmaps);
             */
    }

    public void setmBitmap(int imageID) {
        mBitmap = BitmapFactory.decodeResource(getResources(), imageID);
    }

    /**
     * 设置ImageView(Item)的宽度和高度
     * 设置每个小图片的宽高
     */
    private void initBitmap() {
        /* 每个小图片的宽度 = （总宽度 - 最外的宽度 - 小图片之间的宽度）/ 小图片个数 */
        mItemWidth = (mGameWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        mGamePintuItems = new ImageView[mColumn * mColumn];
        /* 生成ImageView */
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);

            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());

            mGamePintuItems[i] = item;
            item.setId(i + 1);
            /* mItemBitmaps的下标值是正确顺序，将正确的顺序绑定到了每个小图片上。即tag */
            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth,
                    mItemWidth);

            /*
            设置Item的横向间距，利用rightMargin
            当不是最后一列的时候
            */
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;

            }
            //不是第一列的时候
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, mGamePintuItems[i - 1].getId());
            }
            /*
            设置行间距
            不是第一行
            */
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, mGamePintuItems[i - mColumn].getId());

            }
            addView(item, lp);
        }
    }

    public void restart() {
        isGameOVer = false;
        mColumn--;
        nextLevel();
    }

    public boolean isPause() {
        return isPause;
    }

    private boolean isPause = false;

    /**
     * 游戏暂停
     */
    public void pause() {
        isPause = true;
    }

    /**
     * 恢复游戏
     */
    public void resume() {
        isPause = false;
    }

    public void nextLevel() {
        this.removeAllViews();
        mAnimLayout = null;
        //mBitmap = null;
        mFirst = null;
        mSecond = null;
        mColumn++;
        isGameSuccess = false;
        checkTimeEnable();

        initItems();
        initBitmap();

    }

    /**
     * 获取paddings中的最小值
     *
     * @param paddings 源
     * @return 最小值
     */
    private int min(int... paddings) {
        int min = paddings[0];
        for (int padding : paddings) {
            if (padding < min)
                min = padding;
        }
        return min;
    }

    private ImageView mFirst = null;
    private ImageView mSecond = null;

    @Override
    public void onClick(View vi) {
        if (isPause) {
            Toast.makeText(getContext(), "游戏暂停中", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView v = (ImageView) vi;
        /* 如果正在运行动画，点击无效 */
        if (isAniming) {
            return;
        }

        /* 连续两次点击同一张图片表示取消选择
            连续两次点击相同时，自身与自身交换
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        */
        if (mFirst == null) {
            mFirst = v;
            /* #55FF0000 前2位表示透明度。00：完全透明，FF：完全不透明。后6位表示颜色 */
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));

        } else {
            mSecond = v;
            exchangeView();
        }

    }

    /* 动画层布局，用于两张图片交换时的动画显示 */
    private RelativeLayout mAnimLayout;
    private boolean isAniming = false;

    /**
     * 交换两张图片
     */
    private void exchangeView() {

        mFirst.setColorFilter(null);

        final String mFirstTag = (String) mFirst.getTag();
        final String mSecondTag = (String) mSecond.getTag();

        /* 构造动画层 */
        setUpAnimLayout();

        /* 设置第一张图片 */
        ImageView first = new ImageView(getContext());
        first.setImageBitmap(mItemBitmaps.get(getImageIdByTag(mFirstTag)).getBitmap());
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        /* 将第一张图片加入到动画层 */
        first.setLayoutParams(lp);
        mAnimLayout.addView(first);

        /* 设置第二张图片 */
        ImageView second = new ImageView(getContext());
        second.setImageBitmap(mItemBitmaps.get(getImageIdByTag(mSecondTag)).getBitmap());
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        /* 将第二张图片加入到动画层 */
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        /* 设置第一张图片的动画效果 */
        TranslateAnimation animFirst = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(),
                0, mSecond.getTop() - mFirst.getTop());
        animFirst.setDuration(300);
        animFirst.setFillAfter(true);
        first.startAnimation(animFirst);

        /* 设置第二张图片的动画效果 */
        TranslateAnimation animSecond = new TranslateAnimation(0,
                -(mSecond.getLeft() - mFirst.getLeft()), 0, -(mSecond.getTop() - mFirst.getTop()));
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);

        /* 设置动画监听 */
        animFirst.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);

                isAniming = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFirst.setImageBitmap(mItemBitmaps.get(getImageIdByTag(mSecondTag)).getBitmap());
                mSecond.setImageBitmap(mItemBitmaps.get(getImageIdByTag(mFirstTag)).getBitmap());

                mFirst.setTag(mSecondTag);
                mSecond.setTag(mFirstTag);

                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);
                mFirst = mSecond = null;

                mAnimLayout.removeAllViews();
                /* 判断游戏是否成功 */
                checkSuccess();
                isAniming = false;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /**
     * 判断拼图是否成功，成功返回true，否则返回false
     *
     * @return 游戏结果
     */
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < mGamePintuItems.length; i++) {
            ImageView iv = mGamePintuItems[i];
            if (getImageIndexByTag((String) iv.getTag()) != i) {
                isSuccess = false;
                break;
            }
        }
        if (isSuccess) {
            isGameSuccess = true;
            mHandler.removeMessages(TIME_CHANGE);
            Toast.makeText(getContext(), "Success ， level up !!!",
                    Toast.LENGTH_LONG).show();
            mHandler.sendEmptyMessage(NEXT_LEVEL);
        }

    }

    private int getImageIdByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    private int getImageIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    /**
     * 构造动画层
     */
    private void setUpAnimLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }

}
