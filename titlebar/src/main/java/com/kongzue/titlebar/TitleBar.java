package com.kongzue.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.titlebar.interfaces.OnBackPressed;
import com.kongzue.titlebar.interfaces.OnRightButtonPressed;
import com.kongzue.titlebar.interfaces.OnTitleBarDoubleClick;
import com.kongzue.titlebar.util.StatusBarUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/10/3 21:55
 */
public class TitleBar extends LinearLayout {
    
    private OnBackPressed onBackPressed;
    private OnRightButtonPressed onRightButtonPressed;
    private OnTitleBarDoubleClick onTitleBarDoubleClick;
    
    private int height = 144;
    private int mainColor = -1;
    private int titleColor = -1;
    private int tipColor = -1;
    private String title;
    private String tip;
    private int leftButtonImage = -1;
    private int rightButtonImage = -1;
    private boolean statusBar;
    private int splitLineColor = -1;
    private int titleSize = -1;
    private int tipSize = -1;
    private int buttonTextSize = -1;
    private int gravity = 0;
    private String backText;
    private String rightText;
    private boolean titleBold;
    private boolean noBackButton;
    private float backgroundAlpha = 1f;
    
    public enum GravityValue {
        LEFT,
        CENTER,
        RIGHT
    }
    
    private Drawable bkgDrawable;
    private Context context;
    
    public TitleBar(Context context) {
        super(context);
        this.context = context;
        init();
    }
    
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        loadAttrs(context, attrs);
    }
    
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
        loadAttrs(context, attrs);
    }
    
    //读取属性
    private void loadAttrs(Context context, AttributeSet attrs) {
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.titleBar);
            height = typedArray.getLayoutDimension(R.styleable.titleBar_android_layout_height, -2);
            mainColor = typedArray.getColor(R.styleable.titleBar_mainColor, mainColor);
            titleColor = typedArray.getColor(R.styleable.titleBar_titleColor, titleColor);
            tipColor = typedArray.getColor(R.styleable.titleBar_tipColor, tipColor);
            title = typedArray.getNonResourceString(R.styleable.titleBar_title);
            tip = typedArray.getNonResourceString(R.styleable.titleBar_tip);
            leftButtonImage = typedArray.getResourceId(R.styleable.titleBar_leftButtonImage, leftButtonImage);
            rightButtonImage = typedArray.getResourceId(R.styleable.titleBar_rightButtonImage, rightButtonImage);
            statusBar = typedArray.getBoolean(R.styleable.titleBar_statusBarTransparent, statusBar);
            splitLineColor = typedArray.getColor(R.styleable.titleBar_splitLineColor, splitLineColor);
            titleSize = typedArray.getDimensionPixelOffset(R.styleable.titleBar_titleSize, titleSize);
            tipSize = typedArray.getDimensionPixelOffset(R.styleable.titleBar_tipSize, tipSize);
            buttonTextSize = typedArray.getDimensionPixelOffset(R.styleable.titleBar_buttonTextSize, buttonTextSize);
            gravity = typedArray.getInt(R.styleable.titleBar_gravity, GravityValue.LEFT.ordinal());
            backText = typedArray.getNonResourceString(R.styleable.titleBar_backText);
            rightText = typedArray.getNonResourceString(R.styleable.titleBar_rightText);
            noBackButton = typedArray.getBoolean(R.styleable.titleBar_noBackButton, noBackButton);
            titleBold = typedArray.getBoolean(R.styleable.titleBar_titleBold, titleBold);
            backgroundAlpha = typedArray.getFloat(R.styleable.titleBar_backgroundAlpha, backgroundAlpha);
            
            typedArray.recycle();
        } catch (Exception e) {
        }
        
        refreshView();
    }
    
    //刷新界面
    private void refreshView() {
        if (isAlreadyInit) {
            if (noBackButton) {
                btnBack.setVisibility(GONE);
                txtTitle.setPadding(dp2px(30),0,0,0);
                boxBody.setPadding(dp2px(15), 0, 0, 0);
            } else {
                txtTitle.setPadding(0,0,0,0);
                boxBody.setPadding(0, 0, 0, 0);
                if (leftButtonImage != -1) {
                    btnBack.setVisibility(VISIBLE);
                    btnBack.setImageResource(leftButtonImage);
                } else {
                    btnBack.setVisibility(INVISIBLE);
                }
            }
            
            if (rightButtonImage != -1) {
                btnMore.setVisibility(VISIBLE);
                btnMore.setImageResource(rightButtonImage);
            } else {
                btnMore.setVisibility(INVISIBLE);
            }
            
            if (mainColor != -1) {
                txtTitle.setTextColor(mainColor);
                setImageViewColor(btnBack, mainColor);
                setImageViewColor(btnMore, mainColor);
                
                txtBack.setTextColor(mainColor);
                txtMore.setTextColor(mainColor);
                
                if (bkgDrawable instanceof ColorDrawable) {
                    ColorDrawable colordDrawable = (ColorDrawable) bkgDrawable;
                    int color = colordDrawable.getColor();
                    int r = (color & 0xff0000) >> 16;
                    int g = (color & 0x00ff00) >> 8;
                    int b = (color & 0x0000ff);
                    if (isDeepColor(r, g, b)) {
                        StatusBarUtil.setTranslucentStatus((Activity) context, true, false);
                    } else {
                        StatusBarUtil.setTranslucentStatus((Activity) context, true, true);
                    }
                }
            } else {
                //根据背景颜色判断适用深色主题还是浅色主题
                boxBkg.setBackground(bkgDrawable);
                if (bkgDrawable instanceof ColorDrawable) {
                    ColorDrawable colordDrawable = (ColorDrawable) bkgDrawable;
                    int color = colordDrawable.getColor();
                    int r = (color & 0xff0000) >> 16;
                    int g = (color & 0x00ff00) >> 8;
                    int b = (color & 0x0000ff);
                    if (isDeepColor(r, g, b)) {
                        txtTitle.setTextColor(getResources().getColor(R.color.colorWhite));
                        
                        txtBack.setTextColor(getResources().getColor(R.color.colorWhite));
                        txtMore.setTextColor(getResources().getColor(R.color.colorWhite));
                        
                        txtTip.setTextColor(getResources().getColor(R.color.colorWhiteAlpha50));
                        setImageViewColor(btnBack, getResources().getColor(R.color.colorWhite));
                        setImageViewColor(btnMore, getResources().getColor(R.color.colorWhite));
                        
                        if (statusBar) {
                            try {
                                StatusBarUtil.setTranslucentStatus((Activity) context, true, false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        txtTitle.setTextColor(getResources().getColor(R.color.colorBlack));
                        
                        txtBack.setTextColor(getResources().getColor(R.color.colorBlack));
                        txtMore.setTextColor(getResources().getColor(R.color.colorBlack));
                        
                        txtTip.setTextColor(getResources().getColor(R.color.colorBlackAlpha50));
                        setImageViewColor(btnBack, getResources().getColor(R.color.colorBlack));
                        setImageViewColor(btnMore, getResources().getColor(R.color.colorBlack));
                        
                        if (statusBar) {
                            try {
                                StatusBarUtil.setTranslucentStatus((Activity) context, true, true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (statusBar) {
                int statusBarHeight = StatusBarUtil.getStatusBarHeight(context);
                int newHeight = height + statusBarHeight;
                
                boxBody.setY(statusBarHeight);
                
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, newHeight);
                rootView.setLayoutParams(params);
                
                RelativeLayout.LayoutParams bodyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
                boxBody.setLayoutParams(bodyParams);
            }
            
            if (tipColor != -1) {
                txtTip.setTextColor(tipColor);
            }
            
            if (titleColor != -1) {
                txtTitle.setTextColor(titleColor);
            }
            
            if (titleBold) {
                txtTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            
            if (title != null) {
                txtTitle.setText(title);
            }
            
            if (!isNull(tip)) {
                txtTip.setVisibility(VISIBLE);
                txtTip.setText(tip);
            } else {
                txtTip.setVisibility(GONE);
            }
            
            if (splitLineColor != -1) {
                splitLine.setVisibility(VISIBLE);
                splitLine.setBackgroundColor(splitLineColor);
            } else {
                splitLine.setVisibility(GONE);
            }
            
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            txtTip.setTextSize(TypedValue.COMPLEX_UNIT_PX, tipSize);
            txtBack.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);
            txtMore.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);
            
            if (gravity == GravityValue.LEFT.ordinal()) {
                txtTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                txtTip.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else if (gravity == GravityValue.CENTER.ordinal()) {
                txtTitle.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                txtTip.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            } else if (gravity == GravityValue.RIGHT.ordinal()) {
                txtTitle.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                txtTip.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
            
            
            if (!isNull(backText)) {
                txtBack.setVisibility(VISIBLE);
                txtMore.setVisibility(INVISIBLE);
                txtBack.setText(backText);
            } else {
                txtBack.setText("");
            }
            
            if (!isNull(rightText)) {
                txtBack.setVisibility(VISIBLE);
                txtMore.setVisibility(VISIBLE);
                txtMore.setText(rightText);
            } else {
                txtMore.setText("");
                if (isNull(backText)) {
                    txtBack.setVisibility(GONE);
                    txtMore.setVisibility(GONE);
                }
            }
            
            boxBkg.setAlpha(backgroundAlpha);
            
            setEvents();
        }
    }
    
    private boolean isDoubleClick = false;
    
    private void setEvents() {
        if (leftButtonImage != -1) {
            btnBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnBack.getVisibility() != VISIBLE) return;
                    if (onBackPressed != null) {
                        onBackPressed.onBackPressed();
                    } else {
                        try {
                            ((Activity) context).onBackPressed();
                        } catch (Exception e) {
                        }
                    }
                }
            });
        }
        
        txtBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBack.callOnClick();
            }
        });
        
        if (rightButtonImage != -1) {
            btnMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnMore.getVisibility() != VISIBLE) return;
                    if (onRightButtonPressed != null) {
                        onRightButtonPressed.onRightButtonPressed();
                    }
                }
            });
            txtMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnMore.callOnClick();
                }
            });
        }
        
        if (onTitleBarDoubleClick != null) {
            boxBody.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isDoubleClick) {
                        onTitleBarDoubleClick.onDoubleClick();
                        isDoubleClick = false;
                    } else {
                        isDoubleClick = true;
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                isDoubleClick = false;
                            }
                        }, 1000);
                    }
                }
            });
        }
    }
    
    private View rootView;
    
    private RelativeLayout boxBkg;
    private View splitLine;
    private LinearLayout boxBody;
    private ImageView btnBack;
    private TextView txtBack;
    private TextView txtTitle;
    private TextView txtTip;
    private TextView txtMore;
    private ImageView btnMore;
    
    private boolean isAlreadyInit = false;
    
    //初始化
    private void init() {
        LayoutParams rootParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(rootParams);
        
        //默认属性值
        mainColor = -1;
        tipColor = -1;
        title = "TitleBar";
        tip = null;
        leftButtonImage = R.mipmap.img_back;
        rightButtonImage = -1;
        statusBar = false;
        splitLineColor = -1;
        titleSize = dp2px(22);
        tipSize = dp2px(12);
        buttonTextSize = dp2px(18);
        
        //初始化
        if (context != null) {
            removeAllViews();
            rootView = LayoutInflater.from(context).inflate(R.layout.layout_title_bar, null, false);
            
            boxBkg = rootView.findViewById(R.id.box_bkg);
            splitLine = rootView.findViewById(R.id.split_line);
            boxBody = rootView.findViewById(R.id.box_body);
            btnBack = rootView.findViewById(R.id.btn_back);
            txtBack = rootView.findViewById(R.id.txt_back);
            txtTitle = rootView.findViewById(R.id.txt_title);
            txtTip = rootView.findViewById(R.id.txt_tip);
            txtMore = rootView.findViewById(R.id.txt_more);
            btnMore = rootView.findViewById(R.id.btn_more);
            
            addView(rootView);
            
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            rootView.setLayoutParams(params);
            
            isAlreadyInit = true;
        }
    }
    
    protected void setImageViewColor(ImageView view, int color) {
        try {
            Drawable modeDrawable = view.getDrawable().mutate();
            Drawable temp = DrawableCompat.wrap(modeDrawable);
            ColorStateList colorStateList = ColorStateList.valueOf(color);
            DrawableCompat.setTintList(temp, colorStateList);
            view.setImageDrawable(temp);
        } catch (Exception e) {
        
        }
    }
    
    public boolean isDeepColor(int r, int g, int b) {
        if (r * 0.299 + g * 0.578 + b * 0.114 >= 192) { //浅色
            return false;
        } else {  //深色
            return true;
        }
    }
    
    @Override
    public void setBackgroundColor(int color) {
        bkgDrawable = new ColorDrawable(color);
        refreshView();
    }
    
    @Override
    public void setBackgroundResource(int resid) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bkgDrawable = context.getDrawable(resid);
        }
        refreshView();
    }
    
    @Override
    public void setBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bkgDrawable = background;
        }
        refreshView();
    }
    
    private boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null")) {
            return true;
        }
        return false;
    }
    
    private int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
    
    private float px2dp(int pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (context != null) {
            if (statusBar) {
                int newHeight = height + StatusBarUtil.getStatusBarHeight(context);
                setMeasuredDimension(getMeasuredWidth(), newHeight);//设置宽高
            }
        }
    }
    
    public OnBackPressed getOnBackPressed() {
        return onBackPressed;
    }
    
    public TitleBar setOnBackPressed(OnBackPressed onBackPressed) {
        this.onBackPressed = onBackPressed;
        setEvents();
        return this;
    }
    
    public OnRightButtonPressed getOnRightButtonPressed() {
        return onRightButtonPressed;
    }
    
    public TitleBar setOnRightButtonPressed(OnRightButtonPressed onRightButtonPressed) {
        this.onRightButtonPressed = onRightButtonPressed;
        setEvents();
        return this;
    }
    
    public OnTitleBarDoubleClick getOnTitleBarDoubleClick() {
        return onTitleBarDoubleClick;
    }
    
    public TitleBar setOnTitleBarDoubleClick(OnTitleBarDoubleClick onTitleBarDoubleClick) {
        this.onTitleBarDoubleClick = onTitleBarDoubleClick;
        setEvents();
        return this;
    }
    
    public int getMainColor() {
        return mainColor;
    }
    
    public TitleBar setMainColor(@ColorInt int mainColor) {
        this.mainColor = mainColor;
        refreshView();
        return this;
    }
    
    public int getTipColor() {
        return tipColor;
    }
    
    public TitleBar setTipColor(@ColorInt int tipColor) {
        this.tipColor = tipColor;
        refreshView();
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public TitleBar setTitle(String title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public String getTip() {
        return tip;
    }
    
    public TitleBar setTip(String tip) {
        this.tip = tip;
        refreshView();
        return this;
    }
    
    public int getLeftButtonImage() {
        return leftButtonImage;
    }
    
    public TitleBar setLeftButtonImage(@DrawableRes int leftButtonImage) {
        this.leftButtonImage = leftButtonImage;
        refreshView();
        return this;
    }
    
    public int getRightButtonImage() {
        return rightButtonImage;
    }
    
    public TitleBar setRightButtonImage(@DrawableRes int rightButtonImage) {
        this.rightButtonImage = rightButtonImage;
        refreshView();
        return this;
    }
    
    public boolean isStatusBarTransparent() {
        return statusBar;
    }
    
    public TitleBar setStatusBarTransparent(boolean on) {
        this.statusBar = on;
        refreshView();
        return this;
    }
    
    public int getSplitLineColor() {
        return splitLineColor;
    }
    
    public TitleBar setSplitLineColor(@ColorInt int splitLineColor) {
        this.splitLineColor = splitLineColor;
        refreshView();
        return this;
    }
    
    public int getTitleSize() {
        return titleSize;
    }
    
    public TitleBar setTitleSize(int titleSize) {
        this.titleSize = titleSize;
        refreshView();
        return this;
    }
    
    public int getTipSize() {
        return tipSize;
    }
    
    public TitleBar setTipSize(int tipSize) {
        this.tipSize = tipSize;
        refreshView();
        return this;
    }
    
    public int getButtonTextSize() {
        return buttonTextSize;
    }
    
    public TitleBar setButtonTextSize(int buttonTextSize) {
        this.buttonTextSize = buttonTextSize;
        refreshView();
        return this;
    }
    
    public int getGravity() {
        return gravity;
    }
    
    /**
     * 文字排版：居左、居中、居右
     *
     * @param gravity {@link GravityValue#LEFT }{@link GravityValue#CENTER }{@link GravityValue#RIGHT }
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
        refreshView();
    }
    
    public String getBackText() {
        return backText;
    }
    
    public TitleBar setBackText(String backText) {
        this.backText = backText;
        refreshView();
        return this;
    }
    
    public String getRightText() {
        return rightText;
    }
    
    public TitleBar setRightText(String rightText) {
        this.rightText = rightText;
        refreshView();
        return this;
    }
    
    public boolean isNoBackButton() {
        return noBackButton;
    }
    
    public TitleBar setNoBackButton(boolean noBackButton) {
        this.noBackButton = noBackButton;
        refreshView();
        return this;
    }
    
    public float getBackgroundAlpha() {
        return backgroundAlpha;
    }
    
    public TitleBar setBackgroundAlpha(float backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
        boxBkg.setAlpha(backgroundAlpha);
        return this;
    }
}
