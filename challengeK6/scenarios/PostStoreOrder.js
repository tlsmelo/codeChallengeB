import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { check, sleep } from 'k6';
import http from 'k6/http';

export function handleSummary(data) {
    const now = new Date();
    const formattedDate = now.toISOString().replace(/T/, '-').replace(/:/g, '').substring(0, 17);
    const fileName = `reportStoreOrder${formattedDate}.html`;
    return {
        [fileName]: htmlReport(data),
    };
}

export const options = {
    vus: 10,
    duration: '60s',
    thresholds: {
        http_req_duration: ['p(95)<1000'], //95% should return less than 1s
        http_req_failed: ['rate<0.10'] //10% of requests can fail - after some iterations the error 500 begin to happen
    }
}

export default function () {
    const url = 'http://localhost:8080/api/v3/store/order'
    const payload = JSON.stringify({
        id: Math.floor(Math.random() * 5000),
        petId: Math.floor(Math.random() * 10000),
        quantity: 10,
        shipDate: "2024-10-30T13:29:58.646Z",
        status: "approved",
        complete: true
    })

    const headers = {
        'headers': {
            'Content-Type': 'application/json'
        }
    }

    const res = http.post(url, payload, headers)

    console.log(res.body)

    check(res, {
        'status should be 200': (r) => r.status === 200
    })

    sleep(1)

}