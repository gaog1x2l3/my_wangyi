package com.example.wynewsobject;

/**
 * 滚动广告对象
 * 
 * @author 何国有
 *
 */
public class HeadAdObject {
	// "ImgSrc":
	// "http://media.china-sss.com/pics/gallery/201510/1676fa4a-6725-47e2-8507-a56ece4f285e_201510300915.jpg",
	// "Link": "http://pages.springtour.com/Special/ms101022ssy/index.aspx",
	private String ImgSrc;// 图片的地址
	private String Link;// 点击图片时跳转的详细页地址

	public String getImgSrc() {
		return ImgSrc;
	}

	public void setImgSrc(String imgSrc) {
		ImgSrc = imgSrc;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public HeadAdObject(String imgSrc, String link) {
		super();
		ImgSrc = imgSrc;
		Link = link;
	}

	public HeadAdObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "HeadAdObject [ImgSrc=" + ImgSrc + ", Link=" + Link + "]";
	}
	

}
