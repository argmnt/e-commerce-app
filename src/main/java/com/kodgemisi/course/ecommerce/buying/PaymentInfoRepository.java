package com.kodgemisi.course.ecommerce.buying;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created on August, 2018
 *
 * @author yagiz
 */
@Repository
interface PaymentInfoRepository extends CrudRepository<PaymentInfo, Long> {

}
