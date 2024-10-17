import { group, sleep } from "k6";
import GetLoginLoad from "./scenarios/GetLoginLoad.js";

export const options = {
    stages: [
        {duration: '30s', target: 50},
        {duration: '1m', target: 50},
        {duration: '30s', target: 0},
    ],
    thresholds: {
        http_req_duration: ['p(95)<1000'], //95% should return less than 1s
        http_req_failed: ['rate<0.01'] //1% of requests can fail
    }
}

export default () => {
    group('Endpoint User', () => {
        GetLoginLoad();
    });

    sleep(1);

}