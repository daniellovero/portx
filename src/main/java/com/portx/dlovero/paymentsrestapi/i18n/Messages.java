package com.portx.dlovero.paymentsrestapi.i18n;


// As we only support English we put all the strings here, but it will be easier to extract them or i18nalize later.
public class Messages {
    public static class Exceptions {
        public static final String INVALID_UUID_VERSION = "Invalid UUID version. UUID version is 4 and variant 2";
        public static final String PAYMENT_ALREADY_EXISTS = "Payment already exists";

        public static String INVALID_FIELD_LESS_OR_EQUAL_TO_ZERO(String fieldName) {
            return "Field " + fieldName + " cannot be less than one";
        }

        public static String INVALID_CANNOT_BE_BLANK(String fieldName) {
            return "Field " + fieldName + " cannot be blank";
        }

        public static String INVALID_STATUS(String statusName) {
            return "Field " + statusName + " has an invalid a valid status";
        }
    }

    public class Api {
        public static final String GET_PAYMENT_OPERATION = "Get a payment by its ID";
        public static final String GET_PAYMENT_200_DESCRIPTION = "Payment found";
        public static final String GET_PAYMENT_400_DESCRIPTION = "Invalid id supplied";
        public static final String GET_PAYMENT_404_DESCRIPTION = "Payment not found";
        public static final String GET_PAYMENT_ID_PARAMETER_DESCRIPTION = "ID of the payment to seek for";
        public static final String POST_PAYMENT_OPERATION = "Create a payment";
        public static final String POST_PAYMENT_202_DESCRIPTION = "Payment accepted";
        public static final String POST_PAYMENT_400_DESCRIPTION = "Invalid body for payment supplied. Or invalid idempotent key";
        public static final String POST_PAYMENT_500_DESCRIPTION = "Payment already exists. Or error while creating payment";
        public static final String POST_PAYMENT_BODY_DESCRIPTION = "Payment to create";
        public static final String POST_PAYMENT_IDEMPOTENT_KEY_DESCRIPTION = "UUID version 4";
    }
}
