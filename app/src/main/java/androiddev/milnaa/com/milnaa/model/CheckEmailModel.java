package androiddev.milnaa.com.milnaa.model;

/**
 * Created by LiuXiaocong on 9/27/2016.
 */
public class CheckEmailModel {

    /**
     * Code : 0
     * Message : Available
     */

    private ResponseBean Response;
    /**
     * userName : Y
     */

    private ResultBean Result;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public ResultBean getResult() {
        return Result;
    }

    public void setResult(ResultBean Result) {
        this.Result = Result;
    }

    public static class ResponseBean {
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

    public static class ResultBean {
        private String userName;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
