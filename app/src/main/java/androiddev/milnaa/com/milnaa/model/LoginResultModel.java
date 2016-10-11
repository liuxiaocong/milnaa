package androiddev.milnaa.com.milnaa.model;

/**
 * Created by LiuXiaocong on 9/27/2016.
 */
public class LoginResultModel {

    /**
     * Code : 999
     * Message : Failed.
     */

    private ResponseBean Response;
    /**
     * DefaultFolder : null
     * Gender : null
     * SessionId : null
     * UserAvatar : null
     * UserCountry : null
     * UserDB : null
     * UserFirstName : null
     * UserFullName : null
     * UserId : null
     * UserLocation : null
     * UserName : null
     * UserWebServer : null
     * UserZip : null
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
        private Object DefaultFolder;
        private Object Gender;
        private Object SessionId;
        private Object UserAvatar;
        private Object UserCountry;
        private Object UserDB;
        private Object UserFirstName;
        private Object UserFullName;
        private Object UserId;
        private Object UserLocation;
        private Object UserName;
        private Object UserWebServer;
        private Object UserZip;

        public Object getDefaultFolder() {
            return DefaultFolder;
        }

        public void setDefaultFolder(Object DefaultFolder) {
            this.DefaultFolder = DefaultFolder;
        }

        public Object getGender() {
            return Gender;
        }

        public void setGender(Object Gender) {
            this.Gender = Gender;
        }

        public Object getSessionId() {
            return SessionId;
        }

        public void setSessionId(Object SessionId) {
            this.SessionId = SessionId;
        }

        public Object getUserAvatar() {
            return UserAvatar;
        }

        public void setUserAvatar(Object UserAvatar) {
            this.UserAvatar = UserAvatar;
        }

        public Object getUserCountry() {
            return UserCountry;
        }

        public void setUserCountry(Object UserCountry) {
            this.UserCountry = UserCountry;
        }

        public Object getUserDB() {
            return UserDB;
        }

        public void setUserDB(Object UserDB) {
            this.UserDB = UserDB;
        }

        public Object getUserFirstName() {
            return UserFirstName;
        }

        public void setUserFirstName(Object UserFirstName) {
            this.UserFirstName = UserFirstName;
        }

        public Object getUserFullName() {
            return UserFullName;
        }

        public void setUserFullName(Object UserFullName) {
            this.UserFullName = UserFullName;
        }

        public Object getUserId() {
            return UserId;
        }

        public void setUserId(Object UserId) {
            this.UserId = UserId;
        }

        public Object getUserLocation() {
            return UserLocation;
        }

        public void setUserLocation(Object UserLocation) {
            this.UserLocation = UserLocation;
        }

        public Object getUserName() {
            return UserName;
        }

        public void setUserName(Object UserName) {
            this.UserName = UserName;
        }

        public Object getUserWebServer() {
            return UserWebServer;
        }

        public void setUserWebServer(Object UserWebServer) {
            this.UserWebServer = UserWebServer;
        }

        public Object getUserZip() {
            return UserZip;
        }

        public void setUserZip(Object UserZip) {
            this.UserZip = UserZip;
        }
    }
}
