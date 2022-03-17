/**
 *
 */
package com.mobatia.vkcsalesapp.model;



/**
 * @author Archana.S
 */

public class CartModel {


    String prodId;
    String prodName;
    String prodSize;
    String prodColor;
    String prodQuantity;
    String prodGridValue;
    String prodSizeId;
    String prodColorId;
    String pid;
    String sapId;
    String catId;
    String status;

    public CartModel() {
    }

    public CartModel(String prodId, String prodName, String prodSize, String prodColor, String prodQuantity, String prodGridValue, String prodSizeId, String prodColorId, String pid, String sapId, String catId, String status) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodSize = prodSize;
        this.prodColor = prodColor;
        this.prodQuantity = prodQuantity;
        this.prodGridValue = prodGridValue;
        this.prodSizeId = prodSizeId;
        this.prodColorId = prodColorId;
        this.pid = pid;
        this.sapId = sapId;
        this.catId = catId;
        this.status = status;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdSize() {
        return prodSize;
    }

    public void setProdSize(String prodSize) {
        this.prodSize = prodSize;
    }

    public String getProdColor() {
        return prodColor;
    }

    public void setProdColor(String prodColor) {
        this.prodColor = prodColor;
    }

    public String getProdQuantity() {
        return prodQuantity;
    }

    public void setProdQuantity(String prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

    public String getProdGridValue() {
        return prodGridValue;
    }

    public void setProdGridValue(String prodGridValue) {
        this.prodGridValue = prodGridValue;
    }

    public String getProdSizeId() {
        return prodSizeId;
    }

    public void setProdSizeId(String prodSizeId) {
        this.prodSizeId = prodSizeId;
    }

    public String getProdColorId() {
        return prodColorId;
    }

    public void setProdColorId(String prodColorId) {
        this.prodColorId = prodColorId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSapId() {
        return sapId;
    }

    public void setSapId(String sapId) {
        this.sapId = sapId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
