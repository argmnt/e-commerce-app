package com.kodgemisi.course.ecommerce.buying;

import com.kodgemisi.course.ecommerce.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created on August, 2018
 *
 * @author yagiz
 */
@Component
@AllArgsConstructor
public class PaymentInfoValidator implements Validator {

	private final UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(PaymentInfo.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PaymentInfo paymentInfo = (PaymentInfo) target;
		if (!userService.existsByUsername(paymentInfo.getUsername())) {
			errors.rejectValue("username", "error.notExist");
		}
	}
}
