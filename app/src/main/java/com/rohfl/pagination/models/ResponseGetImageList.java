package com.rohfl.pagination.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGetImageList{

	@SerializedName("ResponseGetImageList")
	private List<ResponseGetImageListItem> responseGetImageList;

	public List<ResponseGetImageListItem> getResponseGetImageList(){
		return responseGetImageList;
	}
}