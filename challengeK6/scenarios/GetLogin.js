import { check, fail, sleep } from 'k6';
import http from 'k6/http';
import { Rate, Trend } from "k6/metrics";

//time of miliseconds of req
export let GetLoginDuration = new Trend('get_login_duration');
//percentage of failure
export let GetLoginFailRate = new Rate('get_login_fail_rate');
//percentage of success
export let GetLoginSuccessRate = new Rate('get_login_success_rate');
//percentage of reqs
export let GetLoginReqs = new Rate('get_login_reqs');

export default function () {
    let res = http.get('http://localhost:8080/api/v3/user/login?username=test&password=test')

    GetLoginDuration.add(res.timings.duration);
    GetLoginReqs.add(1);
    GetLoginFailRate.add(res.status != 200);
    GetLoginSuccessRate.add(res.status == 200);

    let durationMsg = 'Max Duration $(1000/1000)s'
    if (!check(res, {
        'max duration': (r) => r.timings.duration < 1000,
    })){
        fail(durationMsg);
    }

    sleep(1);

}