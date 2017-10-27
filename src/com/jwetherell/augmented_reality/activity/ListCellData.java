package com.jwetherell.augmented_reality.activity;

import android.content.Context;
import android.content.Intent;
/**
 *院系列表
 */
public class ListCellData {
	public ListCellData(Context context,String controlName,Intent relatedIntent) {
		this.controlName=controlName;
		this.context=context;
		this.relatedIntent=relatedIntent;
		}
		private String controlName="null";
	   public String getControlName() {
		return controlName;
	}
	   private Context context=null;
	   public Context getContext() {
		return context;
	}
	   private Intent relatedIntent=null;
	   public Intent getRelatedIntent() {
		return relatedIntent;
	}
	   public void startActivity(){
		   getContext().startActivity(getRelatedIntent());
	   }
	   @Override
	public String toString() {
		return getControlName();
	}
}
