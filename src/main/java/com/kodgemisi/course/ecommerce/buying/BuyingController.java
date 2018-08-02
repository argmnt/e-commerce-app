package com.kodgemisi.course.ecommerce.buying;

import com.kodgemisi.course.ecommerce.cart.CartItem;
import com.kodgemisi.course.ecommerce.cart.CartService;
import com.kodgemisi.course.ecommerce.exceptions.OutOfStockException;
import com.kodgemisi.course.ecommerce.product.ProductService;
import com.kodgemisi.course.ecommerce.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@PreAuthorize("hasRole('USER')")
@Controller
@AllArgsConstructor
@RequestMapping("/buy")
public class BuyingController {

    private final CartService cartService;

    private final BuyingService buyingService;

    private final ProductService productService;

    private final PaymentInfoService paymentInfoService;

    private final PaymentInfoValidator paymentInfoValidator;

    @InitBinder
    void addPaymentinfovalidator(WebDataBinder binder) {
        binder.addValidators(paymentInfoValidator);
    }

    @GetMapping("/checkout")
    String checkout(Model model) {
        List<CartItem> cartItems = cartService.getAllItems();
        BigDecimal total = BigDecimal.valueOf(0);
        for (CartItem cartItem : cartItems) {
            BigDecimal itemTotal = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf((cartItem.getCount())));
            total = total.add(itemTotal);
        }
        PaymentInfo paymentInfo = new PaymentInfo();
        model.addAttribute("paymentInfo", paymentInfo);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", total);
        return "buying/checkout";
    }

    @PostMapping
    String proceedPayment(@Valid PaymentInfo paymentInfo, BindingResult bindingResult, RedirectAttributes redirectAttributes, @AuthenticationPrincipal User user, Model model) {

        if (bindingResult.hasErrors()) {
            List<CartItem> cartItems = cartService.getAllItems();
            BigDecimal total = BigDecimal.valueOf(0);
            for (CartItem cartItem : cartItems) {
                BigDecimal itemTotal = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf((cartItem.getCount())));
                total = total.add(itemTotal);
            }
            model.addAttribute("paymentInfo", paymentInfo);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("cartTotal", total);
            return "buying/checkout";
        }

        // payment details
        paymentInfo.getAddress();
        List<CartItem> cartItems = cartService.getAllItems();

        // todo: handle exception
        Set<SellingProduct> sellingProducts;
        try {
            sellingProducts = buyingService.createSellingProducts(cartItems);
        }
        catch (OutOfStockException outOfStockException) {
            redirectAttributes.addFlashAttribute("outOfStockMessage", "Item is out of stock");
            return "redirect:/";
        }

        paymentInfoService.save(paymentInfo);

        Buying buying = buyingService.createNewBuying(user, sellingProducts, PaymentType.CREDIT_CARD, paymentInfo);


        productService.updateStockCounts(sellingProducts);

        // todo: update buying status
        buyingService.setBuyingStatus(buying, BuyingStatus.APPROVED);

        cartService.removeAllItems();

        // todo: set redirect attributes
        redirectAttributes.addFlashAttribute("paymentSuccessMessage", "Payment has successfully charged");

        return "redirect:/";
    }

}
