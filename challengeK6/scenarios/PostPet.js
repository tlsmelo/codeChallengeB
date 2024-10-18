import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { check, sleep } from 'k6';
import http from 'k6/http';

export function handleSummary(data) {
  const now = new Date();
  const formattedDate = now.toISOString().replace(/T/, '-').replace(/:/g, '').substring(0, 17);
  const fileName = `reportPet${formattedDate}.html`;

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
    http_req_duration: ['p(95)<500'], //95% should return less than 500ms
    http_req_failed: ['rate<0.10'] //10% of requests can fail - after some iterations the error 500 begin to happen
  }
}

export default function () {
  const url = 'http://localhost:8080/api/v3/pet'
  const payload = JSON.stringify(
    {
      "id": Math.floor(Math.random() * 5000),
      "name": "doggie",
      "category": {
        "id": 1,
        "name": "Dogs"
      },
      "photoUrls": [
        "string"
      ],
      "tags": [
        {
          "id": 0,
          "name": "string"
        }
      ],
      "status": "available"
    })

  const headers = {
    'headers': {
      'Content-Type': 'application/json'
    }
  }

  const res = http.post(url, payload, headers)

  console.log(res.body)

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