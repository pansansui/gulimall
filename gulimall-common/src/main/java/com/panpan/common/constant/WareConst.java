package com.panpan.common.constant;



/**
 * @author panpan
 * @create 2021-08-25 下午5:29
 */
public class WareConst {

    public enum PurchaseStatusEnum{
        CREATED(0,"新建"),ALLOCATED(1,"已分配"),
        RECEIVE(2,"已领取"),COMPLETED(3,"已完成"),
        HASERROR(4,"有异常");
        private int code;
        private  String msg;

        PurchaseStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public enum PurchaseDetailStatusEnum{
        CREATED(0,"新建"),ALLOCATED(1,"已分配"),
        PURCHASEING(2,"正在采购"),COMPLETED(3,"已完成"),
        HASERROR(4,"采购失败");
        private int code;
        private  String msg;

        PurchaseDetailStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
