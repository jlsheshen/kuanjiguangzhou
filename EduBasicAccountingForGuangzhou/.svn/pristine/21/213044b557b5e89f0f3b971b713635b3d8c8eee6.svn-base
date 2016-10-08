package com.edu.basicaccountingforguangzhou.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

public class CustomerDialog extends Dialog {

	private int Rid;

	public CustomerDialog(Context context, int Rid) {
		super(context);
		this.Rid = Rid;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(Rid);

		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}

	@Override
	public void show() {
		super.show();
	}

}
