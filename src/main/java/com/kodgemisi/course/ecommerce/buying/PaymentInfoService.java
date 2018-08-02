package com.kodgemisi.course.ecommerce.buying;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created on August, 2018
 *
 * @author yagiz
 */
@Service
@AllArgsConstructor
public class PaymentInfoService {

	private final PaymentInfoRepository paymentInfoRepository;

	void save(PaymentInfo paymentInfo) {
		paymentInfoRepository.save(paymentInfo);
	}

}
