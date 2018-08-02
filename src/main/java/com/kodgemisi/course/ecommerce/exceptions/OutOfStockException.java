package com.kodgemisi.course.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created on August, 2018
 *
 * @author yagiz
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OutOfStockException extends RuntimeException {

}
