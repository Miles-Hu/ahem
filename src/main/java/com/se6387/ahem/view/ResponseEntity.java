package com.se6387.ahem.view;


import lombok.Data;

/**
 * @author Miles
 * @create 2022-07-23 3:41 PM
 * @email miles.j.hoo@gmail.com
 **/

@Data
public class    ResponseEntity<E> {

    private Integer code;

    private String message;

    private E data;

    public ResponseEntity<E> success(E data) {
        code = 1;
        message = "success";
        this.data = data;
        return this;
    }

    public ResponseEntity<E> fail(String message) {
        code = 0;
        this.message = message;
        if (message == null || message.isEmpty()) {
            message = "fail";
        }
        return this;
    }
}
