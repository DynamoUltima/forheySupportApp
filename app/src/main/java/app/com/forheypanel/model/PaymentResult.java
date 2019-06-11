package app.com.forheypanel.model;

public class PaymentResult {
    String tag;
    int success;
    int order_num;
    String success_msg;

    public String getTag() {
        return tag;
    }

    public int getSuccess() {
        return success;
    }

    public int getOrder_num() {
        return order_num;
    }

    public String getSuccess_msg() {
        return success_msg;
    }
}
