import http from "k6/http";
import { check } from "k6";

export const options = {
  vus: 50,        // 并发用户数
  iterations: 200 // 总请求数（大于库存）
};

const BASE = "http://localhost:8080";
const PRODUCT_ID = 14; // 改成你的

export default function () {
  const requestId = `${__VU}-${__ITER}-${Date.now()}`;

  const url = `${BASE}/api/order`;
  const payload = JSON.stringify({ productId: PRODUCT_ID, count: 1 });

  const params = {
    headers: {
      "Content-Type": "application/json",
      "X-Request-Id": requestId
    }
  };

  const res = http.post(url, payload, params);

  check(res, {
    "status is 200": (r) => r.status === 200
  });
}