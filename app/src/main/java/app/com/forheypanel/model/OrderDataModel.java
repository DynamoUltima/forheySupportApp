package app.com.forheypanel.model;

/**
 * Created by nayram on 2/26/15.
 */

public class OrderDataModel {

    String server_code,pickupPoint,pickupDate,pickupFromTime,pickupToTime,deliveryDate,deliveryFromTime,deliveryToTime,status,created_at,order_type;
    String name,phone, location;

    public OrderDataModel(String server_code, String pickupPoint, String pickupDate, String pickupFromTime,
                          String pickupToTime, String deliveryDate, String deliveryFromTime, String deliveryToTime,
                          String status, String created_at,String order_type,String name,String phone,String location) {
        this.server_code = server_code;
        this.pickupPoint = pickupPoint;
        this.pickupDate = pickupDate;
        this.pickupFromTime = pickupFromTime;
        this.pickupToTime = pickupToTime;
        this.deliveryDate = deliveryDate;
        this.deliveryFromTime = deliveryFromTime;
        this.deliveryToTime = deliveryToTime;
        this.status = status;
        this.created_at = created_at;
        this.order_type=order_type;
        this.name=name;
        this.phone=phone;
        this.location=location;
    }

    public OrderDataModel() {
    }


    public OrderDataModel(String server_code, String pickupPoint, String pickupDate, String status, String created_at, String order_type) {
        this.server_code = server_code;
        this.pickupPoint = pickupPoint;
        this.pickupDate = pickupDate;
        this.status = status;
        this.created_at = created_at;
        this.order_type = order_type;
    }

    public OrderDataModel(String server_code, String pickupPoint, String pickupDate, String status, String created_at, String order_type, String name, String phone, String location) {
        this.server_code = server_code;
        this.pickupPoint = pickupPoint;
        this.pickupDate = pickupDate;
        this.status = status;
        this.created_at = created_at;
        this.order_type = order_type;
        this.name = name;
        this.phone = phone;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getServer_code() {
        return server_code;
    }

    public void setServer_code(String server_code) {
        this.server_code = server_code;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public String getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(String pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getPickupFromTime() {
        return pickupFromTime;
    }

    public void setPickupFromTime(String pickupFromTime) {
        this.pickupFromTime = pickupFromTime;
    }

    public String getPickupToTime() {
        return pickupToTime;
    }

    public void setPickupToTime(String pickupToTime) {
        this.pickupToTime = pickupToTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryFromTime() {
        return deliveryFromTime;
    }

    public void setDeliveryFromTime(String deliveryFromTime) {
        this.deliveryFromTime = deliveryFromTime;
    }

    public String getDeliveryToTime() {
        return deliveryToTime;
    }

    public void setDeliveryToTime(String deliveryToTime) {
        this.deliveryToTime = deliveryToTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
