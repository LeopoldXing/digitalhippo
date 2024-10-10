package com.leopoldhsing.digitalhippo.model.vo;

import java.util.Objects;

public class ProductImageVo {
    private String filename;
    private Integer filesize;
    private Integer height;
    private Integer width;
    private String mimeType;
    private String fileType;
    private String url;

    public ProductImageVo() {
    }

    public ProductImageVo(String filename, Integer filesize, Integer height, Integer width, String mimeType, String fileType, String url) {
        this.filename = filename;
        this.filesize = filesize;
        this.height = height;
        this.width = width;
        this.mimeType = mimeType;
        this.fileType = fileType;
        this.url = url;
    }

    @Override
    public String toString() {
        return "ProductImageVo{" +
                "filename='" + filename + '\'' +
                ", filesize=" + filesize +
                ", height=" + height +
                ", width=" + width +
                ", mimeType='" + mimeType + '\'' +
                ", fileType='" + fileType + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getFilesize() {
        return filesize;
    }

    public void setFilesize(Integer filesize) {
        this.filesize = filesize;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductImageVo that = (ProductImageVo) o;
        return Objects.equals(filename, that.filename) && Objects.equals(filesize, that.filesize) && Objects.equals(height, that.height) && Objects.equals(width, that.width) && Objects.equals(mimeType, that.mimeType) && Objects.equals(fileType, that.fileType) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(filename);
        result = 31 * result + Objects.hashCode(filesize);
        result = 31 * result + Objects.hashCode(height);
        result = 31 * result + Objects.hashCode(width);
        result = 31 * result + Objects.hashCode(mimeType);
        result = 31 * result + Objects.hashCode(fileType);
        result = 31 * result + Objects.hashCode(url);
        return result;
    }
}
