2016-5-30
lucher
------------------
具有缩放等功能的单据控件

----
说明：
键盘操作事件：
所有模式：PgUp 下一页，PgDn 上一页
答题模式下：left/up 上一空，right/down/tab/enter/pad_enter 下一空
非答题模式下：left 上一页，right 下一页


***必须在测试模式下才支持全部提交，否则会出现逻辑混乱
-----
技术积累：
输入法软件盘可能会遮挡EditText，使用adjustpan可使布局上移，从而显示出EditText
但是，如果当前EditText(gravity is center or center_horizontal)已经获取焦点了，再点击该EditText弹出输入法软件盘时，布局不会上移，从而导致EditText被遮挡
这个问题是Android的已知bug，在stackoverflow上有人提出过，可通过如下方法解决：
http://stackoverflow.com/questions/15317157/android-adjustpan-not-working-after-the-first-time
问：Android adjustpan not working after the first time
答：The problem you're experiencing is now a known platform bug as described here: https://code.google.com/p/android/issues/detail?id=182191
The fix is not backported and so will not take effect until Android N. Until Android N is your minimum SDK, the following solution is a workaround.
You can create a custom class extending EditText and override the method onKeyPreIme(int keyCode, KeyEvent event) like this:
@Override
public boolean onKeyPreIme(int keyCode, KeyEvent event)
{
   if(keyCode == KeyEvent.KEYCODE_BACK)
     {
         clearFocus();
     }
return super.onKeyPreIme(keyCode, event);
}
You should also ensure a higher level layout has the ability to be focused so the EditText's focus may be cleared successfully. The following attributes may work for you:

android:descendantFocusability="beforeDescendants"
android:focusableInTouchMode="true"
Now when the back key is pressed, the EditText loses the focus. Then when you tap the button again, adjustpan will work as intended. (Posting as an answer since the edit was refused.)