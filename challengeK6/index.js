import { group, sleep } from "k6";
import GetLogin from "./scenarios/GetLogin.js";

export default () => {
    group('Endpoint Get Login', () => {
        GetLogin();
    });

    sleep(1);

}