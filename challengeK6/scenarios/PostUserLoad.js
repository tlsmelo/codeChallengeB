import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { check, sleep } from 'k6';
import http from 'k6/http';

export function handleSummary(data) {
    return {
      "summary.html": htmlReport(data),
    };
  }

export const options = {
    stages: [
        {duration: '30s', target: 50},
        {duration: '1m', target: 50},
        {duration: '30s', target: 0},
    ],
    thresholds: {
        http_req_duration: ['p(95)<1000'], //95% should return less than 1s
        http_req_failed: ['rate<0.10'] //10% of requests can fail - after some iterations the error 500 begin to happen
    }
}

export default function () {
    const url = 'http://localhost:8080/api/v3/user'
    const payload = JSON.stringify({
        id: Math.floor(Math.random() * 5000),
        username: "theUser",
        firstName: "John",
        lastName: "James",
        email: "john@email.com",
        password: "12345",
        phone: "12345",
        userStatus: 1
      })

    const headers = {
        'headers': {
            'Content-Type': 'application/json'
        }
    }

    const res = http.post(url, payload, headers)

    console.log(res.body)

    check (res, {
        'status should be 200': (r) => r.status === 200
    })

    sleep(1)

}