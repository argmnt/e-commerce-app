package com.kodgemisi.course.ecommerce.buying;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * Created on August, 2018
 *
 * @author yagiz
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PaymentInfo {

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	private String username;

	@Pattern(regexp = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}", message = "{error.paymentInfo.invalidPhoneNumber}" )
	private String phone;

	private String email;

	private String address;

	@Transient
	@Pattern(regexp = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" + "(?<mastercard>5[1-5][0-9]{14})|" + "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|"
			+ "(?<amex>3[47][0-9]{13})|" + "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|"
			+ "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$", message = "{error.paymentInfo.invalidCreditCard}")
	private String creditCard;

}
