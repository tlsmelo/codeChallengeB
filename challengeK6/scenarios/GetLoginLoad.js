import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { check, sleep } from 'k6';
import http from 'k6/http';

import uuid from './libs/uuid.js';

export function handleSummary(data) {
    const now = new Date();
    const formattedDate = now.toISOString().replace(/T/, '-').replace(/:/g, '').substring(0, 17);
    const fileName = `reportLogin${formattedDate}.html`;

    const reportPath = `./reports/${fileName}`;

    return {
        [reportPath]: htmlReport(data),
    };
}

export const options = {
    stages: [
        { duration: '1m', target: 100 },
        { duration: '2m', target: 100 },
        { duration: '1m', target: 0 },
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'], //95% should return less than 500ms
        http_req_failed: ['rate<0.01'] //1% of requests can fail
    }
}

export default function () {
    let username = `${uuid.v4()}@test.com`
    let password = 'test123'
    const res = http.get(`http://localhost:8080/api/v3/user/login?username=${username}&password=${password}`)

    check(res, {
        'status should be 200': (r) => {
            if (r.status !== 200) {
                fail(`Expected 200 but got ${r.status}`);
            }
            return r.status === 200;
        },
    });

    sleep(1)

}