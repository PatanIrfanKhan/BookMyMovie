package com.example.BookMyMovie.Payments;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;

@Service
public class PaypalService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Value("${paypal.webhook-id}")
    private String webhookId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 1) Get OAuth2 access token
    public String getAccessToken() throws Exception {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        URL url = new URL(baseUrl + "/v1/oauth2/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Basic " + encodedAuth);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write("grant_type=client_credentials".getBytes());
        }

        if (conn.getResponseCode() >= 400) {
            throw new RuntimeException("Failed to get token. HTTP: " + conn.getResponseCode());
        }

        try (InputStream is = conn.getInputStream()) {
            JsonNode resp = objectMapper.readTree(is);
            return resp.get("access_token").asText();
        }
    }

    // 2) Create order
    public JsonNode createOrder(String amount, String currency, String returnUrl, String cancelUrl) throws Exception {
        String token = getAccessToken();
        URL url = new URL(baseUrl + "/v2/checkout/orders");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");

        String body = """
        {
          "intent": "CAPTURE",
          "purchase_units": [{
            "amount": {
              "currency_code": "%s",
              "value": "%s"
            }
          }],
          "application_context": {
            "brand_name": "BookMyMovie",
            "return_url": "%s",
            "cancel_url": "%s"
          }
        }
        """.formatted(currency, amount, returnUrl, cancelUrl);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes());
        }

        if (conn.getResponseCode() >= 400) {
            String err = readStream(conn.getErrorStream());
            throw new RuntimeException("CreateOrder failed: " + err);
        }

        try (InputStream is = conn.getInputStream()) {
            return objectMapper.readTree(is);
        }
    }

    // 3) Capture order
    public JsonNode captureOrder(String orderId) throws Exception {
        String token = getAccessToken();
        URL url = new URL(baseUrl + "/v2/checkout/orders/" + orderId + "/capture");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");

        if (conn.getResponseCode() >= 400) {
            String err = readStream(conn.getErrorStream());
            throw new RuntimeException("Capture failed: " + err);
        }

        try (InputStream is = conn.getInputStream()) {
            return objectMapper.readTree(is);
        }
    }

    // 4) Verify webhook signature using PayPal verify API
    // You should pass the raw webhook JSON (eventBody) and the headers received from PayPal.
    public boolean verifyWebhook(String transmissionId, String transmissionTime, String certUrl,
                                 String authAlgo, String transmissionSig, String webhookEventBody) throws Exception {

        String token = getAccessToken();
        URL url = new URL(baseUrl + "/v1/notifications/verify-webhook-signature");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/json");

        String payload = """
        {
          "transmission_id": "%s",
          "transmission_time": "%s",
          "cert_url": "%s",
          "auth_algo": "%s",
          "transmission_sig": "%s",
          "webhook_id": "%s",
          "webhook_event": %s
        }
        """.formatted(transmissionId, transmissionTime, certUrl, authAlgo, transmissionSig, webhookId, webhookEventBody);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
        }

        if (conn.getResponseCode() >= 400) {
            String err = readStream(conn.getErrorStream());
            throw new RuntimeException("Verify webhook failed: " + err);
        }

        try (InputStream is = conn.getInputStream()) {
            JsonNode resp = objectMapper.readTree(is);
            String status = resp.path("verification_status").asText();
            return "SUCCESS".equalsIgnoreCase(status);
        }
    }

    private String readStream(InputStream is) throws IOException {
        if (is == null) return "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String l;
            while ((l = br.readLine()) != null) sb.append(l);
            return sb.toString();
        }
    }
}
