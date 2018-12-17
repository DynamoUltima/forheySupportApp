package app.com.forheypanel.model;

/**
 * Created by nayram on 6/1/15.
 */
public class NotificationDataModel {
    String message,orderId,type,title,email,image,name;
    int id,state,client_id;

    public NotificationDataModel(String message, String orderId, int id, int state,String type,String title,String email,String image,
                                 String name,int client_id) {
        this.message = message;
        this.orderId = orderId;
        this.id = id;
        this.state = state;
        this.type=type;
        this.title=title;
        this.name=name;
        this.email=email;
        this.image=image;
        this.client_id=client_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NotificationDataModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
