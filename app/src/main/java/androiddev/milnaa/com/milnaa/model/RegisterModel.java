package androiddev.milnaa.com.milnaa.model;

/**
 * Created by LiuXiaocong on 9/19/2016.
 */
public class RegisterModel {

    /**
     * Code : 999
     * Message : Failed.
     */

    private StatusBean Status;

    public StatusBean getStatus() {
        return Status;
    }

    public void setStatus(StatusBean Status) {
        this.Status = Status;
    }

    public static class StatusBean {
        private int Code;
        private String Message;

        public int getCode() {
            return Code;
        }

        public void setCode(int Code) {
            this.Code = Code;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }
    }
}
