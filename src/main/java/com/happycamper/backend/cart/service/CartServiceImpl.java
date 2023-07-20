package com.happycamper.backend.cart.service;

import com.happycamper.backend.cart.controller.form.AddCartItemRequestForm;
import com.happycamper.backend.cart.entity.Cart;
import com.happycamper.backend.cart.entity.CartItem;
import com.happycamper.backend.cart.repository.CartItemRepository;
import com.happycamper.backend.cart.repository.CartRepository;
import com.happycamper.backend.cart.service.response.CartItemListResponseForm;
import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.repository.MemberRepository;
import com.happycamper.backend.product.entity.Product;
import com.happycamper.backend.product.entity.ProductOption;
import com.happycamper.backend.product.repository.ProductOptionRepository;
import com.happycamper.backend.product.repository.ProductRepository;
import com.happycamper.backend.utility.transform.TransformToDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    final private MemberRepository memberRepository;
    final private ProductRepository productRepository;
    final private ProductOptionRepository productOptionRepository;
    final private CartRepository cartRepository;
    final private CartItemRepository cartItemRepository;
    @Override
    public List<CartItemListResponseForm> add(String email, AddCartItemRequestForm requestForm) {

        // 사용자의 토큰으로 사용자 특정하기
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            log.info("사용자 확인 불가");
            return null;
        }
        Member member = maybeMember.get();

        // 사용자가 선택한 값으로 상품, 상품 옵션 찾기
        Optional<Product> maybeProduct = productRepository.findById(requestForm.getProductId());
        Optional<ProductOption> maybeProductOption = productOptionRepository.findById(requestForm.getProductOptionId());

        // 못찾으면 null 반환
        if(maybeProduct.isEmpty() || maybeProductOption.isEmpty()) {
            return null;
        }

        Product product = maybeProduct.get();
        ProductOption productOption = maybeProductOption.get();

        // 사용자의 카트 찾기
        Optional<Cart> maybeCart = cartRepository.findByMember(member);

        if(maybeCart.isEmpty()) {
            Cart cart = new Cart();

            List<CartItem> cartItemList = new ArrayList<>();

            CartItem cartItem = new CartItem(
                    TransformToDate.transformToDate(requestForm.getCheckInDate()), TransformToDate.transformToDate(requestForm.getCheckOutDate()),
                    requestForm.getAmount(), requestForm.getAmount() * maybeProductOption.get().getOptionPrice(),
                    cart, product, productOption
            );

            cartItemList.add(cartItem);

            cart.setCount(cartItemList.size());
            cart.setMember(member);
            cartRepository.save(cart);
            cartItemRepository.save(cartItem);

            List<CartItemListResponseForm> responseFormList = new ArrayList<>();

            for(CartItem cartItem1: cartItemList) {
                CartItemListResponseForm responseForm = new CartItemListResponseForm(
                        cartItem1.getId(),
                        cartItem1.getProduct().getId(),
                        cartItem1.getProduct().getProductName(),
                        cartItem.getProductOption().getId(),
                        cartItem1.getProductOption().getOptionName(),
                        cartItem1.getPayment(),
                        cartItem1.getCheckInDate(),
                        cartItem1.getCheckOutDate()
                );
                responseFormList.add(responseForm);
            }

            return responseFormList;
        }

        if(maybeCart.isPresent()) {
            Cart cart = maybeCart.get();

            List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);

            CartItem cartItem = new CartItem(
                    TransformToDate.transformToDate(requestForm.getCheckInDate()), TransformToDate.transformToDate(requestForm.getCheckOutDate()),
                    requestForm.getAmount(), requestForm.getAmount() * maybeProductOption.get().getOptionPrice(),
                    cart, product, productOption
            );

            cartItemList.add(cartItem);
            cart.setCount(cartItemList.size());
            cart.setMember(member);
            cartRepository.save(cart);
            cartItemRepository.save(cartItem);

            List<CartItemListResponseForm> responseFormList = new ArrayList<>();

            for(CartItem cartItem1: cartItemList) {
                CartItemListResponseForm responseForm = new CartItemListResponseForm(
                        cartItem1.getId(),
                        cartItem1.getProduct().getId(),
                        cartItem1.getProduct().getProductName(),
                        cartItem.getProductOption().getId(),
                        cartItem1.getProductOption().getOptionName(),
                        cartItem1.getPayment(),
                        cartItem1.getCheckInDate(),
                        cartItem1.getCheckOutDate()
                );
                responseFormList.add(responseForm);
            }

            return responseFormList;
        }

        return null;
    }

    @Override
    public List<CartItemListResponseForm> delete(String email, Long id) {

        // 사용자의 토큰으로 사용자 특정하기
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            log.info("사용자 확인 불가");
            return null;
        }
        Member member = maybeMember.get();
        Optional<Cart> maybeCart = cartRepository.findByMember(member);
        if(maybeCart.isEmpty()) {
            log.info("카트가 없습니다.");
            return null;
        }

        Cart cart = maybeCart.get();

        List<CartItem> maybeCartItemList = cartItemRepository.findAllByCart(cart);

        for(CartItem cartItem: maybeCartItemList) {
            if(cartItem.getId().equals(id)) {
                cartItemRepository.deleteById(id);
                cart.setCount(cart.getCount() - 1);
            }
        }
        log.info("장바구니에서 상품 삭제 완료!");
        cartRepository.save(cart);

        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
        List<CartItemListResponseForm> responseFormList = new ArrayList<>();

        for(CartItem cartItem: cartItemList) {
            CartItemListResponseForm responseForm = new CartItemListResponseForm(
                    cartItem.getId(),
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getProductName(),
                    cartItem.getProductOption().getId(),
                    cartItem.getProductOption().getOptionName(),
                    cartItem.getPayment(),
                    cartItem.getCheckInDate(),
                    cartItem.getCheckOutDate()
            );
            responseFormList.add(responseForm);
        }
        return responseFormList;
    }

    @Override
    public List<CartItemListResponseForm> getMyCart(String email) {
        // 사용자의 토큰으로 사용자 특정하기
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            log.info("사용자 확인 불가");
            return null;
        }
        Member member = maybeMember.get();

        Optional<Cart> maybeCart = cartRepository.findByMember(member);
        if(maybeCart.isEmpty()) {
            log.info("카트가 없습니다.");
            return null;
        }

        Cart cart = maybeCart.get();
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
        List<CartItemListResponseForm> responseFormList = new ArrayList<>();

        for(CartItem cartItem: cartItemList) {
            CartItemListResponseForm responseForm = new CartItemListResponseForm(
                    cartItem.getId(),
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getProductName(),
                    cartItem.getProductOption().getId(),
                    cartItem.getProductOption().getOptionName(),
                    cartItem.getPayment(),
                    cartItem.getCheckInDate(),
                    cartItem.getCheckOutDate()
            );
            responseFormList.add(responseForm);
        }
        return responseFormList;
    }

    @Override
    public List<CartItemListResponseForm> deleteList(String email, List<Long> idList) {
        // 사용자의 토큰으로 사용자 특정하기
        Optional<Member> maybeMember = memberRepository.findByEmail(email);
        if(maybeMember.isEmpty()) {
            log.info("사용자 확인 불가");
            return null;
        }
        Member member = maybeMember.get();
        Optional<Cart> maybeCart = cartRepository.findByMember(member);
        if(maybeCart.isEmpty()) {
            log.info("카트가 없습니다.");
            return null;
        }

        Cart cart = maybeCart.get();

        List<CartItem> maybeCartItemList = cartItemRepository.findAllByCart(cart);

        for(CartItem cartItem: maybeCartItemList) {
            for(int i = 0; i < idList.size(); i++ ){
                if(cartItem.getId().equals(idList.get(i))) {
                    cartItemRepository.deleteById(idList.get(i));
                    cart.setCount(cart.getCount() - 1);
                }
            }
        }
        log.info("장바구니에서 상품 삭제 완료!");
        cartRepository.save(cart);

        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);
        List<CartItemListResponseForm> responseFormList = new ArrayList<>();

        for(CartItem cartItem: cartItemList) {
            CartItemListResponseForm responseForm = new CartItemListResponseForm(
                    cartItem.getId(),
                    cartItem.getProduct().getId(),
                    cartItem.getProduct().getProductName(),
                    cartItem.getProductOption().getId(),
                    cartItem.getProductOption().getOptionName(),
                    cartItem.getPayment(),
                    cartItem.getCheckInDate(),
                    cartItem.getCheckOutDate()
            );
            responseFormList.add(responseForm);
        }
        return responseFormList;
    }
}
