package com.booktest.androidreader;

import com.kaixinbook.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class EditTextWithDelete extends EditText implements OnFocusChangeListener{

private Drawable imgEnable;

private Context context;

public EditTextWithDelete(Context context) {

super(context);

this.context = context;

init();

}

public EditTextWithDelete(Context context, AttributeSet attrs, int defStyle) {

super(context, attrs, defStyle);

this.context = context;

init();

}

public EditTextWithDelete(Context context, AttributeSet attrs) {

super(context, attrs);

this.context = context;

init();

}

private void init() {

//��ȡͼƬ��Դ

imgEnable = context.getResources().getDrawable(R.drawable.delete);

addTextChangedListener(new TextWatcher() {

 

@Override

public void onTextChanged(CharSequence s, int start, int before, int count) {

}

@Override

public void beforeTextChanged(CharSequence s, int start, int count,

int after) {

 

}

 

@Override

public void afterTextChanged(Editable s) {

setDrawable();

//Toast.makeText(context, getText(), 10).show();

}

});

setDrawable();

}

/**

* ����ɾ��ͼƬ

*/

private void setDrawable() {

if(length() == 0) {

setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

}else {

setCompoundDrawablesWithIntrinsicBounds(null, null, imgEnable, null);

}

}

/**
071.
* event.getX() ��ȡ���Ӧ�������Ͻǵ�X����
072.
* event.getY() ��ȡ���Ӧ�������Ͻǵ�Y����
073.
* getWidth() ��ȡ�ؼ��Ŀ��
074.
* getTotalPaddingRight() ��ȡɾ��ͼ�����Ե���ؼ��ұ�Ե�ľ���
075.
* getPaddingRight() ��ȡɾ��ͼ���ұ�Ե���ؼ��ұ�Ե�ľ���
076.
* getWidth() - getTotalPaddingRight() ����ɾ��ͼ�����Ե���ؼ����Ե�ľ���
077.
* getWidth() - getPaddingRight() ����ɾ��ͼ���ұ�Ե���ؼ����Ե�ľ���
078.
*/

@Override

public boolean onTouchEvent(MotionEvent event) {

if(imgEnable != null && event.getAction() == MotionEvent.ACTION_UP) {

int x = (int) event.getX() ;

//�жϴ������Ƿ���ˮƽ��Χ��

boolean isInnerWidth = (x > (getWidth() - getTotalPaddingRight())) &&

(x < (getWidth() - getPaddingRight()));

//��ȡɾ��ͼ��ı߽磬����һ��Rect����

Rect rect = imgEnable.getBounds();

//��ȡɾ��ͼ��ĸ߶�

int height = rect.height();

int y = (int) event.getY();

//����ͼ��ײ����ؼ��ײ��ľ���

int distance = (getHeight() - height) /2;

//�жϴ������Ƿ�����ֱ��Χ��(���ܻ��е����)

//���������������distance����distance+ͼ������ĸ߶ȣ�֮�ڣ�����Ϊ����ɾ��ͼ��

boolean isInnerHeight = (y > distance) && (y < (distance + height));

if(isInnerWidth && isInnerHeight) {

setText("");

}

}

return super.onTouchEvent(event);

}

@Override

protected void finalize() throws Throwable {

super.finalize();

}

@Override

public void onFocusChange(View v, boolean hasFocus) {

if(hasFocus) {

setDrawable();

}else {

setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

}

}

}