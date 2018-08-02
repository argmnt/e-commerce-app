package com.kodgemisi.course.ecommerce.buying;

import com.kodgemisi.course.ecommerce.cart.CartItem;
import com.kodgemisi.course.ecommerce.exceptions.OutOfStockException;
import com.kodgemisi.course.ecommerce.exceptions.ResourceNotFoundException;
import com.kodgemisi.course.ecommerce.product.Product;
import com.kodgemisi.course.ecommerce.product.ProductService;
import com.kodgemisi.course.ecommerce.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
class BuyingService {

    private final ProductService productService;

    private final BuyingRepository buyingRepository;

    private final SellingProductRepository sellingProductRepository;

    void save(Buying buying) {
        buyingRepository.save(buying);
    }

    Buying createNewBuying(User user, Set<SellingProduct> sellingProducts, PaymentType paymentType, PaymentInfo paymentInfo) {
        Buying buying = new Buying(user, paymentInfo, sellingProducts, paymentType);
        this.save(buying);
        return buying;
    }

    Buying findById(Long id) {
        return buyingRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    Set<SellingProduct> createSellingProducts(List<CartItem> items) throws OutOfStockException {
        return items.stream().map(item -> {
            Product product = productService.findById(item.getProductId());
            if (product.getStock() < item.getCount()) {
                // todo: throw new Exception
                throw new OutOfStockException();
            }
            SellingProduct selling = new SellingProduct(item.getCount(), product);
            sellingProductRepository.save(selling);
            return selling;
        })
        .collect(Collectors.toSet());
    }

    @Transactional
    void setBuyingStatus (Buying buying, BuyingStatus buyingStatus) {
        Buying persistedBuying = this.findById(buying.getId());
        persistedBuying.setBuyingStatus(buyingStatus);
    }

}
