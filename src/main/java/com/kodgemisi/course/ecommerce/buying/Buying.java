package com.kodgemisi.course.ecommerce.buying;

import com.kodgemisi.course.ecommerce.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Buying implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User buyer;

    // implement buying info
    @OneToOne
    private PaymentInfo paymentInfo;

    @OneToMany
    private Set<SellingProduct> sellingProducts;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentType paymentType;


    @Enumerated(EnumType.STRING)
    @NotNull
    private BuyingStatus buyingStatus;

    public Buying(User buyer, PaymentInfo paymentInfo, Set<SellingProduct> sellingProducts, @NotNull PaymentType paymentType) {
        this.buyer = buyer;
        this.paymentInfo = paymentInfo;
        this.sellingProducts = sellingProducts;
        this.paymentType = paymentType;
        this.buyingStatus = BuyingStatus.PREPARING;
    }


}
