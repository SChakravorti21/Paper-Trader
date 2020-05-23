package com.finance.papertrader.api.requests.user;

import lombok.Data;

public class CreateUser {

    @Data
    public static class Request {
        private String username;
    }

    @Data
    public static class Response {
        private String username;

        public Response(String username) {
            this.username = username;
        }
    }
}
