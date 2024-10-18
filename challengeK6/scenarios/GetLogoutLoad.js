import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { check, sleep } from 'k6';
import http from 'k6/http';

export function handleSummary(data) {
    const now = new Date();
    const formattedDate = now.toISOString().replace(/T/, '-').replace(/:/g, '').substring(0, 17);
    const fileName = `reportLogout${formattedDate}.html`;

    const reportPath = `./reports/${fileName}`;

    return {
        [reportPath]: htmlReport(data),
    };
}

export const options = {
    stages: [
        { duration: '30s', target: 50 },
        { duration: '1m', target: 50 },
        { duration: '30s', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<1000'], //95% should return less than 1s
        http_req_failed: ['rate<0.01'] //1% of requests can fail
    }
}

export default function () {
    const res = http.get('http://localhost:8080/api/v3/user/logout')

    check(res, {
        'status should be 200': (r) => r.status === 200
    })

    sleep(1)

}