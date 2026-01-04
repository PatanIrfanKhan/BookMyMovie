package com.example.BookMyMovie.Payments;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/paypal")
public class PaymentController {

//    private final PaypalService paypalService;
//    private final PaymentRepository paymentRepository;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Value("${paypal.base-url}")
//    private String paypalBaseUrl;
//
//    public PaymentController(PaypalService paypalService, PaymentRepository paymentRepository) {
//        this.paypalService = paypalService;
//        this.paymentRepository = paymentRepository;
//    }
//
//    // Create order and return approval URL(s)
//    @PostMapping("/create-order")
//    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest req) {
//        try {
//            // return and cancel URLs - PayPal will redirect user here after approval/cancel
//            String returnUrl = req.getReturnUrl(); // e.g., https://yourdomain.com/paypal/return
//            String cancelUrl = req.getCancelUrl(); // e.g., https://yourdomain.com/paypal/cancel
//
//            JsonNode orderResp = paypalService.createOrder(req.getAmount(), req.getCurrency(), returnUrl, cancelUrl);
//            String orderId = orderResp.get("id").asText();
//
//            // extract approve link
//            String approveLink = null;
//            for (JsonNode link : orderResp.get("links")) {
//                if ("approve".equals(link.get("rel").asText())) approveLink = link.get("href").asText();
//            }
//
//            // persist minimal Payment record with status CREATED
//            Payment p = Payment.builder()
//                    .orderId(orderId)
//                    .status("CREATED")
//                    .currency(req.getCurrency())
//                    .amount(req.getAmount())
//                    .build();
//            paymentRepository.save(p);
//
//            Map<String, Object> resp = new HashMap<>();
//            resp.put("orderId", orderId);
//            resp.put("approveLink", approveLink);
//            resp.put("raw", orderResp);
//            return ResponseEntity.ok(resp);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    // Capture endpoint (called after user approves and PayPal redirects to your return_url)
//    // You can call this from frontend after PayPal redirect with ?token=<ORDERID>
//    @PostMapping("/capture/{orderId}")
//    public ResponseEntity<?> capture(@PathVariable String orderId) {
//        try {
//            JsonNode captureResp = paypalService.captureOrder(orderId);
//
//            // find capture id & status
//            JsonNode purchaseUnit = captureResp.path("purchase_units").get(0);
//            JsonNode payments = purchaseUnit.path("payments");
//            JsonNode captures = payments.path("captures");
//            String captureId = captures.get(0).path("id").asText();
//            String status = captures.get(0).path("status").asText();
//            String amount = captures.get(0).path("amount").path("value").asText();
//            String currency = captures.get(0).path("amount").path("currency_code").asText();
//
//            Payment p = paymentRepository.findById(orderId).orElse(Payment.builder().orderId(orderId).build());
//            p.setCaptureId(captureId);
//            p.setStatus(status);
//            p.setAmount(amount);
//            p.setCurrency(currency);
//            paymentRepository.save(p);
//
//            return ResponseEntity.ok(Map.of("status", status, "captureId", captureId, "raw", captureResp));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
//        }
//    }
//
//    // Webhook entrypoint — PayPal will POST here
//    @PostMapping("/webhook")
//    public ResponseEntity<String> webhook(
//            @RequestHeader("PAYPAL-TRANSMISSION-ID") String transmissionId,
//            @RequestHeader("PAYPAL-TRANSMISSION-TIME") String transmissionTime,
//            @RequestHeader("PAYPAL-CERT-URL") String certUrl,
//            @RequestHeader("PAYPAL-AUTH-ALGO") String authAlgo,
//            @RequestHeader("PAYPAL-TRANSMISSION-SIG") String transmissionSig,
//            @RequestBody String body) {
//
//        try {
//            // Verify signature by calling PayPal verify API
//            boolean verified = paypalService.verifyWebhook(
//                    transmissionId, transmissionTime, certUrl, authAlgo, transmissionSig, body);
//
//            if (!verified) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid-webhook-signature");
//            }
//
//            // parse event
//            JsonNode event = objectMapper.readTree(body);
//            String eventType = event.path("event_type").asText();
//
//            // handle relevant events
//            if ("PAYMENT.CAPTURE.COMPLETED".equalsIgnoreCase(eventType) ||
//                    "CHECKOUT.ORDER.APPROVED".equalsIgnoreCase(eventType) ||
//                    "PAYMENT.CAPTURE.DENIED".equalsIgnoreCase(eventType)) {
//
//                // For capture completed: find capture and related order id
//                if (event.has("resource")) {
//                    JsonNode resource = event.get("resource");
//                    // For PAYMENTS.CAPTURE.* event, resource.order_id may be present or resource.supplementary_data
//                    String orderId = null;
//                    if (resource.has("supplementary_data") && resource.get("supplementary_data").has("related_ids") ) {
//                        JsonNode related = resource.get("supplementary_data").get("related_ids");
//                        if (related.has("order_id")) orderId = related.get("order_id").asText();
//                    }
//                    // fallback: resource.get("id") or resource.get("invoice_id")
//                    // Many events include order id under different paths; adjust per real payloads.
//
//                    // If we have order id, update DB. Otherwise try to inspect resource keys.
//                    if (orderId != null) {
//                        Payment p = paymentRepository.findById(orderId).orElse(null);
//                        if (p != null) {
//                            p.setStatus(resource.path("status").asText());
//                            if (resource.has("id")) p.setCaptureId(resource.get("id").asText());
//                            paymentRepository.save(p);
//                        }
//                    }
//                }
//            }
//
//            // Acknowledge
//            return ResponseEntity.ok("received");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
//        }
//    }
//
//    // Optional: check payment status by orderId
//    @GetMapping("/status/{orderId}")
//    public ResponseEntity<?> status(@PathVariable String orderId) {
//
//        Payment payment=paymentRepository.findById(orderId).orElse(null);
//        if(payment!=null)
//            return new ResponseEntity<>(payment,HttpStatus.OK);
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }
}