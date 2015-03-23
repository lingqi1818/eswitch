package org.codeanywhere.demo.web.pojo;

/**
 * 图书展示类
 * 
 * @author chenke
 */
/*
 * writer.write("doubanUrl:" + doubanUrl + "\r\n"); writer.write("img:" + img +
 * "\r\n"); writer.write("title:" + URLEncoder.encode(title, charset) + "\r\n");
 * writer.write("comment:" + URLEncoder.encode(comment, charset) + "\r\n");
 * writer.write("author:" + URLEncoder.encode(author, charset) + "\r\n");
 * writer.write("downloadUrl:" + URLEncoder.encode(downloadUrl, charset) +
 * "\r\n"); writer.write("kw:" + URLEncoder.encode(kw, charset) + "\r\n");
 */
public class Book {
    private String title;      //书名
    private String downloadUrl; //下载链接
    private String comment;    //评论
    private String img;        //图片
    private String kw;         //类型
    private String author;     //作者
    private String doubanUrl;  //豆瓣地址

    public String getDoubanUrl() {
        return doubanUrl;
    }

    public void setDoubanUrl(String doubanUrl) {
        this.doubanUrl = doubanUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKw() {
        return kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
